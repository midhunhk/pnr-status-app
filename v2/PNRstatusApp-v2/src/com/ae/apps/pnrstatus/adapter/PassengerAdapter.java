package com.ae.apps.pnrstatus.adapter;

import java.util.List;

import com.ae.apps.pnrstatus.vo.PassengerDataVo;
import com.ae.apps.pnrstatus.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PassengerAdapter extends BaseAdapter {

	//---------------------------------
	// Private Variables
	//---------------------------------
	private LayoutInflater inflater;
	private static List<PassengerDataVo> arrayList;

	/**
	 * 2 arg constructor
	 * @param context
	 * @param list
	 */
	public PassengerAdapter(Context context, List<PassengerDataVo> list) {
		arrayList = list;
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
			// Use the custom layout
			convertView = inflater.inflate( R.layout.passenger_info_row, null);
			
			// Get references to the TextViews in the layout
			holder = new ViewHolder();
			holder.txtLine1 = (TextView) convertView.findViewById(R.id.passenger_info_line1);
			holder.txtLine2 = (TextView) convertView.findViewById(R.id.passenger_info_line2);
			holder.txtLine3 = (TextView) convertView.findViewById(R.id.passenger_info_line3);
			holder.txtLine4 = (TextView) convertView.findViewById(R.id.passenger_info_line4);

			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		// Set the values for the TextViews based on the PassengerDataVo
		PassengerDataVo dataVo = arrayList.get(position);
		if(null != dataVo)
		{
			holder.txtLine1.setText("Passenger Name : " + dataVo.getTrainPassenger());
			holder.txtLine2.setText("Booking Status : " + dataVo.getTrainBookingBerth());
			holder.txtLine3.setText("Current Status : " + dataVo.getTrainCurrentStatus());
			holder.txtLine4.setText("Berth Position : " + dataVo.getBerthPosition());
		}
		
		return convertView;
	}

	/**
	 * Helper class that denotes the elements inside the layout
	 * @author Midhun_Harikumar
	 *
	 */
	static class ViewHolder 
	{
		TextView txtLine1;
		TextView txtLine2;
		TextView txtLine3;
		TextView txtLine4;
	}
}