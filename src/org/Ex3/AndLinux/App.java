package org.Ex3.AndLinux;
import android.app.*;
import android.util.*;
import android.content.*;
import org.Ex3.AndLinux.utils.*;
import java.io.*;
import android.view.*;
import android.widget.*;
import android.os.*;
import java.lang.Process;
import org.apache.http.util.*;
import jackpal.androidterm.emulatorview.*;
import java.util.*;

public class App extends Application
	implements Thread.UncaughtExceptionHandler
{
	private static App app;
	private SharedPreferences pref;
	private ProgressDialog pdialog;
	private SessionManager sessionManager;
	public AndLinuxSevice als;
	
	@Override
	public void uncaughtException(Thread p1, Throwable p2)
	{
		Log.e("UNCAUGHT", "ERROR:", p2);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			File logfile = new File("/sdcard/ALICrash.log");
			try
			{
				if (!logfile.exists()) logfile.createNewFile();
				RandomAccessFile raf = new RandomAccessFile(logfile, "rw");
				raf.seek(raf.length());
				raf.write(p2.getMessage().getBytes());
				raf.close();
			}
			catch (IOException e)
			{}

		}
		System.exit(0);
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		app = this;
		pref = getSharedPreferences(getPackageName() + "_preferences", 0);
		sessionManager = new SessionManager();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	public static App get()
	{
		return app;
	}
	
	public SessionManager getSessionManager()
	{
		return sessionManager;
	}
	
	public SharedPreferences getPref()
	{
		return pref;
	}
	
	public void setProgressDialog(ProgressDialog progdialog)
	{
		pdialog = progdialog;
	}
	
	public void installframework()
	{
		if (pdialog != null) pdialog.show();
		new ThreadUtils(null)
		{
			@Override
			public void run()
			{
				installframework_priv();
				if (pdialog != null) pdialog.dismiss();
				pdialog = null;
			}
		}.start();
	}
	
	public void installframework_priv()
	{
		String path = getFilesDir().getPath();
		try
		{
			AssetUtils.copyAssetDirToFiles(this, path, "utils");
		}
		catch (IOException e)
		{
			ToastUtils.showText(this, e.getMessage(), Toast.LENGTH_LONG);
			return;
		}
		ToastUtils.showText(this, getString(R.string.success), Toast.LENGTH_LONG);
		return;
	}
	
	public void installrootfs(String from, String to)
	{
		pdialog.show();
		Bundle bd = new Bundle();
		Log.d("install", from);
		bd.putString("from", from);
		bd.putString("to", to);
		new ThreadUtils(bd)
		{
			@Override
			public void run()
			{
				String from = ((Bundle)bind).getString("from");
				String to = ((Bundle)bind).getString("to");
				installrootfs_priv(from, to);
				pdialog.dismiss();
			}
		}.start();
	}
	
	private boolean installrootfs_priv(String from, String target)
	{
		try
		{
			Runtime runtime = Runtime.getRuntime();
			String[] envp = {"BUSYBOX=/data/data/org.Ex3.AndLinux/files/utils/busybox",
				"SRC=" + from,
				"TGT=" + target};
				int ret;
			Process process = runtime.exec("/data/data/org.Ex3.AndLinux/files/utils/install.sh", envp);
			if ((ret = process.waitFor()) != 0)
			{
				Log.d("install", String.valueOf(ret));
				ToastUtils.showText(this, getString(R.string.error), Toast.LENGTH_LONG);
				return false;
			}
		}
		catch (Throwable ob)
		{
			ob.printStackTrace();
			ToastUtils.showText(this, ob.getMessage(), Toast.LENGTH_LONG);
			return false;
		}
		ToastUtils.showText(this, getString(R.string.success), Toast.LENGTH_LONG);
		return true;
	}
}
