package org.tudelft.neat;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.tudelft.neat.eventsources.BatteryLevelEventSource;
import org.tudelft.neat.eventsources.CombinedEventSource;
import org.tudelft.neat.eventsources.CpuFrequency;
import org.tudelft.neat.eventsources.NMEAEventSource;
import org.tudelft.neat.eventsources.TelephonyManagerEventSource;
import org.tudelft.neat.eventsources.VibratorSyncEventSource;

import android.app.Application;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class MPowerApplication extends Application
{

    private String outputFilePattern = "mpowerlog-%s.log";
	
	private boolean active;
	private OutputLog output;
	private List<EventSource> sources = new ArrayList<EventSource>();
	
	private boolean logCatEnabled = true, kernelEventsEnabled = true, hardwareSyncEnabled = true;
	private boolean mainBufferEnabled = true, systemBufferEnabled = true, radioBufferEnabled = true, eventBufferEnabled = true;
	
	private CombinedEventSource combinedEventSource = new CombinedEventSource(this);
//	private KernelEventSource kernelEventSource = new KernelEventSource(this);
//	private LogCatEventSource logCatSource = new LogCatEventSource(this);
	private VibratorSyncEventSource vibratorSyncEventSource = new VibratorSyncEventSource(this);
	
	public MPowerApplication()
	{
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	public void start()
	{
		// Stop periodic wake-up
		AlarmReceiver.disablePeriodicWakeup(this);
		
		// Gather sources
		sources.clear();
//		if (kernelEventsEnabled) sources.add(kernelEventSource);
//		if (logCatEnabled) sources.add(logCatSource);
		if (hardwareSyncEnabled) sources.add(vibratorSyncEventSource);
		sources.add(combinedEventSource);
		sources.add(new TelephonyManagerEventSource(this));
		sources.add(new NMEAEventSource(this));
		sources.add(new BatteryLevelEventSource(this));
		sources.add(new CpuFrequency(this, 60000L));
		
		// Generate file name
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = String.format(outputFilePattern, formatter.format(new java.util.Date()));
		output = new OutputLog(new File(Environment.getExternalStorageDirectory(), fileName));
		Log.d("PATH_PATH" , Environment.getExternalStorageDirectory().getPath().toString() );
		Toast.makeText(this, Environment.getExternalStorageDirectory().getPath().toString(), Toast.LENGTH_LONG).show();
		
		try
		{
			output.open();
		} catch (FileNotFoundException e)
		{
			// TODO handle
			e.printStackTrace();
		}
		
		// Log start event
		output.append("SYSTEM", String.format("Log started on %s at %s, millis=%d, uptime=%d",
				new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date()),
				new SimpleDateFormat("HH-mm").format(new java.util.Date()),
				System.currentTimeMillis(),
				SystemClock.uptimeMillis()
				));
		
		// Start sources
		for (EventSource source : sources)
			source.start(output);
		
		// Enable periodic wake-up (to make unkillable)
		AlarmReceiver.enablePeriodicWakeup(this, 5000);
		
		this.active = true;
	}
	
	public void stop()
	{
		// Stop periodic wake-up
		AlarmReceiver.disablePeriodicWakeup(this);
		
		// Stop sources
		for (EventSource source : sources)
			source.stop();
				
		// Close output log
		output.close();
		output = null;
				
		this.active = false;
	}
	
	public void periodicTimeOut()
	{
		if (!isActive())
		{
			start();
			
			// Append a message indicating a restart
			Log.d("MPowerLog", "Logger crashed, restarted");
		}
	}
	
	public void tickle()
	{
		if (active && sources.contains(vibratorSyncEventSource))
			vibratorSyncEventSource.tickle();
	}
	
	public void setHardwareSyncEnabled(boolean hardwareSyncEnabled)
	{
		this.hardwareSyncEnabled = hardwareSyncEnabled;
	}

	public boolean isHardwareSyncEnabled()
	{
		return hardwareSyncEnabled;
	}
	
	public void setKernelEventsEnabled(boolean kernelEventsEnabled)
	{
		this.kernelEventsEnabled = kernelEventsEnabled;
	}
	
	public boolean isKernelEventsEnabled()
	{
		return kernelEventsEnabled;
	}
	
	public void setLogCatEnabled(boolean logCatEnabled)
	{
		this.logCatEnabled = logCatEnabled;
	}
	
	public boolean isLogCatEnabled()
	{
		return logCatEnabled;
	}
	
	public void setMainBufferEnabled(boolean mainBufferEnabled)
	{
		this.mainBufferEnabled = mainBufferEnabled;
	}
	
	public boolean isMainBufferEnabled()
	{
		return mainBufferEnabled;
	}
	
	public void setSystemBufferEnabled(boolean systemBufferEnabled)
	{
		this.systemBufferEnabled = systemBufferEnabled;
	}
	
	public boolean isSystemBufferEnabled()
	{
		return systemBufferEnabled;
	}
	
	public void setRadioBufferEnabled(boolean radioBufferEnabled)
	{
		this.radioBufferEnabled = radioBufferEnabled;
	}
	
	public boolean isRadioBufferEnabled()
	{
		return radioBufferEnabled;
	}
	
	public void setEventBufferEnabled(boolean eventBufferEnabled)
	{
		this.eventBufferEnabled = eventBufferEnabled;
	}
	
	public boolean isEventBufferEnabled()
	{
		return eventBufferEnabled;
	}
	
}
