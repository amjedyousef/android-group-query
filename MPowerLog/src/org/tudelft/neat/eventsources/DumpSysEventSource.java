package org.tudelft.neat.eventsources;

import org.tudelft.neat.MPowerApplication;
import org.tudelft.neat.OutputLog;
import org.tudelft.neat.PeriodicEventSource;

import android.util.Log;

import com.stericson.RootTools.execution.Shell;

public class DumpSysEventSource extends PeriodicEventSource
{
	
	private Shell rootShell;

	public DumpSysEventSource(MPowerApplication application, long interval)
	{
		super(application, interval);
	}
	
	@Override
	public void start(OutputLog log)
	{
		try
		{
			rootShell = Shell.startNewRootShell(20000, 3);
		} catch (Exception e)
		{
			Log.e("MPowerLog", "Unable to create root shell for DumpSys", e);
		}
		
		// Call through to parent
		super.start(log);
	}
	
	@Override
	public void stop()
	{
		try
		{
			rootShell.close();
		} catch (Exception e)
		{
			Log.e("MPowerLog", "Unable to close root shell for DumpSys", e);
		}
		
		// Call through to parent
		super.stop();
	}

	@Override
	public void run()
	{
		if (rootShell==null)
			return;
		
		// Run dumpsys
		try
		{
			
			Log.d("MPowerLog", "DumpSys run ");
			LogCommand command = new LogCommand(getLog(), "MPowerLog", "dumpsys cpuinfo");
			Shell.runCommand(command);
			
		} catch (Exception e)
		{
			Log.e("MPowerLog", "Unable to close root shell for DumpSys", e);
		}
		
	}

}
