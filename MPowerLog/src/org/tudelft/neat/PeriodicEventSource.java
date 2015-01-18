package org.tudelft.neat;

import android.util.Log;


public abstract class PeriodicEventSource extends EventSource implements Runnable
{
	
	private Thread runThread;
	private boolean active = false;
	private final long interval;
	private OutputLog log;

	public PeriodicEventSource(MPowerApplication application, long interval)
	{
		super(application);
		this.interval = interval;
	}

	@Override
	public void start(OutputLog log)
	{
		active = true;
		
		this.log = log;
		
		runThread = new Thread()
		{
			@Override
			public void run()
			{
				
				while (active)
				{
					try
					{
						PeriodicEventSource.this.run();
						
						Thread.sleep(interval);
					} catch (InterruptedException e)
					{
						// Ignore
					}
					
					
				}
				
			}
		};
		runThread.start();
		
	}
	
	@Override
	public void stop()
	{
		active = false;
		runThread.interrupt();
		try
		{
			runThread.join(1000L);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public OutputLog getLog()
	{
		return log;
	}

}
