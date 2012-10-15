package com.ae.vo
{
/**
 * This VO represents a Reminder entry
 */
[Bindable]
public class ReminderVO
{	
	/**
	 * date
	 */
	
	private var _date:Date;
	
	public function set date(value:Date):void
	{
		this._date = value;
	}
	
	public function get date():Date
	{
		return this._date;
	}
	
	/**
	 * description 
	 */
	 
	private var _description:String;
	
	public function set description(value:String):void
	{
		this._description = value;
	}
	
	public function get description():String
	{
		return this._description;
	}

}
}