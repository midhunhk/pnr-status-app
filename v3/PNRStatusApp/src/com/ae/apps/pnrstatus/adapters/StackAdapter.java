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

	private List<PNRStatusVo>	items;
	private Context				context;
	private int					xmlResourceId;

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
		PNRStatusVo statusVo = items.get(position);
		if (statusVo != null) {
			holder.pnr.setText(statusVo.getPnrNumber());
			holder.date.setText(statusVo.getDateOfJourneyText());
			holder.journey.setText(statusVo.getTrainBoard());
			String posStr = String.valueOf(position + 1);
			holder.count.setText(posStr);
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