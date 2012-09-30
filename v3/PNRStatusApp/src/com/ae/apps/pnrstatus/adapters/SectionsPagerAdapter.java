package com.ae.apps.pnrstatus.adapters;

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
			/*
			 * fragment = new DummySectionFragment();
			 * 
			 * Bundle args = new Bundle(); args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
			 * fragment.setArguments(args);
			 */
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
			return context.getString(R.string.title_section1).toUpperCase();
		case 1:
			return context.getString(R.string.title_section2).toUpperCase();
		case 2:
			return context.getString(R.string.title_section3).toUpperCase();
		}
		return null;
	}

}