package org.tudelft.neat.eventsources;

import org.tudelft.neat.EventSource;
import org.tudelft.neat.MPowerApplication;
import org.tudelft.neat.OutputLog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

public class BatteryLevelEventSource extends EventSource
{

	public BatteryLevelEventSource(MPowerApplication application)
	{
		super(application);
	}
	
	@Override
	public void start(OutputLog log)
	{
		IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		getApplication().registerReceiver(batteryLevelReceiver, batteryLevelFilter);
	}

	@Override
	public void stop()
	{
		getApplication().unregisterReceiver(batteryLevelReceiver);
	}

	private BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver()
	{
		
		private String getPluggedStatus(int plugged)
		{
			if (plugged==BatteryManager.BATTERY_PLUGGED_AC) return "ac";
			if (plugged==BatteryManager.BATTERY_PLUGGED_USB) return "usb";
			return "battery";
		}
		
		private String getHealth(int health)
		{
			if (health==BatteryManager.BATTERY_HEALTH_DEAD) return "dead";
			if (health==BatteryManager.BATTERY_HEALTH_GOOD) return "good";
			if (health==BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) return "over voltage";
			if (health==BatteryManager.BATTERY_HEALTH_OVERHEAT) return "overheat";
			if (health==BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) return "unspecified failure";
			return "unknown";
		}
		
		private String getStatus(int status)
		{
			if (status==BatteryManager.BATTERY_STATUS_CHARGING) return "charging";
			if (status==BatteryManager.BATTERY_STATUS_DISCHARGING) return "discharging";
			if (status==BatteryManager.BATTERY_STATUS_FULL) return "full";
			if (status==BatteryManager.BATTERY_STATUS_NOT_CHARGING) return "no charging";
			return "unknown";
		}
		
		public void onReceive(Context context, Intent intent)
		{
			
			double normalised = 0;
			int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			if (level >= 0 && scale != 0)
				normalised = level / (double)scale;

			Log.d("MPowerLog", String.format("Battery Status health:%s plugged:%s present:%b scale:%d status:%s technology:%s temperature:%d voltage:%d level:%d percent:%f",
					getHealth(intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)),
					getPluggedStatus(intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)),
					intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, true),
					intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1),
					getStatus(intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)),
					intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY),
					intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1),
					intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1),
					intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1),
					normalised * 100
					));
		}
	};

}
