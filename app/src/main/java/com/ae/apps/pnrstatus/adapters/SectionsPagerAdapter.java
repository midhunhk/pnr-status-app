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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.ae.apps.pnrstatus.R;
import com.ae.apps.pnrstatus.fragments.AboutFragment;
import com.ae.apps.pnrstatus.fragments.PnrStatusFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A FragmentPagerAdapter that returns a fragment corresponding to one of the primary sections of the app.
 */
public class SectionsPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {

	private final List<Fragment> fragments = new ArrayList<>();
	private final List<String> fragmentTitles = new ArrayList<>();

	public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
		super(fragmentActivity);

		fragments.add(new PnrStatusFragment());
		fragmentTitles.add(fragmentActivity.getString(R.string.title_section2).toUpperCase(Locale.getDefault()));

		fragments.add(new AboutFragment());
		fragmentTitles.add(fragmentActivity.getString(R.string.title_section3).toUpperCase(Locale.getDefault()));
	}

	@NonNull
	@Override
	public Fragment createFragment(int position) {
		return fragments.get(position);
	}

	public CharSequence getPageTitle(int position) {
		return fragmentTitles.get(position);
	}

	@Override
	public int getItemCount() {
		return fragments.size();
	}
}