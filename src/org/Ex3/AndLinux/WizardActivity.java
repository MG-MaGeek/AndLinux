package org.Ex3.AndLinux;
import android.app.*;
import android.os.*;
import org.Ex3.AndLinux.wizard.*;
import android.view.*;
import android.content.res.*;

public class WizardActivity extends Activity
{
	WizardFragment fragments[];

	private int pos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wizard);
		/*
		FrameworkInstaller fi = new FrameworkInstaller();
		*/
		
		fragments = new WizardFragment[3];
		fragments[0] = new Welcome();
		fragments[1] = new FrameworkInstaller();
		fragments[2] = new RootfsInstaller();
		pos = 0;
		setFragment();
	}
	
	private void setFragment()
	{
		FragmentManager fm = getFragmentManager();
		setTitle(fragments[pos].getName());
		fm.beginTransaction()
			.replace(R.id.wizardFrameLayout, fragments[pos])
			.commit();
	}
	
	public void onPrevPress(View view)
	{
		if (pos == 0)
		{
			finish();
			return;
		}
		pos--;
		setFragment();
	}
	
	synchronized public void onNextPress(View view)
	{
		if (pos == fragments.length - 1)
		{
			finish();
			return;
		}
		pos++;
		setFragment();
	}

	@Override
	synchronized public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			onPrevPress(null);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		// TODO: Implement this method
		super.onConfigurationChanged(newConfig);
		getWindow().getDecorView().requestLayout();
	}
}
