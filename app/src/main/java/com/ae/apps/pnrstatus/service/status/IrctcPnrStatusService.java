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
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.Logger;
import com.ae.apps.pnrstatus.utils.PNRUtils;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;

import java.util.HashMap;

/**
 * Unofficial service that consumes IRCTC PNR Status Service to fetch the PNR Status for us
 * 
 * @author Midhun
 *
 */
public class IrctcPnrStatusService implements IStatusService {

	private static final String	SERVICE_NAME	= "IRCTC-PNR-Status";
	private static final String	PARAM_PNR		= "pnr";
	private static final String	CONTENT_TYPE	= "Content-Type";
	private static final String	SERVICE_URL		= "http://irctc-pnr-status.com/status/pnr_data";
	
	@Override
	public String getServiceName() {
		return SERVICE_NAME;
	}

	@Override
	public PNRStatusVo getResponse(String pnrNumber) throws StatusException {
		Logger.i(AppConstants.TAG, "Using " + getServiceName());

		PNRStatusVo pnrStatusVo;

		// Create the headers and params for request
		HashMap<String, String> params = new HashMap();
		HashMap<String, String> headers = new HashMap();

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

	@Override
	public PNRStatusVo getResponse(String pnrNumber, Boolean stubResponse) throws StatusException {
		PNRStatusVo statusVo;
		if (stubResponse) {
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
	
	private PNRStatusVo parseResponse(String html) throws StatusException {
		return PNRUtils.parseIrctcPnrStatusResponse(html);
	}

	private static String getStubResponse() {
		return "<span class=\"hide_table\"><h2>PNR- : 4119806693</h2> <div class=\"table-responsive\">"
				+ "<table class=\"table table-striped table-hover hidden-xs\"><thead><tr><th colspan=\"3\">Journey Details</th>"
				+ "</tr><tr><th>Train Number</th><th>Train Name</th><th>Travel Date</th><th>From</th><th>To</th><th>Reserved Upto</th>"
				+ "<th>Boarding</th><th>Class</th></tr></thead><tbody><tr class=\"text-left\"><td>*12624</td><td>CHENNAI MAIL </td>"
				+ "<td>15- 6-2016</td><td>AWY </td><td>MAS </td><td>MAS </td><td>AWY </td><td> 3A</td></tr></tbody></table>"
				+ "</div><table class=\"ng-table responsive table-td-height1 hidden-lg hidden-md hidden-sm visible-xs\"><tbody>"
				+ "<tr><th colspan=\"3\" style=\"padding:5px\">Journey Details</th></tr><tr>"
				+ "<td data-content=\"Train Number\" class=\"small-padding tdno0\">*12624</td>"
				+ "<td data-content=\"Train Name\" class=\"small-padding tdno0\">CHENNAI MAIL </td>"
				+ "<td data-content=\"Boarding Date\" class=\"small-padding tdno0\">15- 6-2016</td>"
				+ "<td data-content=\"From\" class=\"small-padding tdno0\">AWY </td><td data-content=\"To\" class=\"small-padding tdno0\">MAS </td>"
				+ "<td data-content=\"Reserved Upto\" class=\"small-padding tdno0\">MAS </td>"
				+ "<td data-content=\"Boarding Point\" class=\"small-padding tdno0\">AWY </td>"
				+ "<td data-content=\"Class\" class=\"small-padding tdno0\"> 3A</td></tr></tbody>"
				+ "</table><hr> <table class=\"table table-striped table-hover hidden-xs\"><thead>"
				+ "<tr><th>S.No</th><th>Booking Status (Coach No, Berth No., Quota)</th><th>Current Status</th></tr>"
				+ "</thead><tbody><tr class=\"text-left\"><td>Passenger (1)</td><td text=\"center\">B4 , 15,GN</td>"
				+ "<td text=\"center\">CNF</td></tr><tr class=\"text-left\"><td>Passenger (2)</td><td text=\"center\">B4 , 10,GN</td>"
				+ "<td text=\"center\">CNF</td></tr><tr class=\"text-left\"><td>Passenger (3)</td><td text=\"center\">B4 , 11,GN</td>"
				+ "<td text=\"center\">CNF</td></tr><tr class=\"text-left\"><td>Passenger (4)</td><td text=\"center\">B4 , 13,GN</td>"
				+ "<td text=\"center\">CNF</td></tr><tr class=\"text-left\"><td>Passenger (5)</td><td text=\"center\">B4 , 14,GN</td>"
				+ "<td text=\"center\">CNF</td></tr></tbody></table>"
				+ "<table class=\"ng-table responsive table-td-height hidden-lg hidden-md hidden-sm visible-xs\"><tbody>"
				+ "<tr class=\"table-bg-color1\"><td data-content=\"S.No\" class=\"small-padding tdno0\">Passenger (1)</td>"
				+ "<td data-content=\"Booking Status (Coach No, Berth No., Quota)\" class=\"small-padding tdno1\">B4 , 15,GN</td>"
				+ "<td data-content=\"Current Status\" class=\"small-padding tdno2\">CNF</td></tr><tr class=\"table-bg-color\">"
				+ "<td data-content=\"S.No\" class=\"small-padding tdno0\">Passenger (2)</td>"
				+ "<td data-content=\"Booking Status (Coach No, Berth No., Quota)\" class=\"small-padding tdno1\">B4 , 10,GN</td>"
				+ "<td data-content=\"Current Status\" class=\"small-padding tdno2\">CNF</td></tr><tr class=\"table-bg-color1\">"
				+ "<td data-content=\"S.No\" class=\"small-padding tdno0\">Passenger (3)</td>"
				+ "<td data-content=\"Booking Status (Coach No, Berth No., Quota)\" class=\"small-padding tdno1\">B4 , 11,GN</td>"
				+ "<td data-content=\"Current Status\" class=\"small-padding tdno2\">CNF</td></tr><tr class=\"table-bg-color\">"
				+ "<td data-content=\"S.No\" class=\"small-padding tdno0\">Passenger (4)</td>"
				+ "<td data-content=\"Booking Status (Coach No, Berth No., Quota)\" class=\"small-padding tdno1\">B4 , 13,GN</td>"
				+ "<td data-content=\"Current Status\" class=\"small-padding tdno2\">CNF</td></tr><tr class=\"table-bg-color1\">"
				+ "<td data-content=\"S.No\" class=\"small-padding tdno0\">Passenger (5)</td>"
				+ "<td data-content=\"Booking Status (Coach No, Berth No., Quota)\" class=\"small-padding tdno1\">B4 , 14,GN</td>"
				+ "<td data-content=\"Current Status\" class=\"small-padding tdno2\">CNF</td></tr></tbody></table><div class=\"second-adson\">"
				+ "<div class=\"show-table-index\"><p><ins class=\"adsbygoogle\" style=\"display:block\" data-ad-client=\"ca-pub-6060503107075904\" data-ad-slot=\"2029427823\" data-ad-format=\"auto\"></ins></p>"
				+ "</div></div> </span>";
	}

}
