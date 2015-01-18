package org.tudelft.neat.eventsources;

import org.tudelft.neat.OutputLog;

import android.content.Context;
import android.content.Intent;

public class KernelLogCommand extends LogCommand
{
	
	private String resumeTag = "exit suspend, ret = 0";
	private final static String resumeIntent = "org.tudelft.mpower.RESUME";
	private final Context context;

	public KernelLogCommand(OutputLog log, String tag, String command, Context context)
	{
		super(log, tag, command);
		this.context = context;
	}
	
	@Override
	public void commandOutput(int id, String line)
	{
		// Log the event
		super.commandOutput(id, line);
		
		// Fire a broadcast event to notify other applications the phone has come out of suspend
		if (line.contains(resumeTag))
			context.sendBroadcast(new Intent(resumeIntent));
			
	}

}
