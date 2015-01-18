package org.tudelft.neat.eventsources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.tudelft.neat.MPowerApplication;
import org.tudelft.neat.PeriodicEventSource;

import android.util.Log;

/**
 * 
 * Event source that periodically logs the time spent in various CPU
 * frequencies. 
 * 
 * @author Niels Brouwers
 *
 */
public class CpuFrequency extends PeriodicEventSource
{

	public CpuFrequency(MPowerApplication application, long interval)
	{
		super(application, interval);
	}

	@Override
	public void run()
	{
		BufferedReader reader;
		try
		{
			reader = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state"));
			String line;
			
			StringBuffer buf = new StringBuffer();
			buf.append("cpufreq-stats [");
			boolean first = true;
			while ((line=reader.readLine())!=null)
			{
				if (!first)
					buf.append(",");
				
				String[] parts = line.split(" ");
				buf.append("[").append(parts[0]).append(",").append(parts[1]).append("]");
				
				first = false;
			}
			buf.append("]");
			Log.d("MPowerLog", buf.toString());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
