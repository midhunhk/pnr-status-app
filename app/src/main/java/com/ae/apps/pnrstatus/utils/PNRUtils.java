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

package com.ae.apps.pnrstatus.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.util.Log;

import com.ae.apps.pnrstatus.vo.MessageVo;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

/**
 * Utility methods that deal with PNR Numbers and related scenarios
 * 
 * @author midhun_harikumar
 * 
 */
public class PNRUtils {
	private static final String	TD_END	= "</td>";
	private static final String	TD_START	= "<td";
	private static final String TABLE_START = "<table";
	private static final String TABLE_END = "</table>";

	/**
	 * Connects with the url as a GET request and retrieves the response
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String getWebResult(String url) throws IOException {
		return getWebResult(url, AppConstants.METHOD_GET);
	}

	/**
	 * Connects with the url as a required type and retrieves the response.
	 * 
	 * @param url
	 * @param requestMethod
	 * @return
	 * @throws IOException
	 */
	public static String getWebResult(String url, String requestMethod) throws IOException {
		String line = null;
		StringBuilder sb = null;
		String strQueryResponse = null;
		HttpURLConnection urlConnection = null;

		// Make a connection to the url
		urlConnection = (HttpURLConnection) new URL(url).openConnection();
		urlConnection.setRequestMethod(requestMethod);
		urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		// urlConnection.setRequestProperty("Content-Language", "en-US");
		// urlConnection.setDoInput(true);
		urlConnection.setConnectTimeout(10000);
		urlConnection.connect();

		// Read data from the resource pointed by the urlConnection
		InputStream in = urlConnection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		sb = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}

		// Close the reader and disconenct the urlConnection
		reader.close();
		urlConnection.disconnect();

