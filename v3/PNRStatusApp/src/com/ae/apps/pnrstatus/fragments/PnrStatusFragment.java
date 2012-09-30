package com.ae.apps.pnrstatus.fragments;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ae.apps.pnrstatus.adapters.PnrRowAdapter;
import com.ae.apps.pnrstatus.v3.R;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;

/**
 * The PNRStatus Fragment
 * 
 * @author midhun_harikumar
 * 
 */
public class PnrStatusFragment extends ListFragment {

	// http://developer.android.com/guide/components/fragments.html
	private static final String		TAG	= "PnrStatusFragment";

	private View					layout;
	private Context					context;
	private PnrRowAdapter			mCustomAdapter;
	private FragmentActivity		activity;
	private OnCheckStatusListener	mCallback;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = super.getActivity();
		layout = inflater.inflate(R.layout.pnr_status_view, null);
		context = activity.getApplicationContext();
		initActivity();
		return layout;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnCheckStatusListener) activity;
		} catch (ClassCastException ex) {
			throw new ClassCastException(activity.toString() + " must implement OnCheckStatusListener");
		}
	}

	/**
	 * Do initialisations
	 */
	private void initActivity() {

		try {

			// Get the data list from the db and set the data adapter
			List<PNRStatusVo> pnrList = mCallback.getListData();
			mCustomAdapter = new PnrRowAdapter(context, this, pnrList);

			// set the adapter for the list and the data manager
			setListAdapter(mCustomAdapter);
			mCallback.setPNRStatusAdapter(mCustomAdapter);

			// Get a reference to the EditText Object
			final EditText txtPnrNumber = (EditText) layout.findViewById(R.id.pnrTextInput);

			final Button btnAdd = (Button) layout.findViewById(R.id.add_pnr_btn);
			btnAdd.setFocusable(true);
			btnAdd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Create a PNRStatusVo object
					String pnrNumber = txtPnrNumber.getText().toString();
					Log.i(TAG, "Add a pnr " + pnrNumber);
					if (pnrNumber != null && pnrNumber.trim().length() == 10) {
						PNRStatusVo statusVo = new PNRStatusVo();
						statusVo.setPnrNumber(pnrNumber);
						statusVo.setCurrentStatus("");
						statusVo.setFirstPassengerData(null);
						statusVo.setPassengers(null);

						// Clear the input
						txtPnrNumber.setText("");
						txtPnrNumber.requestFocus();
						btnAdd.requestFocus();

						// Add the PNRStatusVo to the list
						mCallback.addPnr(statusVo);
					} else {
						String message = "The entered PNR Number " + pnrNumber + " is invalid!";
						Toast.makeText(context, message, Toast.LENGTH_LONG).show();
					}

				}
			});

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void checkStatus(PNRStatusVo pnrStatusVo) {
		mCallback.checkStatus(pnrStatusVo);
	}

	public void removeRow(PNRStatusVo pnrStatusVo) {
		mCallback.removePnr(pnrStatusVo);
	}

	public void showPNRStatusDetail(PNRStatusVo pnrStatusVo) {
		mCallback.showPNRStatusInfo(pnrStatusVo);
	}

	/**
	 * Container activity must implement this method
	 * 
	 * @author midhun_harikumar
	 * 
	 */
	public interface OnCheckStatusListener {
		/**
		 * Returns a list of PNRStatusVos
		 * 
		 * @return
		 */
		List<PNRStatusVo> getListData();

		/**
		 * Adds a PNRNumber to the list, and notify the adapter if needed
		 * 
		 * @param pnrStatusVo
		 */
		void addPnr(PNRStatusVo pnrStatusVo);

		/**
		 * Removes a PNRNumber from the list
		 * 
		 * @param pnrStatusVo
		 */
		void removePnr(PNRStatusVo pnrStatusVo);

		/**
		 * Checks the status of a PNR Number
		 * 
		 * @param pnrStatusVo
		 */
		void checkStatus(PNRStatusVo pnrStatusVo);

		/**
		 * Set an adapter to the list
		 * 
		 * @param adapter
		 */
		void setPNRStatusAdapter(BaseAdapter adapter);

		/**
		 * Show the PNRStatus information indetail
		 * 
		 * @param pnrStatusVo
		 */
		void showPNRStatusInfo(PNRStatusVo pnrStatusVo);
	}

}