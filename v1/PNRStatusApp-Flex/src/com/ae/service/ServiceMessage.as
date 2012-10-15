package com.ae.service
{

/**
 * The ServiceMessage class has constants for the messages
 * to be displayed after a web service call.
 */
public class ServiceMessage
{
	/**
	 * Specifies a JSON parse Error
	 */
	public static const ERROR_JSON_PARSE:String = 
		"An Error has occured while parsing the response. " + 
		"Is the PNR valid or is of an old ticket?";
	
	/**
	 * Specifies a webservice fault
	 */
	public static const ERROR_WEBSERVICE_FAULT:String = 
		"An error has occured while contacting the service." + 
		"Please try again after some time";

}
}