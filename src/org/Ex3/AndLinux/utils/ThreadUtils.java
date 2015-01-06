package org.Ex3.AndLinux.utils;
import android.os.*;

public abstract class ThreadUtils implements Runnable
{
	protected Object bind;
	public ThreadUtils(Object bd)
	{
		bind = bd;
	}
	
	public void start()
	{
		new Thread(this).start();
	}
}