		strQueryResponse = sb.toString();
		return strQueryResponse;
	}

	/**
	 * 
	 * @param trainCurrentStatus
	 * @param trainBookingBerth
	 * @return
	 */
	public static String getBerthPosition(String trainCurrentStatus, String trainBookingBerth, String ticketClass,
			String splitChar) {
		String[] values;
		String berthPos = AppConstants.BERTH_DEFAULT;
		String[] array1 = trainCurrentStatus.split(splitChar);
		String[] array2 = trainBookingBerth.split(splitChar);

		if (array2.length > 2 && !trainBookingBerth.equals(AppConstants.STATUS_WL)) {
			values = array2;
		} else if (array1.length > 1 && !trainCurrentStatus.equals(AppConstants.STATUS_WL)
				&& !trainCurrentStatus.equals(AppConstants.STATUS_RAC)) {
			values = array1;
		} else {
			return berthPos;
		}

		// If at the time of booking, status was W/L, then we can get the berth status if only CHART IS PREPARED,
		// at what time, currentBookingStatus will be the berth data, if not return --
		if (trainBookingBerth.equals(AppConstants.STATUS_WL) && trainCurrentStatus.equals(AppConstants.STATUS_CNF)) {
			return berthPos;
		}

		if (trainBookingBerth.contains(AppConstants.STATUS_RAC)) {
			return berthPos;
		}

		// Data seems to be good to calculate the Berth position
		try {
			if (null != values[1] && values[1].trim().length() > 0) {
				int modValue = 1;
				int berthNumber = Integer.valueOf(values[1].trim());
				if (values[0].toUpperCase(Locale.getDefault()).charAt(0) == 'S') {
					modValue = berthNumber % AppConstants.SEATS_IN_SLEEPER_COMPARTMENT;
				}
				if (values[0].toUpperCase(Locale.getDefault()).charAt(0) == 'B'
						&& ticketClass.toUpperCase(Locale.getDefault()).equals("3A")) {
					// ticket is for 3rd ac compartment
					modValue = berthNumber % AppConstants.SEATS_IN_SLEEPER_COMPARTMENT;
				}

				switch (modValue) {
				case 0:
					berthPos = AppConstants.BERTH_SIDE_UPPER;
					break;
				case 7:
					berthPos = AppConstants.BERTH_SIDE_LOWER;
					break;
				case 1:
				case 4:
					berthPos = AppConstants.BERTH_LOWER;
					break;
				case 2:
				case 5:
					berthPos = AppConstants.BERTH_MIDDLE;
					break;
				case 3:
				case 6:
					berthPos = AppConstants.BERTH_UPPER;
				}
			}
		} catch (Exception e) {
			Logger.e(AppConstants.TAG, "PNRUtils " + e.getMessage());
		}

		return berthPos;
	}

	/**
	 * This method removes the * from the train number
	 */
	public static String getTrainNo(String sourceString) {
		int index = sourceString.indexOf('*');
		if (index > -1) {
			return sourceString.substring(index + 1);
		}
		return sourceString;
	}

	// -------------------------------------------------------------------------
	// Function to parse the html returned by indianrailinfo; and create a list
	// of required data that we will use to create the data objects
	// -------------------------------------------------------------------------

	private static String	MATCH_START			= "table_border_both";
	private static String	MATCH_BODY			= "<BODY>";
	private static String	MATCH_START_CLOSE	= ">";
	private static String	MATCH_END			= "</TD>";
	private static String	BOLD_START			= "<B>";
	private static String	BOLD_END			= "</B>";
	private static String	IGNORE_TEXT			= "<caption";

	/**
	 * Returns a list of elements corresponding to rows in the html. Tightly coupled to indianrail.info site
	 * 
	 * @param html
	 * @return
	 */

	public static List<String> parseIndianRailHtml(String html) {
		int endPos = 0;
		List<String> elements = new ArrayList();
		// Find the location of <body> tag
		int startPos = html.indexOf(MATCH_BODY);

		if (html != null && html.length() > 0) {
			// Find the start position of the first data tag
			startPos = html.indexOf(MATCH_START, startPos);

			while (startPos > -1) {
				// Calculate the end of the tag and the start of the data part
				endPos = html.indexOf(MATCH_END, startPos);
				int startClosePos = html.indexOf(MATCH_START_CLOSE, startPos);
				int dataStartIndex = startClosePos + MATCH_START_CLOSE.length();

				// Extract the data part that we need
				String buffer = html.substring(dataStartIndex, endPos);
				if (buffer.indexOf(BOLD_START) > -1) {
					buffer = buffer.substring(BOLD_START.length(), buffer.length() - BOLD_END.length());
				}
				if (buffer.indexOf(IGNORE_TEXT) == -1) {
					elements.add(buffer);
					Log.d(AppConstants.TAG, "Parsed Element Value : " + buffer);
				}

				// Update the startPosition value for the next iteration
				startPos = html.indexOf(MATCH_START, dataStartIndex);
			}

		}
		return elements;
	}

	public static List<String> parseTrainPnrStatusResponse(final String html){
		final String DATA_BLOCK_STYLE = "table table-striped table-bordered";
        List<String> elements = new ArrayList();
        if(null != html && html.length() > 0) {
            int startIndex = html.indexOf(DATA_BLOCK_STYLE);
            int endIndex = html.indexOf(TABLE_END, startIndex);

            String journeyDetails = html.substring(startIndex, endIndex);
            List<String> journeyDetailsList = getCellDataAsItems(journeyDetails);
            if(journeyDetailsList.isEmpty() || journeyDetailsList.size() != 17){
                return elements;
            }
            // TrainNo, TrainName, TravelDate, TicketClass
            elements.add(getInnerValue( journeyDetailsList.get(5), "a"));
            elements.add(getInnerValue( journeyDetailsList.get(6), "a"));
            elements.add(journeyDetailsList.get(7));
            elements.add(journeyDetailsList.get(8));
            // FromStation, ToStation, ReservedUpTo,BoardingPoint
            elements.add(journeyDetailsList.get(13));
            elements.add(journeyDetailsList.get(14));
            elements.add(journeyDetailsList.get(15));
            elements.add(journeyDetailsList.get(16));

            // Process passengers table
            startIndex = html.indexOf(DATA_BLOCK_STYLE, endIndex);
            endIndex = html.indexOf(TABLE_END, startIndex);

            String passengerDetails = html.substring(startIndex, endIndex);
            List<String> passengerDetailsList = getCellDataAsItems(passengerDetails);
            int NUM_HEADER_ROWS = 3;
            int NUM_TRAILER_ROWS = 2;
            int NUM_ROWS_PER_PASSENGER = 3;
            int numPassengers = (passengerDetailsList.size() - NUM_HEADER_ROWS - NUM_TRAILER_ROWS) / NUM_ROWS_PER_PASSENGER;
            int offset;
            for(int i = 0; i < numPassengers; i++) {
                offset = NUM_HEADER_ROWS + (i * NUM_ROWS_PER_PASSENGER);
                elements.add(getInnerValue(passengerDetailsList.get( offset ), "strong"));
                elements.add(passengerDetailsList.get( offset + 1));
                elements.add(passengerDetailsList.get( offset + 2));
            }
            // ChartPrepareStatus
            int last = NUM_HEADER_ROWS + numPassengers * NUM_ROWS_PER_PASSENGER + 1;
            elements.add( passengerDetailsList.get(last) );
        }
        return elements;
	}
	
	public static PNRStatusVo parseIrctcPnrStatusResponse(String html) {
		PNRStatusVo pnrStatusVo = null;
		if(null != html && html.length() > 0){
			pnrStatusVo = new PNRStatusVo();
			// First table has Journey Details
			int startIndex = html.indexOf("<tbody>");
			int endIndex = html.indexOf("</tbody>");
			
			String journeyDetails = html.substring(startIndex, endIndex);
			List<String> journeyDetailsList = getCellDataAsItems(journeyDetails);
			
			if(journeyDetailsList.size() > 7){
				pnrStatusVo.setTrainNo(getTrainNo(journeyDetailsList.get(0)));
				pnrStatusVo.setTrainName(journeyDetailsList.get(1));
				pnrStatusVo.setDateOfJourneyText(journeyDetailsList.get(2));
				pnrStatusVo.setTrainJourneyDate(journeyDetailsList.get(2));
				pnrStatusVo.setDestination(journeyDetailsList.get(4));
				pnrStatusVo.setEmbarkPoint(journeyDetailsList.get(5));
				pnrStatusVo.setBoardingPoint(journeyDetailsList.get(6));
				pnrStatusVo.setTicketClass(journeyDetailsList.get(7));
			}
			
			// We need to skip a <table> to reach the Passenger Details
			int passengerTable = html.indexOf("Booking Status", endIndex);
			
			startIndex = html.indexOf("<tbody>", passengerTable);
			endIndex = html.indexOf("</tbody>", passengerTable);
			
			String passengerDetails = html.substring(startIndex, endIndex);
			// each <tr represents a passenger
			
			int passengerStartIndex = passengerDetails.indexOf("<tr");
			int passengerEndIndex = 0;
			List<String> passengerDetailsList = null;
			
			List<PassengerDataVo> passengers = new ArrayList<PassengerDataVo>();
			pnrStatusVo.setPassengers(passengers);
			PassengerDataVo passengerDataVo = null;
			while(passengerStartIndex > -1){
				passengerEndIndex = passengerDetails.indexOf("</tr>", passengerStartIndex);
				passengerDetailsList = getCellDataAsItems(passengerDetails.substring(passengerStartIndex, passengerEndIndex));
				if(passengerDetailsList.size() > 2){
					passengerDataVo = new PassengerDataVo();
					passengerDataVo.setPassenger(passengerDetailsList.get(0));
					passengerDataVo.setBerthPosition(passengerDetailsList.get(1));
					passengerDataVo.setCurrentStatus(passengerDetailsList.get(2));
					passengerDataVo.setBookingBerth(passengerDetailsList.get(2));
					passengers.add(passengerDataVo);
				}
				passengerStartIndex = passengerDetails.indexOf("<tr", passengerEndIndex);
			}
			
			if(!passengers.isEmpty()){
				pnrStatusVo.setFirstPassengerData(passengers.get(0));
				pnrStatusVo.setCurrentStatus(passengers.get(0).getCurrentStatus());
			}
		}
		return pnrStatusVo;
	}

	private static String getInnerValue(final String formatted, final String element){
	    int startIndex = formatted.indexOf("<" + element);
	    if(startIndex > -1){
	        startIndex = formatted.indexOf(">");
	        int endIndex = formatted.indexOf("</" + element + ">");
	        return formatted.substring(startIndex + 1, endIndex);
        }
        return formatted;
    }

	private static List<String> getCellDataAsItems(String journeyDetails) {
		List<String> journeyDetailsList = new ArrayList<>();
		int itemStartIndex = journeyDetails.indexOf(TD_START);
		int itemEndIndex;
		String data;
		while(itemStartIndex > -1){
 			itemStartIndex = journeyDetails.indexOf(">", itemStartIndex);
			itemEndIndex = journeyDetails.indexOf(TD_END, itemStartIndex);
			data = journeyDetails.substring(itemStartIndex + 1, itemEndIndex);
			journeyDetailsList.add(data);
			itemStartIndex = journeyDetails.indexOf(TD_START, itemEndIndex);
		}
		return journeyDetailsList;
	}

	private static String	PNR	= "PNR";

	/**
	 * Parse a message vo and convert to PNRSatus Vo
	 * 
	 * @param messageVo
	 * @return
	 */
	public static PNRStatusVo parsePNRStatus(MessageVo messageVo) {
		PNRStatusVo pnrStatusVo = null;
		if (messageVo != null && messageVo.getMessage() != null) {
			String message = messageVo.getMessage();
			message = message.toUpperCase(Locale.getDefault());

			if (message.startsWith(PNR)) {
				try {
					String[] contents = message.split(":");
					String trainJourney = contents[3].split(",")[0];
					String dateWithMonthText = Utils.getDateWithMonthString(trainJourney);
					long timeStamp = Utils.getTimeStampFromDateString(trainJourney);

					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(timeStamp);

					pnrStatusVo = new PNRStatusVo();
					pnrStatusVo.setPnrNumber(contents[1].split(",")[0]);
					pnrStatusVo.setTrainNo(contents[2].split(",")[0]);
					pnrStatusVo.setTrainJourneyDate(trainJourney);
					pnrStatusVo.setDateOfJourneyText(dateWithMonthText + " (" + Utils.getDayName(trainJourney) + ")");
					pnrStatusVo.setJourneyDateTimeStamp(timeStamp);

					String[] tempData = contents[5].split(",");
					String[] tempData3 = contents[3].split(",");
					String ticketClass = tempData3[1];

					// ticket status value not coming
					String ticketStatus = ""; // tempData[4];
					// Here Compartment and seat are separated with a space
					String berthPosition = getBerthPosition(ticketStatus, ticketStatus, ticketClass, " ");

					pnrStatusVo.setCurrentStatus(ticketStatus);
					pnrStatusVo.setTicketClass(ticketClass);
					pnrStatusVo.setTicketStatus(ticketStatus);
					pnrStatusVo.setBoardingPoint(tempData3[2]);

					// Create a PassengerDataVo
					PassengerDataVo passengerDataVo = new PassengerDataVo();
					passengerDataVo.setPassenger(tempData[1]);
					passengerDataVo.setBookingBerth(tempData[2]);
					passengerDataVo.setBerthPosition(berthPosition);

					// Set as first passenger and place in the list of passengers also
					List<PassengerDataVo> passengersList = new ArrayList<PassengerDataVo>();
					passengersList.add(passengerDataVo);

					pnrStatusVo.setFirstPassengerData(passengerDataVo);
					pnrStatusVo.setPassengers(passengersList);
				} catch (Exception exception) {
					return null;
				}
			}
		}
		return pnrStatusVo;
	}

	/**
	 * Beautifies the PNRNumber string with additional spacing for readability
	 * 
	 * @param boringNum
	 * @return
	 */
	public static String formatPNRString(String boringNum) {
		if (boringNum != null && boringNum.length() == 10) {
			return boringNum.substring(0, 3) + " " + boringNum.substring(3, 6) + " " + boringNum.substring(6, 10);
		}
		return boringNum;
	}

	/**
	 * Returns an empty PNRStatusVo Object
	 * 
	 * @return
	 */
	public static PNRStatusVo getEmptyPNRStatusObject() {
		PNRStatusVo statusVo = new PNRStatusVo();
		statusVo.setCurrentStatus("");
		statusVo.setFirstPassengerData(null);
		statusVo.setPassengers(null);

		return statusVo;
	}

}