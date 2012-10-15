package com.ae.apps.pnrstatus;

import java.util.List;

import com.ae.apps.pnrstatus.adapter.PassengerAdapter;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.Serializer;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;
import com.ae.apps.pnrstatus.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TicketInfoActivity extends ListActivity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ticket_info);
		
		byte[] objectAsArray = getIntent().getExtras().getByteArray(AppConstants.PNR_STATUS_VO);
		
		if(null != objectAsArray)
		{
			final PNRStatusVo pnrStatusVo = (PNRStatusVo) Serializer.deserializeObject(objectAsArray);
			if(null != pnrStatusVo)
			{
				Log.d(AppConstants.TAG, "TicketInfoActivity :: Got DeserializedObject");
				
				// Get references to the info_lines TextViews
				TextView txtPnrNumber = (TextView) findViewById(R.id.ticket_pnrnumber);
				TextView line1 = (TextView) findViewById(R.id.ticket_info_line1);
				TextView line2 = (TextView) findViewById(R.id.ticket_info_line2);
				TextView line3 = (TextView) findViewById(R.id.ticket_info_line3);
				TextView line4 = (TextView) findViewById(R.id.ticket_info_line4);
				
				// Set the values to the textviews from the vo
				String trainInfo = "Train : " + pnrStatusVo.getTrainName() + " (" + pnrStatusVo.getTrainNo() + ")";
				
				Log.d(AppConstants.TAG, trainInfo);
				
				txtPnrNumber.setText(pnrStatusVo.getPnrNumber());
				line1.setText(trainInfo);
				line2.setText("Journey Date : " + pnrStatusVo.getTrainJourney());
				line3.setText("Boarding At  : " + pnrStatusVo.getTrainBoard());
				line4.setText("Destination  : " + pnrStatusVo.getTrainDest());
				
				// Set the list
				List<PassengerDataVo> passengersList= pnrStatusVo.getPassengers();
				PassengerAdapter adapter = new PassengerAdapter(getApplicationContext(), passengersList);
				setListAdapter(adapter);
				
				// Share Button
				Button shareButton = (Button) findViewById(R.id.share_button);
				
				shareButton.setOnClickListener( new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// Create a new intent for Sharing the status
						Intent shareIntent = new Intent(Intent.ACTION_SEND);
						shareIntent.setType("text/plain");
						PassengerDataVo dataVo = pnrStatusVo.getFirstPassengerData();
						
						// Create the Subject and body for the message
						String shareSubject = "PNR Status for " + pnrStatusVo.getPnrNumber();
						String shareBody = "My PNR Status is " + 
							dataVo.getTrainCurrentStatus() + " , " + 
							dataVo.getTrainBookingBerth() + " #PNRStatusApp";
						
						Log.i(AppConstants.TAG, "ShareStatus : " + shareBody);
						
						// Put the extras
						shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
						shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
						
						// Start the chooser activity to let the user choose an app to share
						startActivity(Intent.createChooser(shareIntent, "Share Via"));
					}
				});
			}
			else
			{
				Log.e(AppConstants.TAG, "TicketInfoActivity :: Did not get DeserializedObject");
			}
		}
	}

}
