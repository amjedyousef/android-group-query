package org.tudelft.neat;

public abstract class EventSource
{
	
	private final MPowerApplication application;
	
	public EventSource(MPowerApplication application)
	{
		this.application = application;
	}
	
	public MPowerApplication getApplication()
	{
		return application;
	}
	
	public abstract void start(OutputLog log);
	public abstract void stop();

}
