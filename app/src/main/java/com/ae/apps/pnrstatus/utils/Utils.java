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

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.ae.apps.pnrstatus.R;
import com.ae.apps.pnrstatus.adapters.StackAdapter;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
	/**
	 * This method checks whether Internet connectivity is available on the device
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isInternetAvailable(Context context) {
		// Request the Connectivity service to the OS
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		// Check the current state of the Network Information
		if (networkInfo == null)
			return false;
		if (!networkInfo.isConnected())
			return false;
		if (!networkInfo.isAvailable())
			return false;
		return true;
	}

	public static String[]	monthsArray		= { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
			"Nov", "Dec"					};
	public static String[]	dayNamesArray	= { "", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
			"Saturday"						};

	/**
	 * Returns a string with Month name instead of month number
	 * 
	 * @param dateString
	 * @return
	 */
	public static String getDateWithMonthString(String dateString) {
		if (dateString != null && dateString.trim().length() > 0) {
			String[] tempArray = dateString.split("-");
			String monthStr = tempArray[1];
			int monthInt = Integer.parseInt(monthStr);
			String monthText = "-" + monthsArray[monthInt - 1];
			return dateString.replace("-" + monthStr, monthText);
		}
		return dateString;
	}

	private static String	MESSAGE_DATE_FORMAT	= "dd-M-yy";

	/**
	 * Returns the Day name for the day represented by this timestamp
	 * 
	 * @return
	 */
	public static String getDayName(String dateString) {
		String dayOfWeek = "";
		if (dateString != null && dateString.trim().length() > 0) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(MESSAGE_DATE_FORMAT, Locale.getDefault());
				Date date = dateFormat.parse(dateString);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				dayOfWeek = dayNamesArray[calendar.get(Calendar.DAY_OF_WEEK)];
			} catch (ParseException e) {
			}
		}
		return dayOfWeek;
	}

	public static long getTimeStampFromDateString(String dateString) {
		long timestamp = 0;
		if (dateString != null && dateString.trim().length() > 0) {
			timestamp = getCalendarFromDateString(dateString).getTimeInMillis();
		}
		return timestamp;
	}

	public static Calendar getCalendarFromDateString(String dateString) {
		Calendar calendar = Calendar.getInstance();
		if (dateString != null && dateString.trim().length() > 0) {
			String[] tempArray = dateString.split("-");
			calendar.set(Integer.parseInt(tempArray[2]), Integer.parseInt(tempArray[1]) - 1,
					Integer.parseInt(tempArray[0]));
		}
		return calendar;
	}

	/**
	 * Launches an Intent to open a web page
	 * 
	 * @param context
	 * @param url
	 */
	public static void launchWebPage(Context context, String url) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		context.startActivity(i);
	}

	public static void shareStatus(Context context, PNRStatusVo pnrStatusVo) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");

		PassengerDataVo dataVo = pnrStatusVo.getFirstPassengerData();
		String shareSubject = context.getResources().getString(R.string.str_share_status_subject,
				pnrStatusVo.pnrNumber);
		String shareBody = context.getResources().getString(R.string.str_share_status_detail,
				dataVo.getCurrentStatus(), dataVo.getBookingBerth())
				+ AppConstants.APP_HASH_TAG;

		// Put the extras
		intent.putExtra(Intent.EXTRA_TEXT, shareBody);
		intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);

		// Start the chooser activity to let the user choose an app to share
		String shareVia = context.getResources().getString(R.string.str_share_via);
		context.startActivity(Intent.createChooser(intent, shareVia));
	}

	/**
	 * Returns true if we are running in on emulator
	 * 
	 * @return
	 */
	public static boolean isDevelopmentMode() {
		return Build.PRODUCT.equals(AppConstants.GOOGLE_SDK);
	}

	/**
	 * Return an Adapter View implementation based on the platform version
	 * 
	 * @param context
	 * @param adapter
	 * @return
	 */
	public static AdapterView<?> getAdapterView(Context context, StackAdapter adapter) {
		AdapterView<?> adapterView = null;
		android.widget.StackView stackView = new android.widget.StackView(context);
		stackView.setAdapter(adapter);
		adapterView = stackView;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		adapterView.setLayoutParams(params);
		return adapterView;
	}

	/**
	 * Copy some text to the System clip board
	 * 
	 * @param context
	 * @param data
	 */
	@SuppressWarnings("deprecation")
	public static void copyTextToClipboard(Context context, String data) {
		android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clipData = ClipData.newPlainText("pnrnumber", data);
		clipboard.setPrimaryClip(clipData);
		Toast.makeText(context, R.string.str_pnr_copied_to_clip, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Returns a system style for a dialog based on the OS version
	 * 
	 * @return
	 */
	public static int getDialogStyle() {
		return android.R.style.Theme_Holo_Dialog_NoActionBar;
	}

	public static int getDialogWithTitleStyle() {
		return android.R.style.Theme_Holo_Dialog;
	}

}