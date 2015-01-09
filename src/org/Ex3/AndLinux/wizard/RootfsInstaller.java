package org.Ex3.AndLinux.wizard;
import org.Ex3.AndLinux.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.app.*;
import android.net.*;
import java.net.*;

public class RootfsInstaller extends WizardFragment
{
	EditText pathedit;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.wizard_rootfs_installer, container, false);
		pathedit = (EditText)view.findViewById(R.id.wizardrootfsinstallerPathEdit);
		((Button)view.findViewById(R.id.wizardrootfsinstallerSelectFileButton)).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					Intent fileselect = new Intent(Intent.ACTION_GET_CONTENT);
					fileselect.setType("*/*");
					fileselect.addCategory(Intent.CATEGORY_OPENABLE);
					try
					{
						startActivityForResult(
							Intent.createChooser(
								fileselect, App.get().getString(R.string.select_tgz)
							), 1);
					}
					catch (android.content.ActivityNotFoundException ex)
					{
						
					}
				}
			});
		((Button)view.findViewById(R.id.wizardrootfsinstallerInstallButton)).setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					ProgressDialog pd = new ProgressDialog(getActivity());
					pd.setIcon(R.drawable.andlinux_logo);
					pd.setTitle(R.string.installing);
					pd.setIndeterminate(false);
					App.get().setProgressDialog(pd);
					App.get().installrootfs(pathedit.getText().toString(), App.get().getFilesDir().getPath());
				}
			});
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK)
		{
			String url = data.getData().getPath();
			pathedit.setText(url);
		}
	}

	@Override
	public String getName()
	{
		return App.get().getString(R.string.rootfs_installer);
	}
}
