package org.tudelft.neat.eventsources;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tudelft.neat.OutputLog;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;

public class LogCatCommand extends LogCommand
{
	
	private static Pattern logCatPattern = Pattern.compile(".*([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+).([0-9]+) ([A-Z])/(.*?):(.*)");

	private final ActivityManager activityManager;
	
	// TODO: PID's might run out, in which case the kernel will wrap around and re-issue a previously used one.
	// This is unlikely given typical workloads however, so we probably will never see this 'in the wild'.
	private final HashSet<Integer> processNames = new HashSet<Integer>();

	public LogCatCommand(OutputLog log, String tag, String command, ActivityManager activityManager)
	{
		super(log, tag, command);
		this.activityManager = activityManager;
	}
	
	@Override
	public void commandOutput(int id, String line)
	{
		Matcher matcher = logCatPattern.matcher(line);
		
		if (matcher.matches())
		{
			String tagAndPid =  matcher.group(8);
			
			if (tagAndPid.lastIndexOf('(')!=-1)
			{
				int pid = -1;
				try {
					String pidStr = tagAndPid.substring(tagAndPid.lastIndexOf('(')+1, tagAndPid.lastIndexOf(')'));
					pid = Integer.parseInt(pidStr.trim());
				} catch (Exception ex)
				{
					// Parse error
				}
				
				if (pid!=-1 && !processNames.contains(pid))
				{
					List<RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
					if (processes!=null)
						for (RunningAppProcessInfo info : processes)
							if (info.pid==pid)
							{
								String processInfo = String.format("id=%d uid=%d name=%s",
										info.pid,
										info.uid,
										info.processName
										);
								getLog().append("PROCESS", processInfo);
							}
					
					processNames.add(pid);
				}

			}
	
		} 

		// Log the event
		super.commandOutput(id, line);
	}

}
