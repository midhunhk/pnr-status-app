package com.ae.apps.pnrstatus.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ae.apps.pnrstatus.fragments.PnrStatusFragment;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.v3.R;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

/**
 * Adapter for displaying PNR Rows
 * 
 * @author midhun_harikumar
 * 
 */
public class PnrRowAdapter extends BaseAdapter {

	private LayoutInflater			inflater;
	private PnrStatusFragment		parentFragment;
	private List<PNRStatusVo>	arrayList;

	public PnrRowAdapter(Context context, PnrStatusFragment parentFragment, List<PNRStatusVo> list) {
		arrayList = list;
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.pnr_row_view, null);
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.pnr_number);
			holder.txtStatus = (TextView) convertView.findViewById(R.id.pnr_status);
			holder.btnCheck = (Button) convertView.findViewById(R.id.check_status);
			holder.btnDelete = (Button) convertView.findViewById(R.id.delete_status);
			holder.btnInfo = (Button) convertView.findViewById(R.id.more_info);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtName.setText(arrayList.get(position).getPnrNumber());

		// Show the FirstPassenger Booking Berth if its available
		PassengerDataVo passengerDataVo = arrayList.get(position).getFirstPassengerData();
		if (null != passengerDataVo) {
			String message = "Update " + holder.txtName.getText() + " with " + passengerDataVo.getTrainCurrentStatus();
			Log.d(AppConstants.TAG, message);

			holder.btnInfo.setEnabled(true);
			holder.txtStatus.setText(passengerDataVo.getTrainCurrentStatus());
		} else {
			// Disable the Extra Info Button
			holder.btnInfo.setEnabled(false);
			holder.txtStatus.setText("");
		}

		// Get the PNRStatusVo object
		final PNRStatusVo pnrStatusVo = arrayList.get(position);

		// Check the status of the PNR Number
		holder.btnCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				Log.d(AppConstants.TAG, "PnrRowAdapter :: Check Button Clicked");
				parentFragment.checkStatus(pnrStatusVo);
			}
		});

		// Remove the PNR Number from the view
		holder.btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				Log.d(AppConstants.TAG, "PnrRowAdapter :: Remove Button Clicked");
				parentFragment.removeRow(pnrStatusVo);
			}
		});

		// Display the details of the pnrstatus
		holder.btnInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				Log.d(AppConstants.TAG, "PnrRowAdapter :: Info Button Clicked");
				parentFragment.showPNRStatusDetail(pnrStatusVo);
			}
		});

		return convertView;
	}

	private static class ViewHolder {
		TextView	txtName;
		TextView	txtStatus;
		Button		btnCheck;
		Button		btnDelete;
		Button		btnInfo;
	}
}