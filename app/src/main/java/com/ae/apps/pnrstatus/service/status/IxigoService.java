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

package com.ae.apps.pnrstatus.service.status;

import android.util.Log;

import com.ae.apps.pnrstatus.exceptions.StatusException;
import com.ae.apps.pnrstatus.service.IStatusService;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.PNRUtils;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IxigoService implements IStatusService {

	/**
	 * The URL for IXIGO Service
	 */
	private final String	url			= "http://216.139.222.96:80/train/pnr_status";
	private final String	serviceName	= "IXIGO Service";

	@Override
	public String getServiceName() {
		return serviceName;
	}

	public String getStubResponse() {
		String response = "{\"passengers\": [{\"trainBookingBerth\": \"S5  , 43,GN    \",\"trainPassenger\": \"Passenger 1\",\"trainCurrentStatus\": \"   CNF  \"},{\"trainBookingBerth\": \"S5  , 46,GN    \",\"trainPassenger\": \"Passenger 2\",\"trainCurrentStatus\": \"   CNF  \"},{\"trainBookingBerth\": \"S5  , 42,GN    \",\"trainPassenger\": \"Passenger 3\",\"trainCurrentStatus\": \"   CNF  \"},{\"trainBookingBerth\": \"S5  , 45,GN    \",\"trainPassenger\": \"Passenger 4\",\"trainCurrentStatus\": \"   CNF  \"}],\"trainDest\": \"Chennai Central\",\"trainOrigin\": \"Eranakulam Jn\",\"trainFareClass\": \" SL\",\"chartStat\": \" CHART NOT PREPARED \",\"trainBoard\": \"Eranakulam Jn\",\"trainEmbark\": \"Chennai Central\",\"trainNo\": \"*16042\",\"trainName\": \"CHENNAI EXPRESS\",\"trainJourney\": \"26-12-2010\"}";
		// String response =
		// "{\"passengers\": [{\"trainBookingBerth\": \"S3  , 14,GN    \",\"trainPassenger\": \"Passenger 1\",\"trainCurrentStatus\": \"   CNF  \"}],\"trainDest\": \"Chennai Central\",\"trainOrigin\": \"Eranakulam Jn\",\"trainFareClass\": \" SL\",\"chartStat\": \" CHART NOT PREPARED \",\"trainBoard\": \"Eranakulam Jn\",\"trainEmbark\": \"Chennai Central\",\"trainNo\": \"*16042\",\"trainName\": \"CHENNAI EXPRESS\",\"trainJourney\": \"14-11-2011\"}";
		return response;
	}

	@Override
	public PNRStatusVo getResponse(String pnrNumber) throws StatusException {
		String searchUrl = getServiceUrl(pnrNumber);
		Log.i(AppConstants.TAG, "Using " + getServiceName());
		Log.d(AppConstants.TAG, "SearchURL :  " + searchUrl);

		String response = null;
		PNRStatusVo pnrStatusVo = null;

		try {
			response = PNRUtils.getWebResult(searchUrl);
			pnrStatusVo = parseResponse(response);
		} catch (IOException e) {
			throw new StatusException("IO Error occured");
		}

		Log.d(AppConstants.TAG, "WebResultResponse : " + response);
		return pnrStatusVo;
	}

	@Override
	public PNRStatusVo getResponse(String pnrNumber, Boolean stubResponse) throws StatusException {
		if (stubResponse == true) {
			return parseResponse(getStubResponse());
		}
		return getResponse(pnrNumber);
	}

	private String getServiceUrl(String pnrNumber) {
		if (null != pnrNumber && !pnrNumber.equals("")) {
			String pnr1 = pnrNumber.substring(0, 3);
			String pnr2 = pnrNumber.substring(3);

			return url + "?pnr1=" + pnr1 + "&pnr2=" + pnr2;
		}
		return "";
	}

	/**
	 * This function parses the response
	 */
	protected PNRStatusVo parseResponse(String responseString) throws StatusException {
		PNRStatusVo statusVo = new PNRStatusVo();
		try {

			JSONTokener jsonTokener = new JSONTokener(responseString);
			JSONObject object = (JSONObject) jsonTokener.nextValue();

			JSONArray passengersArray = object.getJSONArray("passengers");

			String firstPassengerStatus = "";
			String trainDest = object.getString("trainDest");
			String trainJourney = object.getString("trainJourney");
			String trainName = object.getString("trainName");
			String trainNo = PNRUtils.getTrainNo(object.getString("trainNo"));
			String trainBoard = object.getString("trainBoard");
			String trainEmbark = object.getString("trainEmbark");
			String ticketClass = object.getString("trainFareClass");

			// Read the PassengerDataVos
			List<PassengerDataVo> passengers = new ArrayList<PassengerDataVo>();

			for (int i = 0; i < passengersArray.length(); i++) {
				JSONObject object2 = passengersArray.getJSONObject(i);
				String trainBookingBerth = object2.getString("trainBookingBerth").trim();
				String trainCurrentStatus = object2.getString("trainCurrentStatus").trim();
				String trainPassenger = object2.getString("trainPassenger");
				String berthPosition = "";

				// Calculate the BerthPosition
				berthPosition = PNRUtils.getBerthPosition(trainCurrentStatus, trainBookingBerth,
						AppConstants.CLASS_UNKNOWN, ",");

				PassengerDataVo dataVo = new PassengerDataVo();
				dataVo.setBookingBerth(trainBookingBerth);
				dataVo.setCurrentStatus(trainCurrentStatus);
				dataVo.setPassenger(trainPassenger);
				dataVo.setBerthPosition(berthPosition);

				passengers.add(dataVo);
			}

			// Get the first passenger status
			if (passengers.size() > 0) {
				PassengerDataVo dataVo = (passengers.get(0));
				statusVo.setFirstPassengerData(dataVo);
				firstPassengerStatus = dataVo.getCurrentStatus();
			}

			// Set the values for the StatusVo
			statusVo.boardingPoint = trainBoard;
			statusVo.setDestination(trainDest);
			statusVo.setEmbarkPoint(trainEmbark);
			statusVo.setTrainJourneyDate(trainJourney);
			statusVo.trainName = trainName;
			statusVo.trainNo = trainNo;
			statusVo.setTicketClass(ticketClass);
			statusVo.setCurrentStatus(firstPassengerStatus);

			statusVo.passengers = passengers;
		} catch (JSONException exception) {
			throw new StatusException("Json Exception");
		}
		return statusVo;

	}
}