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
	private Callback cb;
	private int count = 0;
	private int currentPos = -1;
	
	public ShellTermSession get(int pos)
	{
		return sessions.get(pos);
	}
	
	private void updatenum()
	{
		if (App.get().als == null)
			return;
		App.get().als.update(sessions.size());
	}
	
	public class SessionNotFound extends Throwable
	{
		@Override
		public String getMessage()
		{
			return "Session could not be found.";
		}
	}
	
	public ShellTermSession getCurrentSession() throws SessionNotFound
	{
		if (currentPos >= getCount())
			throw new SessionNotFound();
		return sessions.get(currentPos);
	}
	
	public interface Callback
	{
		public boolean onSessionFinish();
		
		public void onSessionCreate(TermSession session);
		
		public void onSessionSelect(TermSession session)
	}
	
	public void init(Callback cb)
	{
		this.cb = cb;
		if (sessions.isEmpty())
			create();
	}
	
	public void create()
	{
		try
		{
			String cmd = "export ALIF=/data/data/org.Ex3.AndLinux/files;exec $ALIF/start";
			ShellTermSession session =
				new ShellTermSession(new TermSettings(App.get().getResources(),
				PreferenceManager.getDefaultSharedPreferences(App.get())), cmd);
			session.initializeEmulator(80, 25);
			session.setDefaultUTF8Mode(true);
			session.setFinishCallback(this);
			session.setTitle("Window #" + ++count);
			session.setTitleChangedListener(this);
			sessions.add(session);
			updatenum();
			currentPos = sessions.size() - 1;
			notifyDataSetChanged();
			
			if (cb != null) cb.onSessionCreate(session);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void closeCurrentSession()
	{
		if (currentPos < getCount())
			sessions.get(currentPos).finish();
		notifyDataSetChanged();
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
		updatenum();
		if (currentPos == sessions.size())
			currentPos--;
		notifyDataSetChanged();
		if (cb != null)
		{
			if (!cb.onSessionFinish() && currentPos != -1)
				cb.onSessionSelect(get(currentPos));
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
		LayoutInflater inflater = LayoutInflater.from(vg.getContext());
		//TextView text = (TextView)ViewGroup.inflate(App.get(), android.R.layout.simple_expandable_list_item_1, vg);
		//TextView text = (TextView) mInflater.inflate(, vg, false);
		TextView text = (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, vg, false);
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
		if (cb != null) cb.onSessionSelect(get(currentPos));
	}
	
	@Override
	public void onUpdate()
	{
		notifyDataSetChanged();
	}
}
