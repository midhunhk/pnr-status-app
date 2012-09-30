package com.ae.apps.pnrstatus.utils;

import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;

public class Utils {
	/**
	 * This method checks whether internet connectivity is available on the device
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isInternetAvailable(Context context) {
		// Request the Connectivity service to the os
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		// Check the current state of the Network Information
		if (networkInfo == null)
			return false;
		if (networkInfo.isConnected() == false)
			return false;
		if (networkInfo.isAvailable() == false)
			return false;
		return true;
	}

	/**
	 * 
	 */
	public static String[]	monthsArray	= { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
			"Nov", "Dec"				};

	public static String getDateWithMonthString(String dateString) {
		if (dateString != null && dateString.trim().length() > 0) {
			String tempArray[] = dateString.split("-");
			String monthStr = tempArray[1];
			int monthInt = Integer.valueOf(monthStr);
			String monthText = "-" + monthsArray[monthInt - 1];
			return dateString.replace("-" + monthStr, monthText);
		}
		return dateString;
	}

	/**
	 * Launches an Intent to open a webpage
	 * 
	 * @param context
	 * @param url
	 */
	public static void launchWebPage(Context context, String url) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		context.startActivity(i);
	}
	
	public static void shareStatus(Context context, PNRStatusVo pnrStatusVo)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		
		PassengerDataVo dataVo = pnrStatusVo.getFirstPassengerData();
		String shareSubject = "PNR Status for " + pnrStatusVo.getPnrNumber();
		String shareBody = "My PNR Status is " + dataVo.getTrainCurrentStatus() 
				+ " , " + dataVo.getTrainBookingBerth() + " #PNRStatusApp";
		
		// Put the extras
		intent.putExtra(Intent.EXTRA_TEXT, shareBody);
		intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
		
		// Start the chooser activity to let the user choose an app to share
		context.startActivity(Intent.createChooser(intent, "Share Via"));
	}

	/**
	 * Returns true if we are running in development mode
	 * 
	 * @return
	 */
	public static boolean isDevelopmentMode() {
		return Build.PRODUCT.equals(AppConstants.GOOGLE_SDK);
	}
}