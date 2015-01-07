package org.Ex3.AndLinux;
import android.app.*;
import android.os.*;
import jackpal.androidterm.emulatorview.*;
import android.util.*;
import android.content.res.*;
import android.widget.*;
import org.Ex3.AndLinux.utils.*;
import android.view.*;
import android.view.inputmethod.*;
import android.content.*;
import android.app.ActionBar.*;

public class TerminalActivity extends Activity
	implements TermSession.FinishCallback, ActionBar.OnNavigationListener, Runnable
{
	private TerminalFragment fragment;
	private int pos;
	private FragmentManager fm;
	private SessionManager mSessionManager = App.get().getSessionManager();
	private EmulatorView ev;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.term);
		if (mSessionManager.getCount() == 0)
		{
			pos = mSessionManager.create();
		}
		else pos = mSessionManager.getCurrentPos();
		mSessionManager.setCb(this);
		
		fragment = new TerminalFragment(mSessionManager.get(pos));
		fm = getFragmentManager();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getActionBar().setListNavigationCallbacks (mSessionManager, this);
		getActionBar().setSelectedNavigationItem(pos);
		try
		{
			runOnUiThread(this);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	@Override
	public void run()
	{
		fm.beginTransaction()
			.replace(R.id.termFrame, fragment)
			.commit();
		ev = fragment.getEmulatorView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem keyboard = menu.add(R.string.keyboard);
		keyboard.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		keyboard.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
			{
				@Override
				public boolean onMenuItemClick(MenuItem p1)
				{
					toggleSoftKeyboard();
					return true;
				}
			});
		MenuItem add = menu.add(R.string.new_window);
		add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
			{
				@Override
				public boolean onMenuItemClick(MenuItem p1)
				{
					int pos = mSessionManager.create();
					getActionBar().setSelectedNavigationItem(pos);
					mSessionManager.setCurrentPos(pos);
					fragment = new TerminalFragment(mSessionManager.get(pos));
					runOnUiThread(TerminalActivity.this);
					return true;
				}
			});
		MenuItem closebtn = menu.add(R.string.close_window);
		closebtn.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		closebtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
			{
				@Override
				public boolean onMenuItemClick(MenuItem p1)
				{
					mSessionManager.closeCurrentSession();
					return true;
				}
			});
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			App.get().termstate = -1;
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void toggleSoftKeyboard()
	{
        InputMethodManager imm = (InputMethodManager)
            getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }
	
	private void close()
	{
		App.get().termstate += 1;
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			App.get().termstate = -1;
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		close();
	}
	
	@Override
	public void onSessionFinish(TermSession session)
	{
		if (mSessionManager.getCount() > 0)
		{
			finish();
			overridePendingTransition(0, 0);
			return;
		}
		App.get().termstate = -1;
		finish();
	}

	@Override
	public boolean onNavigationItemSelected(int pos, long id)
	{
		if (pos == mSessionManager.getCurrentPos())
		{
			new AlertDialog.Builder(this)
				.setIcon(R.drawable.andlinux_solid)
				.setCancelable(true)
				.setMessage(R.string.set_title);
			
			return true;
		}
		else
		{
			mSessionManager.setCurrentPos(pos);
			fragment = new TerminalFragment(mSessionManager.get(pos));
			runOnUiThread(this);
		}
		return true;
	}
}
