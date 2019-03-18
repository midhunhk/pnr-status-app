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

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ae.apps.pnrstatus.v3.R;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;

/**
 * Data Adapter for StackView
 * 
 * @author midhun_harikumar
 * 
 */
public class StackAdapter extends ArrayAdapter<PNRStatusVo> {

	private final List<PNRStatusVo>	items;
	private final Context			context;
	private final int				xmlResourceId;

	public StackAdapter(Context context, int textViewResourceId, List<PNRStatusVo> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.items = objects;
		this.xmlResourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			// Infalte the view
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(xmlResourceId, null);

			// Set alternate backgrounds
			if (position % 2 == 0) {
				convertView.setBackgroundResource(R.drawable.row_bg01);
			} else {
				convertView.setBackgroundResource(R.drawable.row_bg02);
			}
			// Get reference to the text views
			holder = new ViewHolder();
			holder.pnr = (TextView) convertView.findViewById(R.id.messageItemPnr);
			holder.date = (TextView) convertView.findViewById(R.id.messageItemDate);
			holder.count = (TextView) convertView.findViewById(R.id.messageItemNumber);
			holder.journey = (TextView) convertView.findViewById(R.id.messageItemJourney);

			// Save the holder as this views tag
			convertView.setTag(holder);
		} else {
			// get the holder from the tag
			holder = (ViewHolder) convertView.getTag();
		}

		// Read the data
		if (items.size() > 0) {
			PNRStatusVo statusVo = items.get(position);
			if (statusVo != null) {
				holder.pnr.setText(statusVo.getPnrNumber());
				holder.date.setText(statusVo.getDateOfJourneyText());
				holder.journey.setText(statusVo.getBoardingPoint());
				String posStr = String.valueOf(position + 1);
				holder.count.setText(posStr);
			}
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView	pnr;
		TextView	date;
		TextView	journey;
		TextView	count;
	}

}