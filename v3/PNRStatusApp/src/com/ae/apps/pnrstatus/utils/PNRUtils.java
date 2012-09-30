package com.ae.apps.pnrstatus.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.ae.apps.pnrstatus.vo.MessageVo;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

public class PNRUtils {
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

		// If at the time of booking, status was W/L, then we can get the
		// berth status if only CHART IS PREPARED, at what time,
		// currentBookingStatus will be the berth data, if not return --
		if (trainBookingBerth.equals(AppConstants.STATUS_WL) && trainCurrentStatus.equals(AppConstants.STATUS_CNF)) {
			return berthPos;
		}

		// Data seems to be good to calculate the Berth position
		try {
			int modValue = 1;
			int berthNumber = Integer.parseInt(values[1].trim());
			values[0] = values[0].toUpperCase();
			if (values[0].charAt(0) == 'S') {
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

		} catch (Exception e) {
			Log.e(AppConstants.TAG, e.getMessage());
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
		List<String> elements = new ArrayList<String>();
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

	private static String	PNR	= "PNR";

	/**
	 * 
	 * @param messageVo
	 * @return
	 */
	public static PNRStatusVo parsePNRStatus(MessageVo messageVo) {
		PNRStatusVo pnrStatusVo = null;
		if (messageVo != null && messageVo.getMessage() != null) {
			String message = messageVo.getMessage();
			message = message.toUpperCase();

			if (message.startsWith(PNR)) {
				try {
					String[] contents = message.split(":");
					String trainJourney = contents[3].split(",")[0];

					pnrStatusVo = new PNRStatusVo();
					pnrStatusVo.setPnrNumber(contents[1].split(",")[0]);
					pnrStatusVo.setTrainNo(contents[2].split(",")[0]);
					pnrStatusVo.setTrainJourney(trainJourney);
					pnrStatusVo.setDateOfJourneyText(Utils.getDateWithMonthString(trainJourney));

					String[] tempData = contents[5].split(",");
					String ticketClass = tempData[1];
					String ticketStatus = tempData[4];
					// Here Compartment and seat are separated with a space
					String berthPosition = getBerthPosition(ticketStatus, ticketStatus, ticketClass, " ");

					pnrStatusVo.setCurrentStatus(ticketStatus);
					pnrStatusVo.setTicketClass(ticketClass);
					pnrStatusVo.setTicketStatus(ticketStatus);
					pnrStatusVo.setTrainBoard(tempData[2]);

					// Create a PassengerDataVo
					PassengerDataVo passengerDataVo = new PassengerDataVo();
					passengerDataVo.setTrainPassenger(tempData[3]);
					passengerDataVo.setTrainBookingBerth(tempData[4]);
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

}