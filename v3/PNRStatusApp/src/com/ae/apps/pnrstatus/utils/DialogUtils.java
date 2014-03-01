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

package com.ae.apps.pnrstatus.utils;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ae.apps.pnrstatus.adapters.PassengerAdapter;
import com.ae.apps.pnrstatus.v3.R;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

/**
 * Utility class for managing Dialogs for the application
 * 
 * @author Midhun
 * 
 */
public class DialogUtils {

	/**
	 * Displays the PNRStatus Information as a dialog
	 * 
	 * @param context
	 * @param pnrStatusVo
	 */
	public static void showPNRStatusInfo(final Context context, final PNRStatusVo pnrStatusVo) {
		View inflatedView = LayoutInflater.from(context).inflate(R.layout.ticket_info, null);

		// Set the data from the dataObject
		TextView pnrNumber = (TextView) inflatedView.findViewById(R.id.ticket_pnrnumber);
		pnrNumber.setText(PNRUtils.formatPNRString(pnrStatusVo.getPnrNumber()));

		// Get reference to the textviews
		TextView line1 = (TextView) inflatedView.findViewById(R.id.ticket_info_line1);
		TextView line2 = (TextView) inflatedView.findViewById(R.id.ticket_info_line2);
		TextView line3 = (TextView) inflatedView.findViewById(R.id.ticket_info_line3);
		TextView line4 = (TextView) inflatedView.findViewById(R.id.ticket_info_line4);
		TextView line5 = (TextView) inflatedView.findViewById(R.id.ticket_info_line5);

		// Get the localized strings for the corresponding texts
		Resources resources = context.getResources();
		String line1Text = resources.getString(R.string.str_train_name_no, pnrStatusVo.getTrainName(),
				pnrStatusVo.getTrainNo());
		String line2Text = resources.getString(R.string.str_journey_date, pnrStatusVo.getTrainJourney());
		String line3Text = resources.getString(R.string.str_current_status, pnrStatusVo.getCurrentStatus());
		String line4Text = resources.getString(R.string.str_journey_from_to, pnrStatusVo.getBoardingPoint(),
				pnrStatusVo.getEmbarkPoint());

		// Finally set the text to the textviews
		line1.setText(line1Text);
		line2.setText(line2Text);
		line3.setText(line3Text);
		line4.setText(line4Text);
		line5.setText(pnrStatusVo.getChartStatus());

		// Handle the Share button click
		ImageButton shareButton = (ImageButton) inflatedView.findViewById(R.id.share_button);
		shareButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.shareStatus(context, pnrStatusVo);
			}
		});

		// Create the passenger adapter with the list of apssemgers
		List<PassengerDataVo> passengersList = pnrStatusVo.getPassengers();
		PassengerAdapter adapter = new PassengerAdapter(context, passengersList);
		ListView lv = (ListView) inflatedView.findViewById(android.R.id.list);
		lv.setAdapter(adapter);

		// .setCustomTitle(customTitleView)
		// Ask the system to create an Alert dialog for us
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
			.setCancelable(true)
			.setTitle(R.string.ticket_info_title)
			.setView(inflatedView)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});

		builder.show();
	}

	/**
	 * Display a dialog with the license information
	 * 
	 * @param context
	 */
	public static void showLicenseDialog(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context).setCancelable(true)
				.setTitle(R.string.menu_licence).setMessage(R.string.str_license_text)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		builder.show();
	}
}
