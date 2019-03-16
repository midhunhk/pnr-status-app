package com.ae.apps.pnrstatus.service.status;

import com.ae.apps.pnrstatus.exceptions.StatusException;
import com.ae.apps.pnrstatus.service.IStatusService;
import com.ae.apps.pnrstatus.service.NetworkService;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.Logger;
import com.ae.apps.pnrstatus.utils.PNRUtils;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ae.apps.pnrstatus.utils.AppConstants.TAG;

public class TrainPnrStatusService implements IStatusService {

    private static final String SERVICE_NAME = "TrainPnrStatusService";
    private static final String PARAM_PNR = "lccp_pnrno1";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String SERVICE_URL = "https://www.trainspnrstatus.com/pnrformcheck.php";
    private static final String PARAM_REFERER = "referer";
    private static final String PARAM_ORIGIN = "origin";
    private static final String REFERRER_URL = "https://www.trainspnrstatus.com/";
    private static final String ORIGIN_URL = "https://www.trainspnrstatus.com";
    private static final String SEPARATOR_COMMA = ",";
    private static final String SEPARATOR_SLASH = "/";

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    public PNRStatusVo getResponse(String pnrNumber) throws StatusException {
        // Create the headers and params for request
        HashMap<String, String> params = new HashMap<String, String>();
        HashMap<String, String> headers = new HashMap<String, String>();

        // Creating the headers
        headers.put(PARAM_REFERER, REFERRER_URL);
        headers.put(PARAM_ORIGIN, ORIGIN_URL);
        headers.put("dnt", "1");
        headers.put("content-type", "application/x-www-form-urlencoded");

        // Create the parameters for the request
        params.put(PARAM_PNR, pnrNumber);

        // invoke the post method and get the response
        String webResponse;
        try {
            webResponse = NetworkService
                    .getInstance()
                    .doPostRequest(SERVICE_URL, headers, params);
            if (webResponse == null) {
                throw new StatusException("responseObject is null", StatusException.ErrorCodes.EMPTY_RESPONSE);
            }
            Logger.d(TAG, webResponse);
        } catch (StatusException e) {
            throw e;
        } catch (Exception e) {
            throw new StatusException(e.getMessage(), e);
        }

        Logger.d(TAG, "WebResultResponse length : " + webResponse.length());

        return parseResponse(webResponse);
    }

    @Override
    public PNRStatusVo getResponse(String pnrNumber, Boolean stubResponse) throws StatusException {
        PNRStatusVo pnrStatusVo;
        if (stubResponse) {
            pnrStatusVo = parseResponse(getStubResponse());
        } else {
            pnrStatusVo = getResponse(pnrNumber);
        }
        if (pnrStatusVo != null) {
            pnrStatusVo.setPnrNumber(pnrNumber);
        }
        return pnrStatusVo;
    }

