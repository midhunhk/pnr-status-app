package com.ae.apps.pnrstatus.fragments;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.StackView;
import android.widget.TextView;

import com.ae.apps.pnrstatus.adapters.StackAdapter;
import com.ae.apps.pnrstatus.managers.PNRMessagesManager;
import com.ae.apps.pnrstatus.managers.SMSManager;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.Utils;
import com.ae.apps.pnrstatus.v3.R;
import com.ae.apps.pnrstatus.vo.MessageVo;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

public class MessagesFragment extends Fragment {

	private View				layout;
	private Context				context;
	private FragmentActivity	activity;
	private String				ADDRESS			= "IRCTC";

	private TextView			mPassengerName	= null;
	private TextView			mCurrentStatus	= null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = super.getActivity();
		layout = inflater.inflate(R.layout.messages_view, null);
		context = activity.getApplicationContext();

		if (Utils.isDevelopmentMode()) {
			ADDRESS = "11";
		}

		initActivity();

		return layout;
	}

	private void initActivity() {
		// Find the StackView from the fragment's view
		StackView stackView = (StackView) layout.findViewById(R.id.stackView);
		mPassengerName = (TextView) layout.findViewById(R.id.detailsPassengerName);
		mCurrentStatus = (TextView) layout.findViewById(R.id.detailsCurrentStatus);

		// Create a MessageManager instance
		SMSManager smsManager = new SMSManager(context);

		// Fetch the list of messages by the address
		final List<MessageVo> allSmsList = smsManager.fetchMessagesByAddress(ADDRESS);

		// Create a PNRMessagesManager
		PNRMessagesManager messagesManager = new PNRMessagesManager(allSmsList);
		final List<PNRStatusVo> pnrStatusList = messagesManager.getMessagesList();

		StackAdapter adapter = new StackAdapter(context, R.layout.message_item, pnrStatusList);
		stackView.setAdapter(adapter);

		// Add a click listener for the item

		stackView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PNRStatusVo pnrStatusVo = pnrStatusList.get(position);
				updateMessageDetails(pnrStatusVo);
			}
		});
	}

	/**
	 * Update the details on the message pane
	 * 
	 * @param messageVo
	 */
	private void updateMessageDetails(PNRStatusVo pnrStatusVo) {
		PassengerDataVo passengerDataVo = pnrStatusVo.getFirstPassengerData();
		if (passengerDataVo != null) {
			mPassengerName.setText(passengerDataVo.getTrainPassenger());
			String position = pnrStatusVo.getCurrentStatus();
			if(position.equals(AppConstants.BERTH_DEFAULT) == false)
			{
				position = position + " (" + passengerDataVo.getBerthPosition() + ")"; 
			}
			mCurrentStatus.setText(position);
		}
	}
}
