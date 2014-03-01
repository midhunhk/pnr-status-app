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

package com.ae.apps.pnrstatus.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.ae.apps.pnrstatus.exceptions.StatusException;
import com.ae.apps.pnrstatus.exceptions.StatusException.ErrorCodes;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.HttpUtils;
import com.ae.apps.pnrstatus.utils.PNRUtils;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

public class PnrApiService implements IStatusService {

	private static final String	FIELD_NAME	= "name";
	/**
	 * The URL for PNRAPI Service
	 */
	private final String		url			= "http://pnrapi.alagu.net/api/v1.0/pnr/";
	private final String		serviceName	= "PNRAPI";

	@Override
	public String getServiceName() {
		return serviceName;
	}

	@Override
	public PNRStatusVo getResponse(String pnrNumber) throws StatusException {
		String searchUrl = getServiceUrl(pnrNumber);
		Log.i(AppConstants.TAG, "Using " + getServiceName());
		Log.d(AppConstants.TAG, "SearchURL :  " + searchUrl);

		PNRStatusVo pnrStatusVo = null;

		try {
			// String response = PNRUtils.getWebResult(searchUrl);
			String response = HttpUtils.sendGet(searchUrl);
			Log.d(AppConstants.TAG, "WebResultResponse : " + response);
			pnrStatusVo = parseResponse(response);
		} catch (Exception e) {
			throw new StatusException(e.getMessage(), e);
		}
		return pnrStatusVo;
	}

	@Override
	public PNRStatusVo getResponse(String pnrNumber, Boolean stubResponse) throws StatusException {
		PNRStatusVo statusVo = null;
		if (stubResponse == true) {
			// Return from the stub response
			statusVo = parseResponse(getStubResponse());
		} else {
			// Get the response from the server
			statusVo = getResponse(pnrNumber);
		}
		// Set the pnrnumber to the vo so that the ui can update the correct one in the list
		if (statusVo != null) {
			statusVo.setPnrNumber(pnrNumber);
		}
		return statusVo;
	}

	private String getStubResponse() {
		String response = "{'status':'OK','data':{'train_number':'12623','chart_prepared':false,'pnr_number':'4448820672','train_name':'TRIVANDRUM MAIL','travel_date':{'timestamp':1366914600,'date':'26-4-2013'},'from':{'code':'MAS','name':'CHENNAI CENTRAL','time':'19:45'},'to':{'code':'KTYM','name':'KOTTAYAM','time':'07:35'},'alight':{'code':'KTYM','name':'KOTTAYAM','time':'07:35'},'board':{'code':'MAS','name':'CHENNAI CENTRAL','time':'19:45','timestamp':1366985700},'class':'SL','passenger':[{'seat_number':'RAC 108,GNWL','status':'Confirmed'}]}}";
		return response;
	}

	private String getServiceUrl(String pnrNumber) {
		if (null != pnrNumber && !pnrNumber.equals("")) {
			return url + pnrNumber;
		}
		return "";
	}

	private PNRStatusVo parseResponse(String responseString) throws StatusException {
		PNRStatusVo statusVo = new PNRStatusVo();
		try {
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

				String trainDest = getField(dataObject.getJSONObject("alight"), FIELD_NAME);
				String trainName = dataObject.getString("train_name");
				String trainNo = PNRUtils.getTrainNo(dataObject.getString("train_number"));
				String trainBoard = getField(dataObject.getJSONObject("board"), FIELD_NAME);
				String trainEmbark = getField(dataObject.getJSONObject("to"), FIELD_NAME);
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
				statusVo.setBoardingPoint(trainBoard);
				statusVo.setDestination(trainDest);
				statusVo.setEmbarkPoint(trainEmbark);
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
		} catch (JSONException exception) {
			throw new StatusException("Json response error", exception, ErrorCodes.PARSE_ERROR);
		}
		return statusVo;
	}

	private String getField(JSONObject object, String fieldName) throws JSONException {
		return object.getString(fieldName);
	}
}