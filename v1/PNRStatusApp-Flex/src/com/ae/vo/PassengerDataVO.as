package com.ae.vo
{

/**
 * The VO for PassengerData
 */
[Bindable]
public class PassengerDataVO
{
	/**
	 * Passenger Name, will be Passenger1 and so on only
	 */
	private var _trainPassenger:String;
	
	public function set trainPassenger(value:String):void
	{
		_trainPassenger = value;
	}
	
	public function get trainPassenger():String
	{
		return _trainPassenger;
	}
	
	/**
	 * Berth at Booking Status
	 */
	private var _trainBookingBerth:String;
	
	public function set trainBookingBerth(value:String):void
	{
		_trainBookingBerth = value;
	}
	
	public function get trainBookingBerth():String
	{
		return _trainBookingBerth;
	}
	
	/**
	 * Current Status of berth
	 */
	private var _trainCurrentStatus:String;
	
	public function set trainCurrentStatus(value:String):void
	{
		_trainCurrentStatus = value;
	}
	
	public function get trainCurrentStatus():String
	{
		return _trainCurrentStatus;
	}
	
	/**
	 * The berth position, Additional data added
	 */
	private var _berthPosition:String;
	
	public function set berthPosition(value:String):void
	{
		_berthPosition = value;
	}
	
	public function get berthPosition():String
	{
		return _berthPosition;
	}

}
}