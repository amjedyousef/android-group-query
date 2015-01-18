package org.tudelft.neat;

import android.util.Log;

public abstract class SyncEventSource extends EventSource
{
	
	private Thread runThread;
	private boolean active = false;
	long lastSyncTime;

	public SyncEventSource(MPowerApplication application)
	{
		super(application);
	}

	@Override
	public void start(OutputLog log)
	{
		active = true;
		
		runThread = new Thread()
		{
			@Override
			public void run()
			{
				
				while (active)
				{
					try
					{
						Thread.sleep(Math.round(10+Math.random()*10)*1000L);
					} catch (InterruptedException e)
					{
						// Ignore
					}
					
					lastSyncTime = System.currentTimeMillis();
					sync();
				}
				
			}
		};
		runThread.start();
		
	}
	
	public void tickle()
	{
		Log.d("MPowerLog", "Hee Hee!");
		// Guard against over-using the sync medium
		if (lastSyncTime-System.currentTimeMillis()>5000)
			runThread.interrupt();
	}
	
	/**
	 * Generates a synchronization event that can be observed both in the hardware- and software event trace.
	 */
	public abstract void sync();

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

}
