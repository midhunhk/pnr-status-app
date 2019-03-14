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

package com.ae.apps.pnrstatus.adapters;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ae.apps.pnrstatus.v3.R;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

public class PassengerAdapter extends BaseAdapter {

	private final Context					context;
	private final LayoutInflater			inflater;
	private static List<PassengerDataVo>	arrayList;

	/**
	 * Creates an instance of PassengerAdapter
	 * 
	 * @param context
	 * @param list
	 */
	public PassengerAdapter(Context context, List<PassengerDataVo> list) {
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
		if (convertView == null) {
			// Use the custom layout
			convertView = inflater.inflate(R.layout.popup_passenger_item, null);

			// Get references to the TextViews in the layout
			holder = new ViewHolder();
			holder.txtLine1 = (TextView) convertView.findViewById(R.id.passenger_info_line1);
			holder.txtLine2 = (TextView) convertView.findViewById(R.id.passenger_info_line2);
			holder.txtLine3 = (TextView) convertView.findViewById(R.id.passenger_info_line3);
			holder.txtLine4 = (TextView) convertView.findViewById(R.id.passenger_info_line4);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Set the values for the TextViews based on the PassengerDataVo
		PassengerDataVo dataVo = arrayList.get(position);
		if (null != dataVo) {
			// Show the strings with the help of localization
			Resources resources = context.getResources();
			holder.txtLine1.setText(resources.getString(R.string.str_passengner_name, dataVo.getPassenger()));
			holder.txtLine2.setText(resources.getString(R.string.str_booking_status, dataVo.getBookingBerth()));
			holder.txtLine3.setText(resources.getString(R.string.str_current_status, dataVo.getCurrentStatus()));
			holder.txtLine4.setText(resources.getString(R.string.str_berth_position, dataVo.getBerthPosition()));
		}

		return convertView;
	}

	private static class ViewHolder {
		TextView	txtLine1;
		TextView	txtLine2;
		TextView	txtLine3;
		TextView	txtLine4;
	}
}