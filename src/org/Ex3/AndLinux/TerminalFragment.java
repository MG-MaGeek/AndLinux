package org.Ex3.AndLinux;
import android.app.*;
import android.os.*;
import android.view.*;
import jackpal.androidterm.emulatorview.*;
import android.util.*;
import android.content.res.*;
import org.Ex3.AndLinux.utils.*;
import jackpal.androidterm.*;

public class TerminalFragment extends Fragment
{
	private TermSession session;
	private EmulatorView ev;
	
	public TerminalFragment(TermSession session)
	{
		this.session = session;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.d("termfragment", "oncreateview");
		View view = inflater.inflate(R.layout.termfragment, container, false);
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		ev = (EmulatorView) view.findViewById(R.id.termfragmentEmulatorView);
		ev.setDensity(dm);
		ev.setControlKeyCode(KeyEvent.KEYCODE_VOLUME_DOWN);
		ev.setFnKeyCode(KeyEvent.KEYCODE_VOLUME_UP);
		return view;
	}
	
	public EmulatorView getEmulatorView()
	{
		return ev;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		ev.attachSession(session);
	}
}
