package org.tudelft.neat;

import org.tudelft.mpower.R;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MPowerService extends Service
{

	// Binder so Activities can bind this service.
	private final MPowerServiceBinder binder = new MPowerServiceBinder(this);

	public void start()
	{
		MPowerApplication application = (MPowerApplication)getApplication();
		
		if (application.isActive())
			return;

		// Signal that we're now active
		application.start();
	}

	public void stop()
	{
		MPowerApplication application = (MPowerApplication)getApplication();

		if (!application.isActive())
			return;

		// Stop the selected task
		application.stop();
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// The intent to launch when the user clicks the expanded notification
		Intent launchIntent = new Intent(this, MPowerActivity.class);
		// launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);

		// This constructor is deprecated. Use Notification.Builder instead
		Notification.Builder builder = new Builder(this);
	    Notification not = new Notification(R.drawable.ic_launcher, "M-Power Running", System.currentTimeMillis());
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MPowerActivity.class), Notification.FLAG_ONGOING_EVENT);
	    not.flags = Notification.FLAG_ONGOING_EVENT;
	    not.setLatestEventInfo(this, "M-Power Logger", "M-Power's logging application", contentIntent);

		// Run in the foreground
		startForeground(1234, not);

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return binder;
	}

}
