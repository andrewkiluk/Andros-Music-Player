package com.andrewkiluk.machmusicplayer.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.andrewkiluk.machmusicplayer.R;
import com.andrewkiluk.machmusicplayer.models.PlayerStatus;

public class SettingsActivity extends PreferenceActivity { 
	//implements OnPreferenceClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PlayerStatus.isVisible = true;
		getFragmentManager().beginTransaction().replace(android.R.id.content,
				new SettingsFragment()).commit();

	}

	@Override
	protected void onStop() {
		PlayerStatus.isVisible = false;
		super.onStop();
	}


	public static class SettingsFragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.layout.settings);
		}
	}
}