package org.tudelft.neat;

import android.os.Binder;

public class MPowerServiceBinder extends Binder
{

	private final MPowerService service;
	
	public MPowerServiceBinder(MPowerService service)
	{
		this.service = service;
	}
	
	public MPowerService getService()
	{
		return service;
	}

}
