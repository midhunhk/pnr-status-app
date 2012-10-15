package com.ae.utils
{
	import com.adobe.serialization.json.JSON;
	import com.ae.vo.PassengerDataVO;
	import com.ae.vo.ResultVO;
	import com.ae.vo.StatusDataVO;
	
	import mx.collections.ArrayCollection;
	
	
/**
 * Utility class for operations on Response from server
 */
public class ResponseUtils
{
	/**
	 * Returns the ResultVO with parsed data from the rawData
	 */	
	public static function parseResponse(rawData:String):ResultVO
	{
		var result:ResultVO;
		var passengerData:String = 
			rawData.substring(rawData.indexOf("["), rawData.indexOf("]")+1);
		var otherData:String = 
			"[{" + rawData.substr( rawData.indexOf("]") + 2) + "]";
		
		try
		{
			var passengerDataArray:Array = (JSON.decode(passengerData) as Array);
			var otherInfoArray:Array = (JSON.decode(otherData) as Array);
			
			// Create a ResultVO object
			result = new ResultVO();
			result.passengerDataColl = getPassengerVOs(passengerDataArray);
			result.statusData = getStatusVO(otherInfoArray);
		}
		catch(e:Error)
		{
			trace("[Error] ResponseUtils.parseResponse() : " + e.message);
		}
		return result;
	}
	
	/**
	 * Returns a collection of PassengerVOs based on the Array that is 
	 * passed to it.
	 */
	public static function getPassengerVOs(dataArray:Array):ArrayCollection
	{
		var passengerColl:ArrayCollection = new ArrayCollection();
		for each(var item:Object in dataArray)
		{
			var passengerData:PassengerDataVO = new PassengerDataVO();
			passengerData.trainBookingBerth = item.trainBookingBerth;
			passengerData.trainCurrentStatus = item.trainCurrentStatus;
			passengerData.trainPassenger = item.trainPassenger;
			passengerData.berthPosition = 
				Utils.getBerthPosition(item.trainBookingBerth, item.trainCurrentStatus); 
			passengerColl.addItem(passengerData);
		}
		
		return passengerColl;
	}
	
	/**
	 * Retuns a StatusDataVO object with data from the Array that is passed in
	 */
	public static function getStatusVO(dataArray:Array):StatusDataVO
	{
		var statusVO:StatusDataVO = new StatusDataVO();		
		statusVO.trainDest = dataArray[0].trainDest;
		statusVO.trainOrigin = dataArray[0].trainOrigin;
		statusVO.trainFareClass = dataArray[0].trainFareClass;
		statusVO.chartStat = dataArray[0].chartStat;
		statusVO.trainBoard = dataArray[0].trainBoard;
		statusVO.trainEmbark = dataArray[0].trainEmbark;
		statusVO.trainNo = dataArray[0].trainNo;
		statusVO.trainName = dataArray[0].trainName;
		statusVO.trainJourney = dataArray[0].trainJourney;
		return statusVO;
	}

}
}