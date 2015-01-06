package org.Ex3.AndLinux;

import android.app.*;
import android.graphics.drawable.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.io.*;
import org.apache.http.util.*;
import android.net.*;

public class MainActivity extends Activity 
{
	Spanned text;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		//getWindow().setIcon(R.drawable.andlinux_solid);
		//getWindow().setTitle(getString(R.string.installer));
		getActionBar().setTitle(R.string.installer);
		getActionBar().setSubtitle(R.string.app_name);
		text = Html.fromHtml(getResources().getString(R.string.info), new Html.ImageGetter()
			{
				@Override
				public Drawable getDrawable(String p1)
				{
					Drawable d = getResources().getDrawable(R.drawable.andlinux_solid);
					d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
					return d;
				}
			}, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		View view = getLayoutInflater().inflate(R.layout.updatelog, null);
		InputStream in = getResources().openRawResource(R.raw.update);
		String str = "";
		try
		{
			byte data[] = new byte[in.available()];
			in.read(data);
			str = EncodingUtils.getString(data, "UTF-8");
		}
		catch (IOException ioexcep)
		{
			ioexcep.printStackTrace();
		}
		((TextView)view.findViewById(R.id.updatelogTextView)).setText(str);
		final AlertDialog updatelog = new AlertDialog.Builder(this)
			.setTitle(R.string.update_log)
			.setView(view)
			.setIcon(R.drawable.andlinux_solid)
			.setPositiveButton(R.string.close, null)
			.create();
		final AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this)
			.setTitle(R.string.about)
			.setMessage(text)
			.setIcon(R.drawable.andlinux_solid)
			.setPositiveButton(R.string.close, null)
			.setNeutralButton(R.string.update_log, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					updatelog.show();
				}
			})
			.setNegativeButton(R.string.sendemail, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					Intent email = new Intent(Intent.ACTION_SENDTO);
					email.setData(Uri.parse("mailto:MG.MaGeek@Gmail.com"));
					startActivity(email);
				}
			});
		MenuItem about = menu.add(R.string.about);
		about.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		about.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
			{
				@Override
				public boolean onMenuItemClick(MenuItem p1)
				{
					alertbuilder.create().show();
					return true;
				}
			});
		MenuItem settings = menu.add(R.string.settings);
		settings.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
			{
				@Override
				public boolean onMenuItemClick(MenuItem p1)
				{
					startActivity(new Intent(MainActivity.this, SettingsActivity.class));
					return true;
				}
			});
		return super.onCreateOptionsMenu(menu);
	}
	
	public void onRunItPress(View view)
	{
		startActivity(new Intent(this, TerminalActivity.class));
	}
	
	public void onWizardPress(View view)
	{
		startActivity(new Intent(this, WizardActivity.class));
	}
	
	public void onUpdateModulesPress(View view)
	{
		
	}
	
	public void onExitPress(View view)
	{
		System.exit(0);
		this.finish();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		if (App.get().termstate == 1)
		{
			App.get().termstate = 0;
			onRunItPress(null);
		}
	}
}
