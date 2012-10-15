package com.ae.events
{
import com.ae.vo.PnrNumberVO;
import com.ae.vo.ResultVO;
import com.ae.vo.StatusHistoryVO;

import flash.events.Event;

/**
 * This event can be used to add, remove or display the more info panel
 * for a pnr row
 */
public class PNRRowEvent extends Event
{
	//-------------------------------------------------------------------------
	//
	//	Constants
	//
	//-------------------------------------------------------------------------

	public static const ADD_PNR_ROW:String = "addPnrRow";	
	public static const SHOW_MORE_INFO:String = "showMoreInfo";	
	public static const REMOVE_PNR_ROW:String = "removePnrRow";
	public static const PNR_ROW_UPDATED:String = "pnrRowUpdated";
	public static const CLEAR_HISTORY_EVENT:String = "clearHistoryEvent";
		
	//-------------------------------------------------------------------------
	//
	//	Variables
	//
	//-------------------------------------------------------------------------
	
	public var resultVO:ResultVO;
	public var pnrNumberVO:PnrNumberVO;
	public var newPnrNumberVO:PnrNumberVO;
	public var statusHistoryVO:StatusHistoryVO;
	
	//-------------------------------------------------------------------------
	//
	//  Constructor
	//
	//-------------------------------------------------------------------------
	public function PNRRowEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
	{
		super(type, bubbles, cancelable);
	}
	
}
}