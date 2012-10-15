package com.ae.apps.pnrstatus.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ae.apps.pnrstatus.MainActivity;
import com.ae.apps.pnrstatus.TicketInfoActivity;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.Serializer;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;
import com.ae.apps.pnrstatus.R;

public class PnrRowAdapter extends BaseAdapter {

	//---------------------------------
	// Private Variables
	//---------------------------------
	private Context context;
	private LayoutInflater inflater;
	private static ArrayList<PNRStatusVo> arrayList;
	
	//---------------------------------
	// Methods
	//---------------------------------
	public PnrRowAdapter(Context context, ArrayList<PNRStatusVo> list) {
		arrayList = list;
		this.context = context;
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
		if(convertView == null)
		{
			convertView = inflater.inflate( R.layout.pnr_row_view, null);
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.pnr_number);
			holder.txtStatus = (TextView) convertView.findViewById(R.id.pnr_status);
			holder.txtExtraInfo = (TextView) convertView.findViewById(R.id.extra_info);
			holder.btnCheck = (Button) convertView.findViewById(R.id.check_status);
			holder.btnDelete = (Button) convertView.findViewById(R.id.delete_status);
			holder.btnInfo = (Button) convertView.findViewById(R.id.more_info);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtName.setText( arrayList.get(position).getPnrNumber());
		
		// Show the FirstPassenger Booking Berth if its available
		PassengerDataVo passengerDataVo = arrayList.get(position).getFirstPassengerData();
		if(null != passengerDataVo)
		{
			String message = "Update " + holder.txtName.getText() + " with " + 
				passengerDataVo.getTrainCurrentStatus();
			Log.d(AppConstants.TAG, message);
			
			holder.btnInfo.setEnabled(true);
			holder.txtStatus.setText(passengerDataVo.getTrainCurrentStatus() );
		}
		else
		{
			// Disable the Extra Info Button
			holder.btnInfo.setEnabled(false);
			holder.txtStatus.setText("");
		}
		
		// Get the PNRStatusVo object
		final PNRStatusVo pnrStatusVo = arrayList.get(position);
		final String pnrNumber = pnrStatusVo.getPnrNumber();
		
		// Need to check the Status of the PNR Number
		holder.btnCheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Check the Status of the PNR Number
				Log.d(AppConstants.TAG, "PnrRowAdapter :: Check Button Clicked");
				Intent intent = new Intent(context, MainActivity.class);
				
				intent.putExtra(AppConstants.PNR_NUMBER, pnrNumber);
				intent.putExtra(AppConstants.APP_ACTION, AppConstants.ACTION_CHECK);
				
				context.startActivity(intent);
			}
		});
		
		// Will remove the PNRStatusVo from the list
		holder.btnDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(AppConstants.TAG, "PnrRowAdapter :: Remove Button Clicked");				
				// Create an intent for deleting an item
				Intent intent = new Intent(context, MainActivity.class);
				intent.putExtra(AppConstants.PNR_NUMBER, pnrNumber);
				intent.putExtra(AppConstants.APP_ACTION, AppConstants.ACTION_DELETE);
				
				context.startActivity(intent);
			}
		});
		
		holder.btnInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				
				Log.d(AppConstants.TAG, "PnrRowAdapter :: Info Button Clicked");
				// Create an intent for showing more info
				Intent intent = new Intent(context, TicketInfoActivity.class);
				byte[] objectAsArray = Serializer.serializeObject(pnrStatusVo);
				intent.putExtra(AppConstants.PNR_STATUS_VO, objectAsArray);
				
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	/**
	 * Inner class that represents the UI elements of a PNRRow
	 * @author Midhun_Harikumar
	 *
	 */
	static class ViewHolder 
	{
		TextView txtName;
		TextView txtStatus;
		TextView txtExtraInfo;
		Button btnCheck;
		Button btnDelete;
		Button btnInfo;
	}
}