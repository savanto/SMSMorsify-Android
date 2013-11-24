package com.savanto.android.smsmorsify;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	private SeekBarPreference mDelayPreference;
	private SeekBarPreference mDitPreference;
	private SeekBarPreference mShortGapPreference;
	private SeekBarPreference mMediumGapPreference;

	private SharedPreferences mSharedPreferences;

	/**
	 * Determines whether to always show the simplified settings UI, where
	 * settings are presented in a single list. When false, settings are shown
	 * as a master/detail two-pane view on tablets. When true, a single pane is
	 * shown on tablets.
	 */
	private static final boolean	ALWAYS_SIMPLE_PREFS	= false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Shows the simplified settings UI if the device configuration if the device
		// configuration dictates that a simplified, single-pane UI should be shown.
		if (SettingsPreferenceActivity.isSimplePreferences(this))
		{
			// In the simplified UI, fragments are not used at all and we instead
			// use the older PreferenceActivity APIs.
	
			// Add 'enable' preferences from xml.
			this.addPreferencesFromResource(R.xml.pref_enable);
			PreferenceManager.setDefaultValues(this, R.xml.pref_enable, false);  
	
			// Add 'general' preferences, and a corresponding header from xml.
			PreferenceCategory fakeHeader = new PreferenceCategory(this);
			fakeHeader.setTitle(R.string.pref_header_general);
			this.getPreferenceScreen().addPreference(fakeHeader);
			this.addPreferencesFromResource(R.xml.pref_general);
			PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);  
	
			// Add 'Morse code' preferences, and a corresponding header, from xml.
			fakeHeader = new PreferenceCategory(this);
			fakeHeader.setTitle(R.string.pref_header_morse);
			this.getPreferenceScreen().addPreference(fakeHeader);
			this.addPreferencesFromResource(R.xml.pref_morse);
			PreferenceManager.setDefaultValues(this, R.xml.pref_morse, false);

			// Get references to the preferences.
			this.mDelayPreference = (SeekBarPreference) this.getPreferenceScreen().findPreference(this.getText(R.string.pref_key_delay));
			this.mDitPreference = (SeekBarPreference) this.getPreferenceScreen().findPreference(this.getText(R.string.pref_key_dit));
			this.mShortGapPreference = (SeekBarPreference) this.getPreferenceScreen().findPreference(this.getText(R.string.pref_key_short_gap));
			this.mMediumGapPreference = (SeekBarPreference) this.getPreferenceScreen().findPreference(this.getText(R.string.pref_key_medium_gap));

			// Get the shared preferences
			this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		// Setup initial dynamic summary values of EditText/List/Dialog/Ringtone preferences.
		this.mDelayPreference.setSummary(Long.toString(this.mSharedPreferences.getLong(this.mDelayPreference.getKey(),
				this.mDelayPreference.getDefaultValue())) + this.mDelayPreference.getUnits());
		this.mDitPreference.setSummary(Long.toString(this.mSharedPreferences.getLong(this.mDitPreference.getKey(),
				this.mDitPreference.getDefaultValue())) + this.mDitPreference.getUnits());
		this.mShortGapPreference.setSummary(Long.toString(this.mSharedPreferences.getLong(this.mShortGapPreference.getKey(),
				this.mShortGapPreference.getDefaultValue())) + this.mShortGapPreference.getUnits());
		this.mMediumGapPreference.setSummary(Long.toString(this.mSharedPreferences.getLong(this.mMediumGapPreference.getKey(),
				this.mMediumGapPreference.getDefaultValue())) + this.mMediumGapPreference.getUnits());


		// Register the listener to watch for the preferences value changes.
		// When their values change, their summaries are updated
		// to reflect the new value, per the Android Design guidelines.
		this.mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		// Unregister shared preferences change listener
		this.mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
	}

	/** {@inheritDoc} */
	@Override
	public boolean onIsMultiPane()
	{
		return SettingsPreferenceActivity.isXLargeTablet(this) && !SettingsPreferenceActivity.isSimplePreferences(this);
	}

	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isXLargeTablet(Context context)
	{
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	/**
	 * Determines whether the simplified settings UI should be shown. This is
	 * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
	 * doesn't have newer APIs like {@link PreferenceFragment}, or the device
	 * doesn't have an extra-large screen. In these cases, a single-pane
	 * "simplified" settings UI should be shown.
	 */
	private static boolean isSimplePreferences(Context context)
	{
		return SettingsPreferenceActivity.ALWAYS_SIMPLE_PREFS
				|| Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
				|| !SettingsPreferenceActivity.isXLargeTablet(context);
	}

	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		Preference preference = this.findPreference(key);

		if (preference instanceof SeekBarPreference)
		{
			SeekBarPreference seekBarPreference = (SeekBarPreference) preference;
			preference.setSummary(Integer.toString(seekBarPreference.getProgress()) + seekBarPreference.getUnits());
		}
	}

	/*********************************************************************************
	 * Fragment-based UI construction, for Honeycomb/tablets
	 */

	/** {@inheritDoc} */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target)
	{
		if (!SettingsPreferenceActivity.isSimplePreferences(this))
			this.loadHeadersFromResource(R.xml.pref_headers, target);
	}

	/**
	 * This fragment shows enable SMSMorsify preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class EnablePreferenceFragment extends PreferenceFragment
	{
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			this.addPreferencesFromResource(R.xml.pref_enable);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
//			SettingsPreferenceActivity.bindPreferenceSummaryToValue(this.findPreference("sync_frequency"));
		}
	}

	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class GeneralPreferenceFragment extends PreferenceFragment
	{
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			this.addPreferencesFromResource(R.xml.pref_general);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
//			SettingsPreferenceActivity.bindPreferenceSummaryToValue(this.findPreference("example_text"));
//			SettingsPreferenceActivity.bindPreferenceSummaryToValue(this.findPreference("example_list"));
		}
	}

	/**
	 * This fragment shows Morse code preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class MorsePreferenceFragment extends PreferenceFragment
	{
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			this.addPreferencesFromResource(R.xml.pref_morse);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
//			SettingsPreferenceActivity.bindPreferenceSummaryToValue(this.findPreference("notifications_new_message_ringtone"));
		}
	}
}
