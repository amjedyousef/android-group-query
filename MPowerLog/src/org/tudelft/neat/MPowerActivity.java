package org.tudelft.neat;

import org.tudelft.mpower.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class MPowerActivity extends Activity
{
	
	private MPowerService service;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mpower);
		
		// Start the service, if it is not already running.
		Intent intent = new Intent(this, MPowerService.class);
		startService(intent);

		// Bind it and get a reference.
		bindService(new Intent(MPowerActivity.this, MPowerService.class), serviceConnection, Context.BIND_AUTO_CREATE);
		
		updateStatus();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		updateStatus();
	}
	
	private MPowerApplication getMPowerApplication()
	{
		return (MPowerApplication)this.getApplication();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		// Unbind the service.
		unbindService(serviceConnection);
	}

	private ServiceConnection serviceConnection = new ServiceConnection()
	{

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder)
		{
			service = ((MPowerServiceBinder)binder).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			service = null;
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mpower, menu);
		return true;
	}
	
	private Button getButton(int id)
	{
		return (Button)findViewById(id);
	}

	private ToggleButton getToggleButton(int id)
	{
		return (ToggleButton)findViewById(id);
	}
	
	private void updateStatus()
	{
		MPowerApplication application = getMPowerApplication();
		
		if (application.isActive())
		{
			getButton(R.id.startButton).setEnabled(false);
			getButton(R.id.stopButton).setEnabled(true);
			
			getToggleButton(R.id.kernelEventToggle).setEnabled(false);
			getToggleButton(R.id.logCatToggle).setEnabled(false);
			getToggleButton(R.id.logCatMainToggle).setEnabled(false);
			getToggleButton(R.id.logCatSystemToggle).setEnabled(false);
			getToggleButton(R.id.logCatRadioToggle).setEnabled(false);
			getToggleButton(R.id.logCatEventsToggle).setEnabled(false);
			getToggleButton(R.id.vibratorSyncToggle).setEnabled(false);
		} else
		{
			getButton(R.id.startButton).setEnabled(true);
			getButton(R.id.stopButton).setEnabled(false);

			getToggleButton(R.id.kernelEventToggle).setEnabled(true);
			getToggleButton(R.id.logCatToggle).setEnabled(true);
			getToggleButton(R.id.logCatMainToggle).setEnabled(true);
			getToggleButton(R.id.logCatSystemToggle).setEnabled(true);
			getToggleButton(R.id.logCatRadioToggle).setEnabled(true);
			getToggleButton(R.id.logCatEventsToggle).setEnabled(true);
			getToggleButton(R.id.vibratorSyncToggle).setEnabled(true);
		}
		
		getToggleButton(R.id.kernelEventToggle).setChecked(application.isKernelEventsEnabled());
		getToggleButton(R.id.logCatToggle).setChecked(application.isLogCatEnabled());

		getToggleButton(R.id.logCatMainToggle).setChecked(application.isMainBufferEnabled());
		getToggleButton(R.id.logCatSystemToggle).setChecked(application.isSystemBufferEnabled());
		getToggleButton(R.id.logCatRadioToggle).setChecked(application.isRadioBufferEnabled());
		getToggleButton(R.id.logCatEventsToggle).setChecked(application.isEventBufferEnabled());
		
		getToggleButton(R.id.vibratorSyncToggle).setChecked(application.isHardwareSyncEnabled());
		
	}
	
	public void onStartButton(View view)
	{
		if (service!=null)
			service.start();
		else
			Log.e("MPOWER", "Service not bound");

		updateStatus();
	}

	public void onStopButton(View view)
	{
		if (service!=null)
			service.stop();
		else
			Log.e("MPOWER", "Service not bound");

		updateStatus();
	}

	public void onKernelEventToggle(View view)
	{
		getMPowerApplication().setKernelEventsEnabled(((ToggleButton)view).isChecked());
	}

	public void onLogCatToggle(View view)
	{
		getMPowerApplication().setLogCatEnabled(((ToggleButton)view).isChecked());
	}

	public void onMainToggle(View view)
	{
		getMPowerApplication().setMainBufferEnabled(((ToggleButton)view).isChecked());
	}

	public void onSystemToggle(View view)
	{
		getMPowerApplication().setSystemBufferEnabled(((ToggleButton)view).isChecked());
	}

	public void onRadioToggle(View view)
	{
		getMPowerApplication().setRadioBufferEnabled(((ToggleButton)view).isChecked());
	}

	public void onEventsToggle(View view)
	{
		getMPowerApplication().setEventBufferEnabled(((ToggleButton)view).isChecked());
	}

	public void onVibratorSyncToggle(View view)
	{
		getMPowerApplication().setHardwareSyncEnabled(((ToggleButton)view).isChecked());
	}

}
