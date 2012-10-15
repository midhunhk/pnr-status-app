package com.ae.events
{
import com.ae.vo.ReminderVO;

import flash.events.Event;

/**
 * This event can be used to add, remove or display the more info panel
 * for a reminder
 */
public class RemindersEvent extends Event
{
	//-------------------------------------------------------------------------
	//
	//	Constants
	//
	//-------------------------------------------------------------------------

	public static const ADD_REMINDER:String = "addReminder";	
	public static const REMOVE_REMINDER:String = "removeReminder";	
	public static const UPDATE_REMINDER:String = "updateReminder";
		
	//-------------------------------------------------------------------------
	//
	//	Variables
	//
	//-------------------------------------------------------------------------
	
	public var reminderVo:ReminderVO;
	public var newReminderVo:ReminderVO;
	
	//-------------------------------------------------------------------------
	//
	//  Constructor
	//
	//-------------------------------------------------------------------------
	public function RemindersEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
	{
		super(type, bubbles, cancelable);
	}
	
}
}