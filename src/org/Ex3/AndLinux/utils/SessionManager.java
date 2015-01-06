package org.Ex3.AndLinux.utils;
import android.content.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import jackpal.androidterm.*;
import jackpal.androidterm.emulatorview.*;
import jackpal.androidterm.util.*;
import java.util.*;
import java.util.zip.*;
import org.Ex3.AndLinux.*;
import android.app.*;

public class SessionManager extends BaseAdapter
	implements TermSession.FinishCallback, UpdateCallback
{
	private ArrayList<ShellTermSession> sessions = new ArrayList<ShellTermSession>();
	private TermSession.FinishCallback cb;
	private int count = 0;
	private int currentPos = 0;
	
	public ShellTermSession get(int pos)
	{
		return sessions.get(pos);
	}
	
	public int create()
	{
		try
		{
			String cmd = "export ALIF=/data/data/org.Ex3.AndLinux/files\nexport TEMP=$ALIF/tmp\nexec $ALIF/utils/proot -r $ALIF/linuxroot -f $ALIF/bindings /init";
			ShellTermSession session =
				new ShellTermSession(new TermSettings(App.get().getResources(),
				PreferenceManager.getDefaultSharedPreferences(App.get())), cmd);
			session.initializeEmulator(80, 25);
			session.setDefaultUTF8Mode(true);
			session.setFinishCallback(this);
			session.setTitle("Window #" + ++count);
			session.setTitleChangedListener(this);
			sessions.add(session);
			currentPos = sessions.size() - 1;
			notifyDataSetChanged();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		return currentPos;
	}
	
	public void closeCurrentSession()
	{
		sessions.get(currentPos).finish();
	}
	
	public void setCb(TermSession.FinishCallback cb)
	{
		this.cb = cb;
		return;
	}
	
	public void clearCb()
	{
		cb = null;
	}
	
	@Override
	public void onSessionFinish(TermSession session)
	{
		if (currentPos > sessions.indexOf(session))
		{
			currentPos--;
		}
		sessions.remove(session);
		if (currentPos == sessions.size())
			currentPos--;
		notifyDataSetChanged();
		if (cb != null)
		{
			cb.onSessionFinish(session);
			clearCb();
		}
	}
	
	@Override
	public int getCount()
	{
		return sessions.size();
	}
	
	@Override
	public long getItemId(int pos)
	{
		return pos;
	}
	
	@Override
	public View getView(int id, View view, ViewGroup vg)
	{
		Log.d("SessionManager", "inflate start");
		Activity act = (Activity) vg.getContext();
		//TextView text = (TextView)ViewGroup.inflate(App.get(), android.R.layout.simple_expandable_list_item_1, vg);
		//TextView text = (TextView) mInflater.inflate(, vg, false);
		TextView text = (TextView) act.getLayoutInflater().inflate(android.R.layout.simple_dropdown_item_1line, vg, false);
		text.setText(sessions.get(id).getTitle());
		Log.d("SessionManager", "inflate finish");
		return text;
	}
	
	@Override
	public Object getItem(int id)
	{
		return sessions.get(id).getTitle();
	}
	
	public int getCurrentPos()
	{
		return currentPos;
	}
	
	public void setCurrentPos(int pos)
	{
		currentPos = pos;
	}
	
	@Override
	public void onUpdate()
	{
		notifyDataSetChanged();
	}
}
