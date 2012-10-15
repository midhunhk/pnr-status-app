package com.ae.events
{
import com.ae.vo.ResultVO;

import flash.events.Event;

/**
 * This event will be thrown by the StatusService class after
 * completing the call to the service.
 */
public class ServiceResultEvent extends Event
{
	//-------------------------------------------------------------------------
	//
	//	Constants
	//
	//-------------------------------------------------------------------------
	
	public static const SERVICE_RESULT:String = "serviceResult";
	
	public static const SERVICE_FAULT:String = "serviceFault";
	
	//-------------------------------------------------------------------------
	//
	//	Variables
	//
	//-------------------------------------------------------------------------
	public var resultVO:ResultVO;
	
	public var faultString:String;
	
	//-------------------------------------------------------------------------
	//
	//  Constructor
	//
	//-------------------------------------------------------------------------
	public function ServiceResultEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
	{
		super(type, bubbles, cancelable);
	}
	
}
}