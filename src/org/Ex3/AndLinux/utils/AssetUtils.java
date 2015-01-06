package org.Ex3.AndLinux.utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

public class AssetUtils
{

	public static void copyAssetDirToFiles(Context context, String root, String dirname)
		throws IOException
	{
		File dir = new File(root + "/" + dirname);
		dir.mkdir();

		AssetManager assetManager = context.getAssets();
		String[] children = assetManager.list(dirname);
		for (String child : children)
		{
			child = dirname + '/' + child;
			String[] grandChildren = assetManager.list(child);
			if (0 == grandChildren.length)
				copyAssetFileToFiles(context, root, child);
			else
				copyAssetDirToFiles(context, root, child);
		}
	}

	public static void copyAssetFileToFiles(Context context, String root, String filename)
		throws IOException
	{
		InputStream is = context.getAssets().open(filename);
		byte[] buffer = new byte[is.available()];
		is.read(buffer);
		is.close();

		File tgtfile = new File(root + "/" + filename);
		tgtfile.createNewFile();
		tgtfile.setExecutable(true);
		FileOutputStream os = new FileOutputStream(tgtfile);
		os.write(buffer);
		os.close();
	}
}
