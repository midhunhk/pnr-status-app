package com.ae.apps.pnrstatus.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.ae.apps.pnrstatus.exceptions.StatusException;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.PNRUtils;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

public class PnrApiService implements IStatusService {

	/**
	 * The URL for PNRAPI Service
	 */
	private final String	url			= "http://pnrapi.appspot.com/";
	private final String	serviceName	= "PNRAPI";

	@Override
	public String getServiceName() {
		return serviceName;
	}

	@Override
	public PNRStatusVo getResponse(String pnrNumber) throws JSONException, StatusException, IOException {
		String searchUrl = getServiceUrl(pnrNumber);
		Log.i(AppConstants.TAG, "Using " + getServiceName());
		Log.d(AppConstants.TAG, "SearchURL :  " + searchUrl);

		String response = PNRUtils.getWebResult(searchUrl);
		// String response = pnrService.getStubResponse();

		Log.d(AppConstants.TAG, "WebResultResponse : " + response);
		return parseResponse(response);
	}

	@Override
	public PNRStatusVo getResponse(String pnrNumber, Boolean stubResponse) throws JSONException, StatusException,
			IOException {
		if (stubResponse == true) {
			return parseResponse(getStubResponse());
		}
		return getResponse(pnrNumber);
	}

	private String getStubResponse() {
		String response = "{'status': 'OK', 'data': {'passenger': [{'status': 'CNF', 'seat_number': 'S1  , 59,GN'}, {'status': 'CNF', 'seat_number': 'S1  , 62,GN'}, {'status': 'CNF', 'seat_number': 'S1  , 58,GN'}], 'from': 'ERS', 'alight': 'MAS', 'pnr_number': '4546028247', 'train_number': '*16042', 'to': 'MAS', 'board': 'ERS', 'train_name': 'CHENNAI EXPRESS', 'travel_date': {'date': '26-12-2011', 'timestamp': 1324857600}, 'class': 'SL'}}";
		return response;
	}

	private String getServiceUrl(String pnrNumber) {
		if (null != pnrNumber && !pnrNumber.equals("")) {
			return url + pnrNumber;
		}
		return "";
	}

	private PNRStatusVo parseResponse(String responseString) throws JSONException, StatusException {
		PNRStatusVo statusVo = new PNRStatusVo();

		JSONTokener jsonTokener = new JSONTokener(responseString);
		JSONObject object = (JSONObject) jsonTokener.nextValue();

		// Get the status of the PNR Number
		String ticketStatus = object.getString("status");

		if (ticketStatus.equals(PNRStatus.OK)) {
			JSONObject dataObject = object.getJSONObject("data");
			JSONArray passengersArray = dataObject.getJSONArray("passenger");

			String firstPassengerStatus = "";

			JSONObject dateObject = dataObject.getJSONObject("travel_date");
			String trainJourney = dateObject.getString("date");

			String trainDest = dataObject.getString("alight");
			String trainName = dataObject.getString("train_name");
			String trainNo = PNRUtils.getTrainNo(dataObject.getString("train_number"));
			String trainBoard = dataObject.getString("board");
			String trainEmbark = dataObject.getString("to");
			String ticketClass = dataObject.getString("class");

			// Read the PassengerDataVos
			List<PassengerDataVo> passengers = new ArrayList<PassengerDataVo>();

			for (int i = 0; i < passengersArray.length(); i++) {
				JSONObject object2 = passengersArray.getJSONObject(i);
				String trainBookingBerth = object2.getString("seat_number").trim();
				String trainCurrentStatus = object2.getString("status").trim();
				String trainPassenger = "Passenger " + (i + 1);
				String berthPosition = "";

				// Calculate the BerthPosition
				berthPosition = PNRUtils.getBerthPosition(trainCurrentStatus, trainBookingBerth, ticketClass, ",");

				PassengerDataVo dataVo = new PassengerDataVo();
				dataVo.setTrainBookingBerth(trainBookingBerth);
				dataVo.setTrainCurrentStatus(trainCurrentStatus);
				dataVo.setTrainPassenger(trainPassenger);
				dataVo.setBerthPosition(berthPosition);

				passengers.add(dataVo);
			}

			// Get the first passenger status
			if (passengers.size() > 0) {
				PassengerDataVo dataVo = (passengers.get(0));
				statusVo.setFirstPassengerData(dataVo);
				firstPassengerStatus = dataVo.getTrainCurrentStatus();
			}

			// Set the values for the StatusVo
			statusVo.setTicketStatus(ticketStatus);
			statusVo.setTrainBoard(trainBoard);
			statusVo.setDestination(trainDest);
			statusVo.setTrainEmbark(trainEmbark);
			statusVo.setTrainJourney(trainJourney);
			statusVo.setTrainName(trainName);
			statusVo.setTrainNo(trainNo);
			statusVo.setTicketClass(ticketClass);
			statusVo.setCurrentStatus(firstPassengerStatus);

			statusVo.setPassengers(passengers);
		} else {
			// ticket may be invalid or server timed out
			StatusException exception = new StatusException();
			exception.setStatusString(ticketStatus);
			throw exception;
		}

		return statusVo;
	}
	/*
	 * { 'status': 'OK', 'data': { 'passenger': [ { 'status': 'CNF', 'seat_number': 'S1 , 59,GN' }, { 'status': 'CNF',
	 * 'seat_number': 'S1 , 62,GN' }, { 'status': 'CNF', 'seat_number': 'S1 , 58,GN' }], 'from': 'ERS', 'alight': 'MAS',
	 * 'pnr_number': '4546028247', 'train_number': '*16042', 'to': 'MAS', 'board': 'ERS', 'train_name': 'CHENNAI
	 * EXPRESS', 'travel_date': { 'date': '26-12-2011', 'timestamp': 1324857600 }, 'class': 'SL' } }
	 */

}