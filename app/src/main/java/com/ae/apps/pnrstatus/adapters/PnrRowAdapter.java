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

import static com.ae.apps.pnrstatus.utils.AppConstants.TICKET_STATUS_CONFIRM;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.ae.apps.pnrstatus.R;
import com.ae.apps.pnrstatus.fragments.PnrStatusFragment;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.Logger;
import com.ae.apps.pnrstatus.utils.PNRUtils;
import com.ae.apps.pnrstatus.utils.Utils;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

import java.util.List;

/**
 * Adapter for displaying PNR Rows
 * 
 * @author midhun_harikumar
 * 
 */
public class PnrRowAdapter extends BaseAdapter {

	private final PnrStatusFragment	parentFragment;
	private final List<PNRStatusVo>	arrayList;
	private final LayoutInflater	inflater;
	private final Context			context;

	public PnrRowAdapter(Context context, PnrStatusFragment parentFragment, List<PNRStatusVo> list) {
		arrayList = list;
		this.context = context;
		this.parentFragment = parentFragment;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final String normalPNR = arrayList.get(position).pnrNumber;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.pnr_row_item, null);
			holder = new ViewHolder();
			holder.txtName =  convertView.findViewById(R.id.pnr_number);
			holder.txtStatus =  convertView.findViewById(R.id.pnr_status);
			holder.btnCheck =  convertView.findViewById(R.id.check_status);
			holder.btnDelete =  convertView.findViewById(R.id.delete_status);
			holder.btnInfo =  convertView.findViewById(R.id.more_info);

			convertView.setTag(holder);

			// When clicked on the pnr number, copy it to the clipboard
			holder.txtName.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Utils.copyTextToClipboard(context, normalPNR);
				}
			});
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String formattedString = PNRUtils.formatPNRString(normalPNR);
		holder.txtName.setText(formattedString);

		// Show the FirstPassenger Booking Berth if its available
		PassengerDataVo passengerDataVo = arrayList.get(position).getFirstPassengerData();
		if (null != passengerDataVo) {
			String message = "Update " + holder.txtName.getText() + " with " + passengerDataVo.getCurrentStatus();
			Logger.d(AppConstants.TAG, message);

			holder.btnInfo.setEnabled(true);
			// holder.btnInfo.setImageResource(R.drawable.ic_info);
			String currentStatus = passengerDataVo.getCurrentStatus();
			assert currentStatus != null;
			if (currentStatus.contains(TICKET_STATUS_CONFIRM) && passengerDataVo.getBookingBerth() != null) {
				holder.txtStatus.setText(passengerDataVo.getBookingBerth());
			} else {
				holder.txtStatus.setText(currentStatus);
			}
		} else {
			// Disable the Extra Info Button
			holder.btnInfo.setEnabled(false);
			// Clear the status as well
			holder.txtStatus.setText("");
		}

		// Get the PNRStatusVo object
		final PNRStatusVo pnrStatusVo = arrayList.get(position);

		// Check the status of a PNR Number
		holder.btnCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				holder.txtStatus.setText("");
				parentFragment.checkStatus(pnrStatusVo);
			}
		});

		// Remove the PNR Number from the view
		holder.btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				Activity activity = parentFragment.getActivity();
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setCancelable(true);
				builder.setMessage(activity.getString(R.string.message_delete_pnr));

				// Set the positive button click
				builder.setPositiveButton(activity.getString(android.R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								parentFragment.removeRow(pnrStatusVo);
								dialog.dismiss();
							}
						});

				// Set the negative button click
				builder.setNegativeButton(activity.getString(android.R.string.cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});

				builder.show();

			}
		});

		holder.btnInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				parentFragment.showPNRStatusDetail(pnrStatusVo);
			}
		});
		return convertView;
	}

	private static class ViewHolder {
		TextView	txtName;
		TextView	txtStatus;
		ImageButton	btnCheck;
		ImageButton	btnDelete;
		ImageButton	btnInfo;
	}
}