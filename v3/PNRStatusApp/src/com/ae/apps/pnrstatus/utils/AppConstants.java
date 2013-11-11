/*
 * Copyright 2012 Midhun Harikumar
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

package com.ae.apps.pnrstatus.utils;

public interface AppConstants {
	/**
	 * Constant used for log tag for the application
	 */
	String	TAG								= "PNRStatusApp";

	String	APP_ACTION						= "action";
	String	PNR_NUMBER						= "pnr_number";
	String	ACTION_DELETE					= "actionDelete";
	String	ACTION_CHECK					= "actionCheck";
	String	PNR_STATUS_VO					= "pnrStatusVo";
	String	GOOGLE_SDK						= "google_sdk";
	String	APP_HASH_TAG					= "#PNRStatusApp";

	/**
	 * Ticket Classes
	 */
	String	CLASS_SLEEPER					= "SL";
	String	CLASS_UNKNOWN					= "unknown";
	String	CLASS_AC2						= "AC2";
	String	CLASS_AC3						= "AC3";

	/**
	 * Ticket Status
	 */
	String	STATUS_CNF						= "CNF";
	String	STATUS_RAC						= "RAC";
	String	STATUS_WL						= "W/L";

	/**
	 * Number of seats in compartments
	 */
	int		SEATS_IN_SLEEPER_COMPARTMENT	= 8;
	int		SEATS_IN_AC2_COMPARTMENT		= 6;

	/**
	 * Berth Positions
	 */
	String	BERTH_LOWER						= "Lower";
	String	BERTH_MIDDLE					= "Middle";
	String	BERTH_UPPER						= "Upper";
	String	BERTH_SIDE_LOWER				= "Side Lower";
	String	BERTH_SIDE_UPPER				= "Side Upper";
	String	BERTH_DEFAULT					= "--";

	/**
	 * Request Methods
	 */
	String	METHOD_GET						= "GET";
	String	METHOD_POST						= "POST";

}