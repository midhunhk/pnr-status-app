package com.ae.apps.pnrstatus.utils;

public class AppConstants {
	/**
	 * Constant used for log tag for the application
	 */
	public static final String	TAG								= "PNRStatusApp";

	public static String		APP_ACTION						= "action";
	public static String		PNR_NUMBER						= "pnr_number";
	public static String		ACTION_DELETE					= "actionDelete";
	public static String		ACTION_CHECK					= "actionCheck";
	public static String		PNR_STATUS_VO					= "pnrStatusVo";
	public static String		GOOGLE_SDK						= "google_sdk";

	/**
	 * Ticket Classes
	 */
	public static String		CLASS_SLEEPER					= "SL";
	public static String		CLASS_UNKNOWN					= "unknown";
	public static String		CLASS_AC2						= "AC2";
	public static String		CLASS_AC3						= "AC3";

	/**
	 * Ticket Status
	 */
	public static String		STATUS_CNF						= "CNF";
	public static String		STATUS_RAC						= "RAC";
	public static String		STATUS_WL						= "W/L";

	/**
	 * Number of seats in compartments
	 */
	public static int			SEATS_IN_SLEEPER_COMPARTMENT	= 8;
	public static int			SEATS_IN_AC2_COMPARTMENT		= 6;

	/**
	 * Berth Positions
	 */
	public static String		BERTH_LOWER						= "Lower";
	public static String		BERTH_MIDDLE					= "Middle";
	public static String		BERTH_UPPER						= "Upper";
	public static String		BERTH_SIDE_LOWER				= "Side Lower";
	public static String		BERTH_SIDE_UPPER				= "Side Upper";
	public static String		BERTH_DEFAULT					= "--";

	/**
	 * Request Methods
	 */
	public static String		METHOD_GET						= "GET";
	public static String		METHOD_POST						= "POST";
}
