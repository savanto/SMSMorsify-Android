package com.savanto.android.smsmorsify;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;

/**
 * @author savanto
 *
 */
public class MorseService extends IntentService
{
	private static final long DEFAULT_DELAY	= 5;
	private static final int DEFAULT_REPEAT	= -1;


	/**
	 * BroadcastReceiver to listen for screen being turned on.
	 */
	private ScreenOnReceiver screenOnReceiver;

	public MorseService()
	{
		super("MorseService");
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		this.screenOnReceiver = new ScreenOnReceiver();
		// Register the screenOnReceiver
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		this.registerReceiver(this.screenOnReceiver, intentFilter);
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent)
	{
		// Get SharedPreferences
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		// Do nothing if SMSMorsify is set to off.
		if (! sharedPrefs.getBoolean(this.getString(R.string.pref_key_enabled), false))
			return;

		// Check if user has enabled the screen-on check
		if (sharedPrefs.getBoolean(this.getString(R.string.pref_key_screen_check), true))
		{
			// Do nothing if phone screen is on.
			if (((PowerManager) this.getSystemService(Context.POWER_SERVICE)).isScreenOn())
				return;
		}

		// Do nothing if phone is in silent mode
		if (((AudioManager) this.getSystemService(Context.AUDIO_SERVICE)).getRingerMode() == AudioManager.RINGER_MODE_SILENT)
			return;

		// Retrieve pattern settings from shared preferences.
		final long WAIT_MS			= MorseCode.DEFAULT_WAIT;
		final long DELAY_SECONDS	= sharedPrefs.getLong(this.getString(R.string.pref_key_delay), MorseService.DEFAULT_DELAY);
		final long DIT_MS			= sharedPrefs.getLong(this.getString(R.string.pref_key_dit), MorseCode.DEFAULT_DIT);
		final long DAH_MS			= DIT_MS * 3;
		final long GAP_MS			= DIT_MS;
		final int SHORT_GAP			= (int) sharedPrefs.getLong(this.getString(R.string.pref_key_short_gap), MorseCode.DEFAULT_SHORT_GAP);
		final int MEDIUM_GAP		= (int) sharedPrefs.getLong(this.getString(R.string.pref_key_medium_gap), MorseCode.DEFAULT_MEDIUM_GAP);
		final boolean USE_NUMBERS	= sharedPrefs.getBoolean(this.getString(R.string.pref_key_use_numbers), true);
		final boolean USE_SYMBOLS	= sharedPrefs.getBoolean(this.getString(R.string.pref_key_use_symbols), true);
		final boolean USE_PROSIGNS	= sharedPrefs.getBoolean(this.getString(R.string.pref_key_use_prosigns), true);

		// Create Morse code from message and preferences
		MorseCode morseCode = new MorseCode(intent.getStringExtra(MorseService.MESSAGE_KEY),
				SHORT_GAP, MEDIUM_GAP, USE_NUMBERS, USE_SYMBOLS, USE_PROSIGNS);

		// Delay Morse transmission to allow Notification sound/vibration to finish.
		SystemClock.sleep(DELAY_SECONDS * 1000);

		// Vibrate the message pattern
		((Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE))
			.vibrate(morseCode.toPattern(WAIT_MS, DIT_MS, DAH_MS, GAP_MS), MorseService.DEFAULT_REPEAT);
	}

	@Override
	public void onDestroy()
	{
		// Unregister the screenOnReceiver
		this.unregisterReceiver(this.screenOnReceiver);
	}

	public static final String MESSAGE_KEY = "message";
}
