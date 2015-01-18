package org.tudelft.neat.eventsources;

import org.tudelft.neat.EventSource;
import org.tudelft.neat.MPowerApplication;
import org.tudelft.neat.OutputLog;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class TelephonyManagerEventSource extends EventSource
{

	public TelephonyManagerEventSource(MPowerApplication application)
	{
		super(application);
	}
	
	@Override
	public void start(OutputLog log)
	{
		TelephonyManager manager = (TelephonyManager)getApplication().getSystemService(Context.TELEPHONY_SERVICE);
		manager.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}

	@Override
	public void stop()
	{
		TelephonyManager manager = (TelephonyManager)getApplication().getSystemService(Context.TELEPHONY_SERVICE);
		manager.listen(listener, PhoneStateListener.LISTEN_NONE);
	}
	
	private PhoneStateListener listener = new PhoneStateListener()
	{
		public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength)
		{
			Log.d("MPowerLog", String.format("Telephony Signal Strength CdmaDbm:%d CdmaEcio:%d EvdoDbm:%d EvdoEcio:%d EvdoSnr:%d GsmBitErrorRate:%d GsmSignalStrength:%d",
					signalStrength.getCdmaDbm(),
					signalStrength.getCdmaEcio(),
					signalStrength.getEvdoDbm(),
					signalStrength.getEvdoEcio(),
					signalStrength.getEvdoSnr(),
					signalStrength.getGsmBitErrorRate(),
					signalStrength.getGsmSignalStrength()
					));
		};
	};

}
