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

package com.ae.apps.pnrstatus.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ae.apps.pnrstatus.adapters.PassengerAdapter;
import com.ae.apps.pnrstatus.v3.R;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

import java.util.List;

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
		View header = LayoutInflater.from(context).inflate(R.layout.dialog_header, null);

		// Get reference to the textviews
		TextView line1 =  inflatedView.findViewById(R.id.ticket_info_line1);
		TextView line2 =  inflatedView.findViewById(R.id.ticket_info_line2);
		TextView line3 =  inflatedView.findViewById(R.id.ticket_info_line3);
		TextView line4 =  inflatedView.findViewById(R.id.ticket_info_line4);
		TextView line5 =  inflatedView.findViewById(R.id.ticket_info_line5);

		// Get the localized strings for the corresponding texts
		Resources resources = context.getResources();
		String line1Text = resources.getString(R.string.str_train_name_no, pnrStatusVo.getTrainName(),
				pnrStatusVo.getTrainNo());
		String line2Text = resources.getString(R.string.str_journey_date, pnrStatusVo.getTrainJourneyDate());
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
		ImageButton shareButton =  header.findViewById(R.id.share_button);
		shareButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.shareStatus(context, pnrStatusVo);
			}
		});

		// Create the passenger adapter with the list of apssemgers
		List<PassengerDataVo> passengersList = pnrStatusVo.getPassengers();
		PassengerAdapter adapter = new PassengerAdapter(context, passengersList);
		ListView lv =  inflatedView.findViewById(android.R.id.list);
		lv.setAdapter(adapter);

		// Ask the system to create an Alert dialog for us
		android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(context).setCancelable(true)
				.setTitle(R.string.ticket_info_title)
				.setView(inflatedView)
				.setCustomTitle(header)
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
		android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context)
			.setCancelable(true)
			.setTitle(R.string.menu_licence)
			.setMessage(R.string.str_license_text)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		builder.show();
	}
}
