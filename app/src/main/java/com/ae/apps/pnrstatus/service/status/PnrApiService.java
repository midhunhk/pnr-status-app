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

import com.ae.apps.pnrstatus.exceptions.StatusException;
import com.ae.apps.pnrstatus.exceptions.StatusException.ErrorCodes;
import com.ae.apps.pnrstatus.service.IStatusService;
import com.ae.apps.pnrstatus.service.NetworkService;
import com.ae.apps.pnrstatus.utils.PNRUtils;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/alagu/pnrapi-ruby
 *
 * @author Midhun
 */
public class PnrApiService implements IStatusService {

    private static final String FIELD_NAME = "name";
    private static final String SERVICE_URL = "http://pnrapi.alagu.net/api/v1.0/pnr/";
    private static final String SERVICE_NAME = "PNRAPI";

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    public PNRStatusVo getResponse(String pnrNumber) throws StatusException {
        String searchUrl = getServiceUrl(pnrNumber);

        PNRStatusVo pnrStatusVo;
        try {
            String response = NetworkService.getInstance().doGetRequest(searchUrl);
            if (response == null) {
                throw new StatusException("responseObject is null", ErrorCodes.EMPTY_RESPONSE);
            }
            pnrStatusVo = parseResponse(response);
        } catch (Exception e) {
            throw new StatusException(e.getMessage(), e);
        }
        return pnrStatusVo;
    }

    @Override
    public PNRStatusVo getResponse(String pnrNumber, Boolean stubResponse) throws StatusException {
        PNRStatusVo statusVo;
        if (stubResponse == true) {
            // Return from the stub response
            statusVo = parseResponse(getStubResponse());
        } else {
            // Get the response from the server
            statusVo = getResponse(pnrNumber);
        }
        // Set the pnrnumber to the vo so that the ui can update the correct one in the list
        if (statusVo != null) {
            statusVo.pnrNumber = pnrNumber;
        }
        return statusVo;
    }

    private String getStubResponse() {
        String response = "{'status':'OK','data':{'train_number':'12623','chart_prepared':false,'pnr_number':'4448820672','train_name':'TRIVANDRUM MAIL','travel_date':{'timestamp':1366914600,'date':'26-4-2013'},'from':{'code':'MAS','name':'CHENNAI CENTRAL','time':'19:45'},'to':{'code':'KTYM','name':'KOTTAYAM','time':'07:35'},'alight':{'code':'KTYM','name':'KOTTAYAM','time':'07:35'},'board':{'code':'MAS','name':'CHENNAI CENTRAL','time':'19:45','timestamp':1366985700},'class':'SL','passenger':[{'seat_number':'RAC 108,GNWL','status':'Confirmed'}]}}";
        return response;
    }

    private String getServiceUrl(String pnrNumber) {
        if (null != pnrNumber && !pnrNumber.equals("")) {
            return SERVICE_URL + pnrNumber;
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

                String trainDest = dataObject.getJSONObject("alight").getString(FIELD_NAME);
                String trainName = dataObject.getString("train_name");
                String trainNo = PNRUtils.getTrainNo(dataObject.getString("train_number"));
                String trainBoard = dataObject.getJSONObject("board").getString(FIELD_NAME);
                String trainEmbark = dataObject.getJSONObject("to").getString(FIELD_NAME);
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
                statusVo.setTicketStatus(ticketStatus);
                statusVo.boardingPoint = trainBoard;
                statusVo.setDestination(trainDest);
                statusVo.setEmbarkPoint(trainEmbark);
                statusVo.setTrainJourneyDate(trainJourney);
                statusVo.trainName = trainName;
                statusVo.trainNo = trainNo;
                statusVo.setTicketClass(ticketClass);
                statusVo.setCurrentStatus(firstPassengerStatus);

                statusVo.passengers = (passengers);
            } else {
                throw new StatusException(ticketStatus);
            }
        } catch (JSONException exception) {
            throw new StatusException("Json response error", exception, ErrorCodes.PARSE_ERROR);
        }
        return statusVo;
    }

}