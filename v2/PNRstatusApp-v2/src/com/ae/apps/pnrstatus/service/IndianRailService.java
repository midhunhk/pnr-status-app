package com.ae.apps.pnrstatus.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.util.Log;

import com.ae.apps.pnrstatus.exception.StatusException;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.HttpUtils;
import com.ae.apps.pnrstatus.utils.Utils;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

public class IndianRailService implements IStatusService {

	/**
	 * The URL for Indian Rail Service 
	 */
	private final String url1 = "http://www.indianrail.gov.in/cgi_bin/inet_pnrstat_cgi.cgi";
	
	private final String serviceName = "Indian Rail";
	
	@Override
	public String getServiceName() {
		return serviceName;
	}

	public String getStubResponse() {
		String response = "<td colspan=\"9\" class=\"heading_table_top\">Journey Details</td></tr><TR class=\"heading_table\"><td width=\"11%\">" +
				"Train Number</Td><td width=\"16%\">Train Name</td><td width=\"18%\">Boarding Date <br>(DD-MM-YYYY)</td><td width=\"7%\">From</Td>" +
				"<td width=\"7%\">To</Td><td width=\"14%\">Reserved Upto</Td><td width=\"21%\">Boarding Point</Td><td width=\"6%\">Class</Td></TR>" +
				"<TR><TD class=\"table_border_both\">*12623</TD><TD class=\"table_border_both\">TRIVANDRUM MAIL</TD><TD class=\"table_border_both\">" +
				"14- 9-2012</TD><TD class=\"table_border_both\">MAS </TD><TD class=\"table_border_both\">ERN </TD><TD class=\"table_border_both\">" +
				"ERN </TD><TD class=\"table_border_both\">MAS </TD><TD class=\"table_border_both\"> SL</TD></TR></TABLE><BR /><TABLE align=\"center\">" +
				"<TR><TD><FORM NAME=\"RouteInfo\" METHOD=\"POST\" ACTION=\"http://www.indianrail.gov.in/cgi_bin/inet_trnpath_cgi.cgi\">" +
				"<INPUT TYPE=\"SUBMIT\" CLASS=\"btn_style\" VALUE=\"Get Schedule\" NAME=\"lccp_submitpath\"><INPUT TYPE=\"HIDDEN\" NAME=\"lccp_trn_no\" " +
				"SIZE=\"5\" VALUE=\"12623\"><INPUT  TYPE=\"HIDDEN\" NAME=\"lccp_month\" SIZE=\"2\" VALUE=\"9\"><INPUT  TYPE=\"HIDDEN\" NAME=\"lccp_day\" " +
				"SIZE=\"2\" VALUE=\"14\"><INPUT TYPE=\"HIDDEN\" NAME=\"lccp_daycnt\" SIZE=\"1\" VALUE=\"0\"></FORM></TD></TR></TABLE><TABLE " +
				"width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" class=\"table_border\" id=\"center_table\" ><TR><td width=\"25%\" " +
				"class=\"heading_table_top\">S. No.</td><td width=\"45%\" class=\"heading_table_top\">Booking Status <br /> (Coach No , Berth No., " +
				"Quota)</td><td width=\"30%\" class=\"heading_table_top\">* Current Status <br />(Coach No , Berth No.)</td></TR><TR><TD " +
				"class=\"table_border_both\"><B>Passenger 1</B></TD><TD class=\"table_border_both\"><B>S3,    24,GN  </B></TD><TD " +
				"class=\"table_border_both\"><B>CNF</B></TD></TR><TR><td class=\"heading_table_top\">Charting Status</td><TD colspan=\"3\" " +
				"align=\"middle\" valign=\"middle\" class=\"table_border_both\"> CHART NOT PREPARED </TD></TR><TR>";
		return response;
	}
	

	@Override
	public PNRStatusVo getResponse(String pnrNumber, Boolean stubResponse)
			throws JSONException, StatusException, IOException {
		if(stubResponse == true)
		{
			return parseResponse(getStubResponse());
		}
		return getResponse(pnrNumber);
	}
	
	@Override
	public PNRStatusVo getResponse(String pnrNumber) throws JSONException, StatusException,
			IOException {
		
		// Create the headers and params for request
		HashMap <String, String> params = new HashMap<String, String>();
		HashMap <String, String> headers = new HashMap<String, String>();
		
		headers.put("X-Alt-Referer", "http://www.indianrail.gov.in/pnr_Enq.html");
		
		params.put("lccp_pnrno1", pnrNumber);
		params.put("submitpnr", "Get+Status");
		
		// invoke the post method and get the response
		String webResponse = HttpUtils.postForm(url1, headers, params);
					
		Log.d(AppConstants.TAG, "WebResultResponse : " + webResponse.length());

		return parseResponse(webResponse);
	}
	
	/**
	 * Parse the response html and create the PNRStatusVo object
	 * @param html
	 * @return
	 */
	private PNRStatusVo parseResponse(String html)
	{
		PNRStatusVo pnrStatusVo = new PNRStatusVo();
		List<String> elements = Utils.parseIndianRailHtml(html);
		
		Log.d(AppConstants.TAG, "elements.size() : " + elements.size());
		
		int infoDataCount = 8;
		if(elements.size() > infoDataCount)
		{
			// Seems to be a valid ticket data
			String ticketClass = elements.get(7);
			pnrStatusVo.setTrainNo( Utils.getTrainNo( elements.get(0) ));
			pnrStatusVo.setTrainName(elements.get(1));
			pnrStatusVo.setTrainJourney(elements.get(2));
			pnrStatusVo.setTrainBoard(elements.get(6));
			pnrStatusVo.setTrainDest(elements.get(5));
			pnrStatusVo.setTrainEmbark(elements.get(3));
			pnrStatusVo.setTicketClass(ticketClass);
			
			// Populate the passenger datas
			int passengersCount = (elements.size() - infoDataCount - 1) / 3;
			Log.d(AppConstants.TAG, "passengersCount : " + passengersCount);
			
			List<PassengerDataVo> passengersList = new ArrayList<PassengerDataVo>();
			for(int i = 1; i <= passengersCount; i++)
			{
				int dataIndex = infoDataCount * i;
				String trainCurrentStatus = elements.get(dataIndex + 2);
				String trainBookingBerth = elements.get(dataIndex + 1) ;
				
				// Create the PassengerDataVo
				PassengerDataVo passengerDataVo = new PassengerDataVo();
				passengerDataVo.setTrainPassenger(elements.get(dataIndex ));
				passengerDataVo.setTrainBookingBerth(trainBookingBerth);
				passengerDataVo.setTrainCurrentStatus(trainCurrentStatus);
				
				// Try to calculate the berth position
				String berthPosition = "";
				try
				{
					berthPosition = Utils.getBerthPosition(trainCurrentStatus, trainBookingBerth, ticketClass);
				}
				catch (Exception e) 
				{
					Log.e(AppConstants.TAG, e.getMessage());
				}
				passengerDataVo.setBerthPosition(berthPosition);
				passengerDataVo.setTrainBookingBerth(trainBookingBerth.trim());
				
				// Update some values in the main vo based on the first passenger
				if(i == 1)
				{
					pnrStatusVo.setFirstPassengerData(passengerDataVo);
					pnrStatusVo.setTrainCurrentStatus(trainCurrentStatus);
					pnrStatusVo.setTicketStatus(trainCurrentStatus);
				}
				passengersList.add(passengerDataVo);
			}
			pnrStatusVo.setPassengers(passengersList);
		}
		return pnrStatusVo;
	}
	
}