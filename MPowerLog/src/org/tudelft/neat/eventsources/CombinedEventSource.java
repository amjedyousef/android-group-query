package org.tudelft.neat.eventsources;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.tudelft.neat.EventSource;
import org.tudelft.neat.MPowerApplication;
import org.tudelft.neat.OutputLog;

import android.app.ActivityManager;
import android.content.Context;

import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.Command;
import com.stericson.RootTools.execution.Shell;

public class CombinedEventSource extends EventSource
{

	private Command command;
	private Shell rootShell;

	public CombinedEventSource(MPowerApplication application)
	{
		super(application);
	}

	@Override
	public void start(final OutputLog log)
	{
		ActivityManager activityManager = (ActivityManager) getApplication().getSystemService(Context.ACTIVITY_SERVICE);
		
		try
		{
			rootShell = Shell.startNewRootShell(20000, 3);
			
			// Clear logcat buffer
			command = new LogCatCommand(log, "LOGCAT", "logcat -c -b radio -b main -b system -b events", activityManager);
			rootShell.add(command);
			
			// Start logging
			String bufferString = "";
			if (getApplication().isMainBufferEnabled()) bufferString += " -b main";
			if (getApplication().isSystemBufferEnabled()) bufferString += " -b system";
			if (getApplication().isRadioBufferEnabled()) bufferString += " -b radio";
			if (getApplication().isEventBufferEnabled()) bufferString += " -b events";
			
			if (getApplication().isKernelEventsEnabled())
			{
				command = new LogCatCommand(log, "COMBINED", "logcat -v time" + bufferString + " -f /dev/kmsg | cat /proc/kmsg", activityManager);
			} else
			{
				command = new LogCatCommand(log, "COMBINED", "logcat -v time" + bufferString, activityManager);
			}
			
			rootShell.add(command);
			
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (TimeoutException e)
		{
			e.printStackTrace();
		} catch (RootDeniedException e)
		{
			e.printStackTrace();
		} 
	}

	@Override
	public void stop()
	{
		if (rootShell!=null)
			rootShell.kill();
	}

}
