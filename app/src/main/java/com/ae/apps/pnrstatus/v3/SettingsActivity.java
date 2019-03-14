/*
 * Copyright 2013 Midhun Harikumar
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ae.apps.pnrstatus.v3;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

/**
 * Settings Activity
 * 
 * @author midhun_harikumar
 * 
 */
public class SettingsActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		// Get the current prefernce for service
		final ListPreference servicePreference = (ListPreference) findPreference("pref_service");
		if (servicePreference.getValue() == null) {
			servicePreference.setValueIndex(0);
		}
		// Set the selected value as the summary for this item
		servicePreference.setSummary(servicePreference.getEntry().toString());
		servicePreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// Set the value as the new value
				servicePreference.setValue(newValue.toString());
				// Get the entry which corresponds to the current value and set as summary
				preference.setSummary(servicePreference.getEntry());
				return false;
			}
		});
	}
}
