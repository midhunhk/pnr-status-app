package com.ae.vo
{
/**
 * VO for the Data Format
 */
[Bindable]
public class StatusDataVO
{	
	/**
	 * _trainDest
	 * To Station
	 */
	
	private var _trainDest:String;
	
	public function set trainDest(value:String):void
	{
		this._trainDest = value;
	}
	
	public function get trainDest():String
	{
		return this._trainDest;
	}
	
	/**
	 * _trainOrigin
	 * From Station
	 */
	
	private var _trainOrigin:String;
	
	public function set trainOrigin(value:String):void
	{
		this._trainOrigin = value;
	}
	
	public function get trainOrigin():String
	{
		return this._trainOrigin;
	}
	
	/**
	 * _trainFareClass
	 * SL, AC3 etc 
	 */
	
	private var _trainFareClass:String;
	
	public function set trainFareClass(value:String):void
	{
		this._trainFareClass = value;
	}
	
	public function get trainFareClass():String
	{
		return this._trainFareClass;
	}
	
	/**
	 * _chartStat
	 * CHART PREPARED or CHART NOT PREPARED
	 */
	
	private var _chartStat:String;
	
	public function set chartStat(value:String):void
	{
		this._chartStat = value;
	}
	
	public function get chartStat():String
	{
		return this._chartStat;
	}
	
	/**
	 * _trainBoard
	 * Boarding Place
	 */
	
	private var _trainBoard:String;
	
	public function set trainBoard(value:String):void
	{
		this._trainBoard = value;
	}
	
	public function get trainBoard():String
	{
		return this._trainBoard;
	}
	
	/**
	 * _trainEmbark
	 * Destination
	 */
	
	private var _trainEmbark:String;
	
	public function set trainEmbark(value:String):void
	{
		this._trainEmbark = value;
	}
	
	public function get trainEmbark():String
	{
		return this._trainEmbark;
	}
	
	/**
	 * _trainNo
	 */
	
	private var _trainNo:String;
	
	public function set trainNo(value:String):void
	{
		this._trainNo = value;
	}
	
	public function get trainNo():String
	{
		return this._trainNo;
	}
	
	/**
	 * _trainName
	 */
	
	private var _trainName:String;
	
	public function set trainName(value:String):void
	{
		this._trainName = value;
	}
	
	public function get trainName():String
	{
		return this._trainName;
	}
	
	/**
	 * _trainJourney
	 * Date of Travel
	 */
	
	private var _trainJourney:String;
	
	public function set trainJourney(value:String):void
	{
		this._trainJourney = value;
	}
	
	public function get trainJourney():String
	{
		return this._trainJourney;
	}

}

}