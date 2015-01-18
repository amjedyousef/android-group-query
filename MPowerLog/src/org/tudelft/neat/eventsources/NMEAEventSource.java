package org.tudelft.neat.eventsources;

import org.tudelft.neat.EventSource;
import org.tudelft.neat.MPowerApplication;
import org.tudelft.neat.OutputLog;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.NmeaListener;
import android.location.LocationManager;
import android.util.Log;

public class NMEAEventSource extends EventSource
{

	public NMEAEventSource(MPowerApplication application)
	{
		super(application);
	}

	@Override
	public void start(OutputLog log)
	{
		getLocationManager().addGpsStatusListener(gpsStatusListener);
		getLocationManager().addNmeaListener(nmeaListener);
	}

	@Override
	public void stop()
	{
		getLocationManager().removeGpsStatusListener(gpsStatusListener);
		getLocationManager().removeNmeaListener(nmeaListener);
	}
	
	private LocationManager getLocationManager()
	{
		return (LocationManager)getApplication().getSystemService(Context.LOCATION_SERVICE);
	}
	
	GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener()
	{
		@Override
		public void onGpsStatusChanged(int event)
		{
			GpsStatus status = getLocationManager().getGpsStatus(null);
			
			String statusString = "UNKNOWN";
			switch (event)
			{
			case GpsStatus.GPS_EVENT_STARTED:
				statusString = "GPS_EVENT_STARTED";
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				statusString = "GPS_EVENT_STOPPED";
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				statusString = "GPS_EVENT_SATELLITE_STATUS";
				break;
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				statusString = "GPS_EVENT_FIRST_FI";
				break;
			}
			
			String satString = "";
			for (GpsSatellite sat : status.getSatellites())
				satString += String.format("<azimuth:%f elevation:%f prn:%d snr:%f hasAlmanac:%s hasEphemeris:%s usedInFix:%s>",
						sat.getAzimuth(),
						sat.getElevation(),
						sat.getPrn(),
						sat.getSnr(),
						sat.hasAlmanac()?"true":"false",
						sat.hasEphemeris()?"true":"false",
						sat.usedInFix()?"true":"false"
						);

			Log.d("MPowerLog", String.format("GPS Status status:%s timeToFirstFix:%d satellites:%s maxSatellites:%d",
					statusString,
					status.getTimeToFirstFix(),
					satString,
					status.getMaxSatellites()
					));
		}
	};
	
	NmeaListener nmeaListener = new NmeaListener()
	{
		@Override
		public void onNmeaReceived(long timestamp, String nmea)
		{
			Log.d("MPowerLog", String.format("NMEA time:%d sentence:%s", timestamp, nmea ));
		}
	};

}
