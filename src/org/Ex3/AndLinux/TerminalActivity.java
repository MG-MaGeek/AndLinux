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
import jackpal.androidterm.*;
import jackpal.androidterm.util.*;
import org.Ex3.AndLinux.utils.SessionManager.*;

public class TerminalActivity extends Activity
	implements SessionManager.Callback, ActionBar.OnNavigationListener
{
	private SessionManager sm = App.get().getSessionManager();
	//private FrameLayout container;
	private TermView tv;
	private DisplayMetrics dm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d("TermActivity", "onCreate");
		setContentView(R.layout.term);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getActionBar().setListNavigationCallbacks (sm, this);
		getActionBar().setSelectedNavigationItem(sm.getCurrentPos());
		Intent service = new Intent(this, AndLinuxSevice.class);
		startService(service);
	}
	
	public void createTerm(TermSession session)
	{
		Log.d("TermActivity", "createTerm");
		tv = new TermView(this, session, dm);
		tv.setControlKeyCode(KeyEvent.KEYCODE_VOLUME_DOWN);
		tv.setFnKeyCode(KeyEvent.KEYCODE_VOLUME_UP);
		tv.updatePrefs(new TermSettings(getResources(), App.get().getPref()));
		setContentView(tv, new ViewGroup.LayoutParams(-1, -1));
		//container.removeAllViews();
		//container.addView(tv, -1, -1);
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
					sm.create();
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
					sm.closeCurrentSession();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		sm.init(this);
	}

	@Override
	protected void onResume()
	{
		Log.d("TermActivity", "onReasume beg");
		super.onResume();
		tv.onResume();
		Log.d("TermActivity", "onReasume end");
	}
	
	@Override
	protected void onPause()
	{
		Log.d("TermActivity", "onPause");
		tv.onPause();
		super.onPause();
	}

	@Override
	protected void onStop()
	{
		sm.clearCb();
		super.onStop();
	}
	
	@Override
	public void onSessionCreate(TermSession session)
	{
		getActionBar().setSelectedNavigationItem(sm.getCurrentPos());
		createTerm(session);
	}
	
	@Override
	public boolean onSessionFinish()
	{
		Log.d("TermActivity", String.valueOf(sm.getCount()));
		if (sm.getCount() == 0)
		{
			finish();
			return true;
		}
		return false;
	}
	
	@Override
	public void onSessionSelect(TermSession session)
	{
		getActionBar().setSelectedNavigationItem(sm.getCurrentPos());
		createTerm(session);
	}
	
	@Override
	public boolean onNavigationItemSelected(int pos, long id)
	{
		if (pos == sm.getCurrentPos())
		{
			new AlertDialog.Builder(this)
				.setIcon(R.drawable.andlinux_logo)
				.setCancelable(true)
				.setMessage(R.string.set_title);
			
			return true;
		}
		else
		{
			sm.setCurrentPos(pos);
		}
		return true;
	}
}
