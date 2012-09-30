package com.ae.apps.pnrstatus.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ae.apps.pnrstatus.utils.Utils;
import com.ae.apps.pnrstatus.v3.R;

/**
 * Fragment for the About application page
 * 
 * @author midhun_harikumar
 * 
 */
public class AboutFragment extends Fragment implements OnClickListener {

	private View		layout;
	private Activity	activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.about_view, null);
		activity = getActivity();
		
		// Listen for clicks on the layout
		View fbLinkLayout = (View) layout.findViewById(R.id.fb_link_layout);
		fbLinkLayout.setOnClickListener(this);

		return layout;
	}

	@Override
	public void onClick(View v) {
		// Get the fb link and launch the web page 
		String url = activity.getString(R.string.fb_page_link);
		Utils.launchWebPage(activity, url);
	}
}
