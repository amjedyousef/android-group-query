package org.tudelft.neat.eventsources;

import org.tudelft.neat.MPowerApplication;
import org.tudelft.neat.OutputLog;
import org.tudelft.neat.SyncEventSource;

import android.content.Context;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;

public class VibratorSyncEventSource extends SyncEventSource
{
	
	public VibratorSyncEventSource(MPowerApplication application)
	{
		super(application);
	}

	private Vibrator vibrator;

	@Override
	public void sync()
	{
		Log.d("MPowerLog", "Vibrator_Sync " + SystemClock.uptimeMillis() + " " + System.currentTimeMillis());
		vibrator.vibrate(Math.round(Math.random()*50 + 50));
		Log.d("MPowerLog", "Vibrator_Sync_Post " + SystemClock.uptimeMillis() + " " + System.currentTimeMillis());
	}
	
	@Override
	public void start(OutputLog log)
	{
		super.start(log);
		vibrator = (Vibrator)getApplication().getSystemService(Context.VIBRATOR_SERVICE);
	}
	
}
