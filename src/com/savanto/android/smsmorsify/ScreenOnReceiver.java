package com.savanto.android.smsmorsify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

/**
 * @author savanto
 *
 */
public class ScreenOnReceiver extends BroadcastReceiver
{
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		// Cancel the vibrator.
		((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).cancel();
		
		// Stop MorseService
		context.stopService(new Intent(context, MorseService.class));
	}
}
