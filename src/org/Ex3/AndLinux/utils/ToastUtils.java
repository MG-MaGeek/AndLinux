package org.Ex3.AndLinux.utils;
import android.widget.*;
import android.content.*;
import android.os.*;

public class ToastUtils implements Runnable
{
	String txt;
	Context context;
	int dur;
	private ToastUtils(Context context, String txt, int dur)
	{
		this.context = context;
		this.txt = txt;
		this.dur = dur;
	}
	
	public static void showText(Context context, String txt, int dur)
	{
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new ToastUtils(context, txt, dur));
	}
	
	@Override
	public void run()
	{
		Toast.makeText(context, txt, dur).show();
	}
}
