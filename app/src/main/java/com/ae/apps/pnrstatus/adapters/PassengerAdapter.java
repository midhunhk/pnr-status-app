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

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ae.apps.pnrstatus.R;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

import java.util.List;

public class PassengerAdapter extends BaseAdapter {

	private final Context					context;
	private final LayoutInflater			inflater;
	private static List<PassengerDataVo>	arrayList;

	/**
	 * Creates an instance of PassengerAdapter
	 *
	 * @param context the context
	 * @param list list of passenger data
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
			holder.txtLine1 =  convertView.findViewById(R.id.passenger_info_line1);
			holder.txtLine2 =  convertView.findViewById(R.id.passenger_info_line2);
			holder.txtLine3 =  convertView.findViewById(R.id.passenger_info_line3);
			holder.txtLine4 = convertView.findViewById(R.id.passenger_info_line4);

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