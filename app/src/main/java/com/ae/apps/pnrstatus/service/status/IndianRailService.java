/*
 * Copyright 2014 Midhun Harikumar
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ae.apps.pnrstatus.utils.AppConstants.TAG;

/**
 * Indian Rail service
 *
 * @author midhun_harikumar
 */
public class IndianRailService implements IStatusService {

    private static final String SEPARATOR_COMMA = ",";
    private static final String PARAM_REFERER = "Referer";
    private static final String SUBMIT_VALUE = "Get+Status";
    private static final String PARAM_CAPTCHA_INPUT = "lccp_capinp_val";
    private static final String PARAM_CAPTCHA = "lccp_cap_val";
    private static final String PARAM_SUBMIT = "submitpnr";
    private static final String PARAM_PNR = "lccp_pnrno1";
    private static final String REFERRER_URL = "http://www.indianrail.gov.in/pnr_stat.html";
    // New Url
    // http://www.indianrail.gov.in/enquiry/PNR/PnrEnquiry.html?locale=en
    private static final String PNR_ENQ_URL = "http://www.indianrail.gov.in/pnr_Enq.html";
    private static final String SERVICE_NAME = "IndianRail";

    private static String mServiceUrl = null;

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
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

    @Override
    public PNRStatusVo getResponse(String pnrNumber) throws StatusException {
        Logger.d(TAG, "enter getResponse " + SERVICE_NAME);

        // Here, ahem we generate a random captcha for the server
        String randomCaptcha = getRandomCaptcha();

        // Create the headers and params for request
        HashMap<String, String> params = new HashMap<String, String>();
        HashMap<String, String> headers = new HashMap<String, String>();

        // Creating the headers
        headers.put(PARAM_REFERER, REFERRER_URL);

        // Create the parameters for the request
        params.put(PARAM_PNR, pnrNumber);
        params.put(PARAM_SUBMIT, SUBMIT_VALUE);
        params.put(PARAM_CAPTCHA, randomCaptcha);
        params.put(PARAM_CAPTCHA_INPUT, randomCaptcha);

        // invoke the post method and get the response
        String webResponse = null;
        try {
            if (mServiceUrl == null) {
                // we should fire a request to find the current url used for PNR Enquiry
                webResponse = NetworkService.getInstance().doGetRequest(PNR_ENQ_URL);
                mServiceUrl = getServiceUrl(webResponse);
            }
            // See if we got the url for accessing the service
            if (mServiceUrl == null) {
                throw new RuntimeException("service url is null for indian rail service");
            }

            // now, fire the request for finding the pnrstatus
            webResponse = NetworkService.getInstance().doPostRequest(mServiceUrl, headers, params);
            if (webResponse == null) {
                throw new StatusException("responseObject is null", ErrorCodes.EMPTY_RESPONSE);
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

    /**
     * Parse the response html and create the PNRStatusVo object
     *
     * @param html
     * @return
     * @throws StatusException
     */
    private PNRStatusVo parseResponse(String html) throws StatusException {
        PNRStatusVo pnrStatusVo = new PNRStatusVo();
        List<String> elements;
        try {
            elements = PNRUtils.parseIndianRailHtml(html);
        } catch (Exception e) {
            // If PNR Number is invalid, we might get an exception while parsing
            throw new StatusException("Unable to Parse the response", ErrorCodes.PARSE_ERROR);
        }

        Logger.d(TAG, "elements in parsed response : " + elements.size());

        int infoDataCount = 8;
        if (elements.size() > infoDataCount) {
            // Seems to be a valid ticket data
            String ticketClass = elements.get(7).trim();
            pnrStatusVo.setTrainNo(PNRUtils.getTrainNo(elements.get(0)));
            pnrStatusVo.setTrainName(elements.get(1));
            pnrStatusVo.setTrainJourneyDate(elements.get(2));
            pnrStatusVo.setBoardingPoint(elements.get(6));
            pnrStatusVo.setDestination(elements.get(4));
            pnrStatusVo.setEmbarkPoint(elements.get(5));
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
                            .getBerthPosition(currentStatus, bookingBerth, ticketClass, SEPARATOR_COMMA);
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
            throw new StatusException("Empty response from server", ErrorCodes.EMPTY_RESPONSE);
        }
        Logger.d(TAG, "exit parseResponse()");
        return pnrStatusVo;
    }

    /**
     * @param response
     * @return
     */
    private String getServiceUrl(String response) {
        // response =
        // "<form id=\"form3\" name=\"pnr_stat\" method=\"post\" action=\"http://www.indianrail.gov.in/cgi_bin/inet_pnstat_cgi_26163.cgi\" onsubmit=\"return checkform(this);\"> ";
        int startIndex = response.indexOf("action=") + 8;
        int endIndex = response.indexOf(".cgi", startIndex) + 4;
        return response.substring(startIndex, endIndex);
    }

    private String getRandomCaptcha() {
        return (Math.round(Math.random() * 89999) + 10000) + "";
    }

    private static String getStubResponse() {
        String response = "<td colspan=\"9\" class=\"heading_table_top\">Journey Details</td></tr><TR class=\"heading_table\"><td width=\"11%\">Train Number</Td>"
                + "<td width=\"16%\">Train Name</td><td width=\"18%\">Boarding Date <br>(DD-MM-YYYY)</td><td width=\"7%\">From</Td><td width=\"7%\">To</Td>"
                + "<td width=\"14%\">Reserved Upto</Td><td width=\"21%\">Boarding Point</Td><td width=\"6%\">Class</Td></TR><TR><TD class=\"table_border_both\">*17229</TD>"
                + "<TD class=\"table_border_both\">SABARI EXPRESS </TD><TD class=\"table_border_both\"> 6- 3-2014</TD><TD class=\"table_border_both\">ERN </TD><TD class=\"table_border_both\">SC  </TD>"
                + "<TD class=\"table_border_both\">SC  </TD><TD class=\"table_border_both\">ERN </TD><TD class=\"table_border_both\"> SL</TD></TR></TABLE><BR />"
                + "<TABLE align=\"center\"><TR><TD><FORM NAME=\"RouteInfo\" METHOD=\"POST\" ACTION=\"http://www.indianrail.gov.in/cgi_bin/inet_trnpath_cgi.cgi\">"
                + "<INPUT TYPE=\"SUBMIT\" CLASS=\"btn_style\" VALUE=\"Get Schedule\" NAME=\"lccp_submitpath\"><INPUT TYPE=\"HIDDEN\" NAME=\"lccp_trn_no\" SIZE=\"5\" VALUE=\"17229\">"
                + "<INPUT  TYPE=\"HIDDEN\" NAME=\"lccp_month\" SIZE=\"2\" VALUE=\"3\"><INPUT  TYPE=\"HIDDEN\" NAME=\"lccp_day\" SIZE=\"2\" VALUE=\"6\"><INPUT TYPE=\"HIDDEN\" NAME=\"lccp_daycnt\" SIZE=\"1\" VALUE=\"0\"></FORM></TD></TR></TABLE>"
                + "<TABLE width=\"100%\" border=\"0\"><tr><td align=\"left\"><font size=1><strong>Passenger current status updated time: 1-3-2014 19:42</strong></td></font></tr></table>"
                + "<TABLE width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" class=\"table_border\" id=\"center_table\" >"
                + "<TR><td width=\"25%\" class=\"heading_table_top\">S. No.</td><td width=\"45%\" class=\"heading_table_top\">Booking Status <br /> (Coach No , Berth No., Quota)</td>"
                + "<td width=\"30%\" class=\"heading_table_top\">* Current Status <br />(Coach No , Berth No.)</td>"
                + "</TR><TR><TD class=\"table_border_both\"><B>Passenger 1</B></TD>"
                + "<TD class=\"table_border_both\"><B>S10 ,  7,GN    </B></TD><TD class=\"table_border_both\"><B>   CNF  </B></TD></TR><TR><td class=\"heading_table_top\">Charting Status</td>"
                + "<TD colspan=\"3\" align=\"middle\" valign=\"middle\" class=\"table_border_both\"> CHART NOT PREPARED </TD></TR><TR>";
        return response;
    }
}