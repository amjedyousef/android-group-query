package org.tudelft.neat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		MPowerApplication application = (MPowerApplication)context.getApplicationContext();
		application.periodicTimeOut();
	}

	public static void enablePeriodicWakeup(Context context, long interval)
	{
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Get the AlarmManager service
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
		// Set the alarm
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, interval, interval, sender);
	}

	public static void disablePeriodicWakeup(Context context)
	{
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		// Get the AlarmManager service
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
		// Set the alarm
		alarmManager.cancel(sender);
	}

}
