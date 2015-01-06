package org.Ex3.AndLinux.wizard;
import android.app.*;
import android.os.*;
import android.view.*;
import org.Ex3.AndLinux.*;

public class Welcome extends WizardFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.wizard_welcome, container, false);
	}

	@Override
	public String getName()
	{
		return App.get().getResources().getString(R.string.wizard_welcome);
	}
}
