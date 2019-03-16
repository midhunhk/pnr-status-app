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

package com.ae.apps.pnrstatus.service.status;

import com.ae.apps.pnrstatus.exceptions.StatusException;
import com.ae.apps.pnrstatus.exceptions.StatusException.ErrorCodes;
import com.ae.apps.pnrstatus.service.IStatusService;
import com.ae.apps.pnrstatus.service.NetworkService;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.Logger;
import com.ae.apps.pnrstatus.utils.PNRUtils;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An implementation of StatusService based on the PNRStatusService
 *
 * @author Midhun
 */
public class PNRStatusService implements IStatusService {

    private static final String SERVICE_NAME = "PNRStatus";
    private static final String PARAM_REFERER = "Referer";
    private static final String PARAM_PNR = "code";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String REFERRER_URL = "http://www.pnrstatus.in/";
    private static final String SERVICE_URL = "http://www.pnrstatus.in/pnr/query.php";

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    public PNRStatusVo getResponse(String pnrNumber) throws StatusException {
        Logger.i(AppConstants.TAG, "Using " + getServiceName());

        PNRStatusVo pnrStatusVo = null;

        // Create the headers and params for request
        HashMap<String, String> params = new HashMap<String, String>();
        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put(PARAM_REFERER, REFERRER_URL);
        headers.put(CONTENT_TYPE, "application/x-www-form-urlencoded");
        params.put(PARAM_PNR, pnrNumber);

        try {
            String response = NetworkService.getInstance().doPostRequest(SERVICE_URL, headers, params);
            if (response == null) {
                throw new StatusException("responseObject is null", ErrorCodes.EMPTY_RESPONSE);
            }
            pnrStatusVo = parseResponse(response);
        } catch (Exception e) {
            throw new StatusException(e.getMessage(), e);
        }
        return pnrStatusVo;
    }

    private PNRStatusVo parseResponse(String response) throws StatusException {
        PNRStatusVo statusVo = new PNRStatusVo();
        try {
            JSONTokener jsonTokener = new JSONTokener(response);
            JSONObject rootObject = (JSONObject) jsonTokener.nextValue();

            String charting = rootObject.getString("Charting");

            // Get the train / journey details
            String journey = rootObject.getString("Journey");
            JSONObject journeyDetails = getJsonObject(journey);

            String ticketClass = journeyDetails.getString("Class");
            statusVo.setChartStatus(charting);
            statusVo.setTrainNo(journeyDetails.getString("Train Number"));
            statusVo.setTrainName(journeyDetails.getString("Train Name"));
            statusVo.setTrainJourneyDate(journeyDetails.getString("Boarding Date"));
            // statusVo.setTrainJourney(journeyObject.getString("From"));
            statusVo.setEmbarkPoint(journeyDetails.getString("To"));
            statusVo.setDestination(journeyDetails.getString("Reserved Upto"));
            statusVo.setBoardingPoint(journeyDetails.getString("Boarding Point"));
            statusVo.setTicketClass(ticketClass);

            // Get the passenger details
            JSONArray jsonStatus = rootObject.getJSONArray("Status");
            List<PassengerDataVo> passengers = new ArrayList<PassengerDataVo>();
            PassengerDataVo passengerDataVo = null;
            JSONArray jsonArray = null;
            String berthPosition = null;
            String bookingStatus = null;
            String currentStatus = null;
            for (int i = 1; i < jsonStatus.length(); i++) {
                jsonArray = (JSONArray) jsonStatus.get(i);
                bookingStatus = jsonArray.getString(1);
                currentStatus = jsonArray.getString(2);
                berthPosition = PNRUtils.getBerthPosition(currentStatus, bookingStatus, ticketClass, ",");

                passengerDataVo = new PassengerDataVo();
                passengerDataVo.setPassenger("Passenger " + i);
                passengerDataVo.setBookingBerth(bookingStatus);
                passengerDataVo.setCurrentStatus(currentStatus);
                passengerDataVo.setBerthPosition(berthPosition);

                passengers.add(passengerDataVo);
            }
            // Get the first passenger status
            String firstPassengerStatus = null;
            if (passengers.size() > 0) {
                PassengerDataVo dataVo = (passengers.get(0));
                statusVo.setFirstPassengerData(dataVo);
                firstPassengerStatus = dataVo.getCurrentStatus();
            }
            statusVo.setCurrentStatus(firstPassengerStatus);
            statusVo.setPassengers(passengers);
        } catch (Exception e) {
            throw new StatusException("Json response error", e, ErrorCodes.PARSE_ERROR);
        }
        return statusVo;
    }

    /**
     * This method is specific to responses from PNRStatus to parse json object from response string
     *
     * @param source
     * @return
     * @throws JSONException
     */
    private JSONObject getJsonObject(String source) throws JSONException {
        StringBuilder json = new StringBuilder();
        json.append('{');
        boolean elementStarted = false;
        boolean bracketStarted = false;
        char[] chars = source.toCharArray();
        for (char c : chars) {
            if (c == '[') {
                elementStarted = true;
                continue;
            }
            if (c == ']') {
                elementStarted = false;
                continue;
            }
            if (c == '(') {
                bracketStarted = true;
            }
            if (c == ')') {
                bracketStarted = false;
            }
            if (elementStarted && !bracketStarted && c == ',') {
                json.append(':');
                continue;
            }
            json.append(c);
        }
        json.append('}');

        // Create a JSONObject from the converted text
        JSONTokener tokener = new JSONTokener(json.toString());
        JSONObject object = (JSONObject) tokener.nextValue();
        return object;
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

    private static String getStubResponse() {
        return "{'Journey':[['Train Number','16670'],['Train Name','YERCAUD EXP'],['Boarding Date','23-03-2014'],"
                + "['From','SGE'],['To','MAS'],['Reserved Upto','MAS'],['Boarding Point','SGE'],['Class','SL']],"
                + "'Status':[['S. No.','Booking Status(Coach No , Berth No., Quota)','Current Status (Coach No , Berth No.)'],"
                + "['Passenger 1','S13 , 66,GN','CNF'],['Passenger 2','S13 , 69,GN','CNF']],'Legend':"
                + "{'CAN / MOD':'Cancelled or Modified Passenger','CNF / Confirmed':"
                + "'Confirmed (Coach/Berth number will be available after chart preparation)',"
                + "'RAC':'Reservation Against Cancellation','WL #':'Waiting List Number',"
                + "'RLWL':'Remote Location Wait List','GNWL':'General Wait List','PQWL':'Pooled Quota Wait List',"
                + "'REGRET/WL':'No More Booking Permitted','RELEASED':'Ticket Not Cancelled but Alternative Accommodation Provided'"
                + ",'R# #':'RAC Coach Number Berth Number'},'Charting':'CHART NOT PREPARED'}";
    }

}
