package org.Ex3.AndLinux.wizard;
import android.app.*;
import android.os.*;
import android.view.*;
import org.Ex3.AndLinux.*;
import android.widget.*;

public class FrameworkInstaller extends WizardFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.wizard_framework_installer, container, false);
		((Button)view.findViewById(R.id.wizardframeworkinstallerButton)).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					ProgressDialog pd = new ProgressDialog(getActivity());
					pd.setIcon(R.drawable.andlinux_solid);
					pd.setTitle(R.string.installing);
					pd.setIndeterminate(false);
					App.get().setProgressDialog(pd);
					App.get().installframework();
				}
			});
		return view;
	}

	@Override
	public String getName()
	{
		return App.get().getResources().getString(R.string.frameworks_installer);
	}
}
