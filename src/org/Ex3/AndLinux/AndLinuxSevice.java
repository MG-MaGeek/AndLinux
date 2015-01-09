package org.Ex3.AndLinux;

import android.app.*;
import android.os.*;
import android.content.*;

public class AndLinuxSevice extends Service
{
	public Notification notification = null;
	
	@Override
	public IBinder onBind(Intent p1)
	{
		return null;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		App.get().als = this;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (notification == null) doCreateNotification();
		return Service.START_NOT_STICKY;
	}
	
	public void update(int num)
	{
		notification.number = num;
		startForeground(1, notification);
	}
	
	public void doCreateNotification()
	{
		notification = new Notification(R.drawable.andlinux_service, getText(R.string.andlinux_service), System.currentTimeMillis());
		PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(this, TerminalActivity.class), 0);
		notification.setLatestEventInfo(this, getText(R.string.app_name), getText(R.string.andlinux_service_info), pendingintent);
		notification.number = App.get().getSessionManager().getCount();
		startForeground(1, notification);
	}
}
