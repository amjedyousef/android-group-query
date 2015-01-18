package org.tudelft.neat.eventsources;

import org.tudelft.neat.OutputLog;

import com.stericson.RootTools.execution.Command;

public class LogCommand extends Command
{

	private static int id = 0;
	private final OutputLog log;
	private final String tag;

	public LogCommand(OutputLog log, String tag, String command)
	{
		super(id++, -1, command);
		this.log = log;
		this.tag = tag;
	}

	@Override
	public void commandOutput(int id, String line)
	{
		log.append(tag, line);
	}

	@Override
	public void commandTerminated(int id, String reason)
	{
		System.out.println("[" + tag + "] Terminated: " + reason);
	}

	@Override
	public void commandCompleted(int id, int exitCode)
	{
		System.out.println("[" + tag + "] Completed: " + exitCode);
	}
	
	public OutputLog getLog()
	{
		return log;
	}

}
