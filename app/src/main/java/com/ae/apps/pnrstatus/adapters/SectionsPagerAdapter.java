/*
 * MIT License
 *
 * Copyright (c) 2019 Midhun Harikumar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ae.apps.pnrstatus.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ae.apps.pnrstatus.fragments.AboutFragment;
import com.ae.apps.pnrstatus.fragments.PnrStatusFragment;
import com.ae.apps.pnrstatus.v3.R;

import java.util.Locale;

/**
 * A FragmentPagerAdapter that returns a fragment corresponding to one of the primary sections of the app.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private final Context	context;
	// Removing MessagesFragment
	// private final Fragment	mMessagesFragment;
	private final Fragment	mPnrStatusFragment;
	private final Fragment	mAboutFragment;

	public SectionsPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		this.context = context;
		mPnrStatusFragment = new PnrStatusFragment();
		mAboutFragment = new AboutFragment();
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment;
		if (i == 0) {
			fragment = mPnrStatusFragment;
		} else if (i == 1) {
			fragment = mAboutFragment;
		} else {
			fragment = mAboutFragment;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return context.getString(R.string.title_section2).toUpperCase(Locale.getDefault());
		case 1:
			return context.getString(R.string.title_section3).toUpperCase(Locale.getDefault());

		}
		return null;
	}

}