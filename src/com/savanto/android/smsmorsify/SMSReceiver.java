/**
 * 
 */
package com.savanto.android.smsmorsify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * @author savanto
 *
 */
public class SMSReceiver extends BroadcastReceiver
{
	private static final String SMS_BUNDLE_KEY = "pdus";

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		// Get intent bundle
		Bundle extras = intent.getExtras();

		// Do nothing if extras Bundle is empty
		if (extras != null)
		{
			// Otherwise, proceed.
	
			// Retrieve the received message
			Object[] pdus = (Object[]) extras.get(SMSReceiver.SMS_BUNDLE_KEY);
	
			// String together message from pdus
			String message = "";
			for (int i = 0; i < pdus.length; i++)
				message += SmsMessage.createFromPdu((byte[]) pdus[i]).getMessageBody();
	
			// Create Intent for launching the MorseService, which will process the message.
			Intent morseServiceIntent = new Intent(context, MorseService.class);
			morseServiceIntent.putExtra(MorseService.MESSAGE_KEY, message);
	
			// Enqueue processing of this message with MorseService.
			context.startService(morseServiceIntent);
		}
	}
}
