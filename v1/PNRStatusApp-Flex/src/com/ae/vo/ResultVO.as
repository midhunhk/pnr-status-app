package com.ae.vo
{
	import mx.collections.ArrayCollection;
	
/**
 * This VO represents the data that is returned from the result
 */
[Bindable]
public class ResultVO
{
	//---------------------------------
	// Properties
	//---------------------------------
	
	/**
	 * @private
	 */
	
	private var _statusData:StatusDataVO;
	
	private var _passengerDataColl:ArrayCollection;
	
	//---------------------------------
	// Accessor functions
	//---------------------------------
	
	/**
	 * Passenger Data Coll
	 */
	public function set passengerDataColl(value:ArrayCollection):void
	{
		this._passengerDataColl = value;
	}
	
	public function get passengerDataColl():ArrayCollection
	{
		return this._passengerDataColl;
	}
	
	/**
	 * Status Data
	 */
	public function set statusData(value:StatusDataVO):void
	{
		this._statusData = value;
	}
	
	public function get statusData():StatusDataVO
	{
		return this._statusData;
	}

}
}