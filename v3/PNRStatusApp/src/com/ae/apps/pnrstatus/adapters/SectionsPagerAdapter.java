/*
 * Copyright 2012 Midhun Harikumar
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

package com.ae.apps.pnrstatus.adapters;

import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ae.apps.pnrstatus.fragments.AboutFragment;
import com.ae.apps.pnrstatus.fragments.MessagesFragment;
import com.ae.apps.pnrstatus.fragments.PnrStatusFragment;
import com.ae.apps.pnrstatus.v3.R;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary sections of the app.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private Context		context;

	public SectionsPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = null;
		if (i == 0) {
			fragment = new MessagesFragment();
		} else if (i == 1) {
			fragment = new PnrStatusFragment();
		} else if (i == 2) {
			fragment = new AboutFragment();
		} else {
			fragment = new AboutFragment();
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return context.getString(R.string.title_section1).toUpperCase(Locale.getDefault());
		case 1:
			return context.getString(R.string.title_section2).toUpperCase(Locale.getDefault());
		case 2:
			return context.getString(R.string.title_section3).toUpperCase(Locale.getDefault());
		}
		return null;
	}

}