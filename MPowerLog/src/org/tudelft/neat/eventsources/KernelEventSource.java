package org.tudelft.neat.eventsources;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.tudelft.neat.EventSource;
import org.tudelft.neat.MPowerApplication;
import org.tudelft.neat.OutputLog;

import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.Command;
import com.stericson.RootTools.execution.Shell;

public class KernelEventSource extends EventSource
{

	private Command command;
	private Shell rootShell;
	
	public KernelEventSource(MPowerApplication application)
	{
		super(application);
	}

	@Override
	public void start(final OutputLog log)
	{
		
		try
		{
			rootShell = Shell.startNewRootShell(20000, 3);

			// Clear kernel buffer
			command = new LogCommand(log, "DMESG", "dmesg -c");
			
			rootShell.add(command);
			
			// Start logging
			command = new KernelLogCommand(log, "DMESG", "cat /proc/kmsg", getApplication());
			
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
