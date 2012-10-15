package com.ae.events
{
import flash.events.Event;

/**
 * This event is used for the WebService calls.
 */
public class ServiceEvent extends Event
{
	//-------------------------------------------------------------------------
	//
	//	Variables
	//
	//-------------------------------------------------------------------------
	
	public static const SEND:String = "send";
	
	public var params:String;
	
	public var pnr1:String;
	public var pnr2:String;
	
	//-------------------------------------------------------------------------
	//
	//	Constructor
	//
	//-------------------------------------------------------------------------
	public function ServiceEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
	{
		super(type, bubbles, cancelable);
	}
	
}
}