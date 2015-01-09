package org.Ex3.AndLinux;
import android.content.*;
import android.net.*;
import android.database.*;
import android.os.*;
import java.io.*;

public class FilesContentProvider extends ContentProvider
{
	public static final Uri CONTENT_URI = Uri.parse("content://AndLinux");

	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException, FileNotFoundException
	{
		File file = new File(getContext().getFilesDir(), uri.getPath());
		int imode = 0;
		if (mode.contains("r"))
		{
			if (mode.contains("w"))
				imode = ParcelFileDescriptor.MODE_READ_WRITE;
			else
				imode = ParcelFileDescriptor.MODE_READ_ONLY;
				
		}
		else if (mode.contains("w"))
			imode = ParcelFileDescriptor.MODE_WRITE_ONLY;
		if (mode.contains("+"))
			imode |= ParcelFileDescriptor.MODE_APPEND;
		return ParcelFileDescriptor.open(file, imode);
	}
	
	@Override
	public boolean onCreate()
	{
		return true;
	}

	@Override
	public Cursor query(Uri p1, String[] p2, String p3, String[] p4, String p5)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public String getType(Uri p1)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public Uri insert(Uri p1, ContentValues p2)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public int delete(Uri p1, String p2, String[] p3)
	{
		// TODO: Implement this method
		return 0;
	}

	@Override
	public int update(Uri p1, ContentValues p2, String p3, String[] p4)
	{
		// TODO: Implement this method
		return 0;
	}
	
}
