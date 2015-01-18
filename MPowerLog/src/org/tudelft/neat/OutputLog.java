package org.tudelft.neat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import android.text.TextUtils;

public class OutputLog
{
	
	private final File file;
	private PrintWriter writer;
	
	public OutputLog(File file)
	{
		this.file = file;
	}
	
	public void open() throws FileNotFoundException
	{
		writer = new PrintWriter(file);
	}
	
	public void close()
	{
		writer.flush();
		writer.close();
	}
	
	public synchronized void append(String tag, String ... event)
	{
		writer.write(tag);
		for (String str : event)
		{
			writer.write(",");
			writer.write(TextUtils.htmlEncode(str));
		}
		writer.write("\n");
		writer.flush();
	}

}