    private PNRStatusVo parseResponse(String html) throws StatusException {
        PNRStatusVo pnrStatusVo = new PNRStatusVo();
        List<String> elements;
        try {
            elements = PNRUtils.parseTrainPnrStatusResponse(html);
        } catch (Exception e) {
            // If PNR Number is invalid, we might get an exception while parsing
            throw new StatusException("Unable to Parse the response", StatusException.ErrorCodes.PARSE_ERROR);
        }

        Logger.d(TAG, "elements in parsed response : " + elements.size());

        int infoDataCount = 8;
        if (elements.size() > infoDataCount) {
            // Seems to be a valid ticket data
            String ticketClass = elements.get(3).trim();
            pnrStatusVo.setTrainNo(PNRUtils.getTrainNo(elements.get(0)));
            pnrStatusVo.setTrainName(elements.get(1));
            pnrStatusVo.setTrainJourneyDate(elements.get(2));
            // FromStation, ToStation, ReservedUpTo,BoardingPoint

            pnrStatusVo.setDestination(elements.get(5));
            pnrStatusVo.setEmbarkPoint(elements.get(6));
            pnrStatusVo.setBoardingPoint(elements.get(7));
            pnrStatusVo.setTicketClass(ticketClass);

            // Populate the passenger datas
            int passengersCount = (elements.size() - infoDataCount - 1) / 3;
            Logger.d(AppConstants.TAG, "passengersCount : " + passengersCount);

            int passengerDataIndex = infoDataCount;
            List<PassengerDataVo> passengersList = new ArrayList<PassengerDataVo>();
            for (int i = 1; i <= passengersCount; i++) {
                String currentStatus = elements.get(passengerDataIndex + 2);
                String bookingBerth = elements.get(passengerDataIndex + 1);

                // Create the PassengerDataVo
                PassengerDataVo passengerDataVo = new PassengerDataVo();
                passengerDataVo.setPassenger(elements.get(passengerDataIndex));
                passengerDataVo.setBookingBerth(bookingBerth);
                passengerDataVo.setCurrentStatus(currentStatus);

                // Try to calculate the berth position
                String berthPosition = "";
                try {
                    berthPosition = PNRUtils
                            .getBerthPosition(currentStatus, bookingBerth, ticketClass, SEPARATOR_SLASH);
                } catch (Exception e) {
                    Logger.e(TAG, "Exception in parseResponse() " + e.getMessage());
                }
                passengerDataVo.setBerthPosition(berthPosition);
                passengerDataVo.setBookingBerth(bookingBerth.trim());

                // Update some values in the main vo based on the first passenger
                if (i == 1) {
                    pnrStatusVo.setFirstPassengerData(passengerDataVo);
                    pnrStatusVo.setCurrentStatus(currentStatus);
                    pnrStatusVo.setTicketStatus(currentStatus);
                }
                passengersList.add(passengerDataVo);
                passengerDataIndex += 3;
            }
            String chartStatus = elements.get(elements.size() - 1);
            pnrStatusVo.setChartStatus(chartStatus);
            pnrStatusVo.setPassengers(passengersList);
        } else {
            throw new StatusException("Empty response from server", StatusException.ErrorCodes.EMPTY_RESPONSE);
        }
        Logger.d(TAG, "exit parseResponse()");
        return pnrStatusVo;
    }

    private static String getStubResponse() {
        return "<div id=\"pd\"><table class=\"table table-striped table-bordered\">" +
                "<tr><td colspan=\"4\">Journey Details</td></tr>" +
                "<tr><td>Train No</td><td>Train Name</td><td>Boarding Date<br>(DD-MM-YYYY)</td><td>Class</td></tr>" +
                "<tr><td><a href=\"http://www.trainspnrstatus.com/runningstatus/18118\">18118</a></td>" +
                "<td><a href=\"http://www.trainspnrstatus.com/runningstatus/18118\">RAJYA RANI EXP</a></td>" +
                "<td>11-03-2019</td><td>3A</td></tr>" +
                "<tr><td>From</td><td>To</td><td>Reserved Upto</td><td>Boarding Point</td></tr>" +
                "<tr><td>BBS</td><td>ROU</td><td>ROU</td><td>BBS</td></tr></table>" +
                "<table class=\"table table-striped table-bordered\"><tr><td>S.No.</td>" +
                "<td>*Current Status<br>(Coach No , Berth No.)</td><td>Quota</td></tr>" +
                "<tr><td><strong>Passenger 1</strong></td><td>CNF/B1/37</td><td>GN</td></tr><tr><td><strong>Passenger 2</strong></td>" +
                "<td>CNF/B1/38</td><td>GN</td></tr><tr><td><strong>Passenger 3</strong></td><td>CNF/B1/40</td><td>GN</td></tr><tr><td>Charting Status</td>" +
                "<td colspan=\"3\">CHART PREPARED</td></tr></table>" +
                "</div><script type=\"b5c3dbe14bb6240b598fd7af-text/javascript\">if(typeof(Storage)!==\"undefined\")" +
                "{var data=document.getElementById(\"pd\").innerHTML;localStorage.setItem(\"6526239691\", data);localStorage.setItem(\"date6526239691\", \"16-03-2019 09:04\");}</script></div>";
    }
}
