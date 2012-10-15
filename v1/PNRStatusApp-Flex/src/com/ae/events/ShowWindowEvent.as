package com.ae.events
{
import com.ae.vo.PnrNumberVO;
import com.ae.vo.ResultVO;

import flash.events.Event;

public class ShowWindowEvent extends Event
{
	//-------------------------------------------------------------------------
	//
	//	Constants
	//
	//-------------------------------------------------------------------------
	
	public static const SHOW_MORE_INFO:String = "showMoreInfo";
	public static const SHOW_ABOUT_WINDOW:String = "showAboutWindow";
	public static const SHOW_STATUS_HISTORY_WINDOW:String = "showStatusHistory";
	public static const SHOW_RESERVATION_HELPER:String = "showReservationHelper";
	
	//-------------------------------------------------------------------------
	//
	//	Variables
	//
	//-------------------------------------------------------------------------
	
	/**
	 * pnrnumbervo
	 */
	public var pnrNumberVO:PnrNumberVO;
	
	/**
	 * resultVo
	 */
	public var resultVO:ResultVO;
	
	//-------------------------------------------------------------------------
	//
	//  Constructor
	//
	//-------------------------------------------------------------------------
	public function ShowWindowEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
	{
		super(type, bubbles, cancelable);
	}
	
}
}