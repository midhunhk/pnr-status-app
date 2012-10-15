package com.ae.utils
{
import com.ae.vo.PnrNumberVO;
import com.ae.vo.ReminderVO;
import com.ae.vo.StatusHistoryVO;

import mx.collections.ArrayCollection;
	
/**
 * Utility class
 */
public class Utils
{
	//-------------------------------------------------------------------------
	//
	//	Constants
	//
	//-------------------------------------------------------------------------
	
	public static const MILLISECONDS_PER_DAY:int = 1000 * 60 * 60 * 24;
	
	
	//-------------------------------------------------------------------------
	//
	//	Methods
	//
	//-------------------------------------------------------------------------
	
	/**
	 * This function returns the parameters for the service call
	 */
	public static function getServiceParams(pnr1:String, pnr2:String):String
	{
		return ("pnr1=" + pnr1 + "&pnr2=" + pnr2);
	}
	
	/**
	 * This function returns the stylename based on the ticket status
	 */
	public static function getStyleNameForResult(currentStatus:String):String
	{
		if(currentStatus.match(/CNF/))
			return "pnrRowStyleGreen";
		if(currentStatus.match(/Confirmed/))
			return "pnrRowStyleGreen";
		if(currentStatus.match(/S\d{1}/))
			return "pnrRowStyleGreen";
		if(currentStatus.match(/RAC/))
			return "pnrRowStyleYellow";
		if(currentStatus.match(/W\/L/))
			return "pnrRowStyleRed";
		
		return "pnrRowStyleDefault";
	}
	
	public static function getStyleNameForIndex(index:int):String
	{
		if(index == 1)
			return "pnrRowStyleGreen";
		if(index == 2)
			return "pnrRowStyleYellow";
		if(index == 3)
			return "pnrRowStyleRed";
			
		return "pnrRowStyleDefault";
	}
	
	public static function getIndexForStyleName(styleName:String):int
	{
		if(styleName == "pnrRowStyleGreen")
			return 1;
		if(styleName == "pnrRowStyleYellow")
			return 2;
		if(styleName == "pnrRowStyleRed")
			return 3;
			
		return 0;
	}
	
	public static function getPnrNumberVO(rawData:String):PnrNumberVO
	{
		var pnrNumberVO:PnrNumberVO = new PnrNumberVO();
					
		if(rawData.indexOf("-") > -1)
		{
			var tempArr:Array = rawData.split("-");
			if(tempArr.length == 2)
			{
				pnrNumberVO.pnr1 = tempArr[0];
				pnrNumberVO.pnr2 = tempArr[1];
			}
		}
		else
		{
			pnrNumberVO.pnr1 = rawData.substr(0, 3);
			pnrNumberVO.pnr2 = rawData.substr(3, 7);
		}
		
		return pnrNumberVO;
	}
	
	/**
	 * This function returns the Berth Position based on the seat position
	 * 
	 * @params
	 *  bookingStatusString - status at the time of booking
	 *  currentStatusString - current status string
	 *  ticketClass - the class of the ticket, for calculations based on 
	 *    the type of the compartment
	 */
	public static function getBerthPosition(bookingStatusString:String, 
		currentStatusString:String, 
		ticketClass:String = Constants.SLEEPER_CLASS):String
	{
		var values:Array;
		var arr1:Array = bookingStatusString.split(",");
		var arr2:Array = currentStatusString.split(",");
		
		// Making a blind check with array length
		if(arr2.length > 1 && !bookingStatusString.match(/W\/L/))
		{
			values = arr2;
		}
		else if(arr1.length > 1 && !currentStatusString.match(/W\/L/)
			&& !currentStatusString.match(/RAC/))
		{
			values = arr1;
		}
		else
		{
			return "--";
		}
		
		// If at the time of booking, status was W/L, then we can get the
		// berth status if only CHART IS PREPARED, at what time, 
		// currentBookingStatus will be the berth data, if not return --
		if( bookingStatusString.match(/W\/L/) && 
			currentStatusString.match(/Confirmed/))
		{
			return "--";
		}
		
		// Seems to be some proper data, so lets calculate the berth position
		try
		{
			var berthNumber:int = parseInt(values[1]);
			
			if(ticketClass == Constants.SLEEPER_CLASS ||
				ticketClass == Constants.AC2_CLASS)
			{
				// This calculation is valid for Sleeper and AC3 Classes.
				var modValue:int = berthNumber % Constants.SEATS_IN_SLEEPER_COMPARTMENT;
				
				switch(modValue)
				{
					case 0 : 
							return Constants.BERTH_SIDE_UPPER;
					case 7 : 
							return Constants.BERTH_SIDE_LOWER;
					case 1:
					case 4:
							return Constants.BERTH_LOWER;
					case 2:
					case 5:
							return Constants.BERTH_MIDDLE;
					case 3:
					case 6:
							return Constants.BERTH_UPPER;
				}
			}
		}
		catch(e:Error)
		{
			trace(e.message);
		}
		
		return "--";
	}
	
	/**
	 * Method to get an array collection of PnrNumberVos from the xmlData passed
	 * @param xmlData The xml data
	 */
	public static function getPNRCollFromXML(xmlData:XML):ArrayCollection
	{
		var playlistColl:ArrayCollection = new ArrayCollection();
		
		if(xmlData != null && xmlData.pnrData != null)
		{
			for each(var item:XML in xmlData.pnrData)
			{
				var pnrNumber:String = item.pnrNumber;
				var pnrNumberVo:PnrNumberVO = getPnrNumberVO(pnrNumber);
				pnrNumberVo.notes = item.notes;
				
				for each(var history:XML in item.statusHistories.statusHistory)
				{
					var historyVo:StatusHistoryVO = new StatusHistoryVO();
					historyVo.status = history.status;
					historyVo.styleIndex = history.styleIndex;
					historyVo.timestamp = history.timestamp;
					pnrNumberVo.historyColl.addItem(historyVo);
				}
				
				playlistColl.addItem( pnrNumberVo);
			}
		}
		return playlistColl;
	}
	
	/**
	 * This method returns the list of PnrNumberVOs as an
	 * ArrayCollection so that it can be saved.
	 */
	 public static function getPNRCollAsXML(dataColl:ArrayCollection):XML
	 {
		var xmlData:XML = <PNRList></PNRList>;
		
		if(dataColl)
		{
			for(var i:int = 0; i < dataColl.length; i++){
				var pnrNumberVO:PnrNumberVO = dataColl[i] as PnrNumberVO;
				
				var xmlNode:XML = <pnrData></pnrData>
				xmlNode.pnrNumber = pnrNumberVO.pnr1 + pnrNumberVO.pnr2;
				xmlNode.notes = pnrNumberVO.notes
				
				xmlNode.statusHistories = <statusHistories></statusHistories>
				for(var j:int = 0; j < pnrNumberVO.historyColl.length; j++)
				{
					var item:StatusHistoryVO = pnrNumberVO.historyColl[j];
					
					var historyNode:XML 	= <statusHistory></statusHistory>
					historyNode.status 		= item.status;
					historyNode.styleIndex 	= item.styleIndex;
					historyNode.timestamp 	= item.timestamp;
					
					xmlNode.statusHistories.track[j] = historyNode;
				}
				
				xmlData.track[i] = xmlNode;
			}
		}
		return xmlData;
	}
	
	public static function getRemindersCollFromXML(xmlData:XML):ArrayCollection
	{
		var remindersColl:ArrayCollection = new ArrayCollection();
		
		if(xmlData != null && xmlData.reminder != null)
		{
			for each(var item:XML in xmlData.reminder)
			{
				var date:Date = new Date(item.fullYear, item.month, item.date);
				var reminderVo:ReminderVO = new ReminderVO();
				reminderVo.date = date;
				reminderVo.description = item.description;
				remindersColl.addItem( reminderVo);
			}
		}
		return remindersColl;
	}
	
	public static function getRemindersCollAsXML(dataColl:ArrayCollection):XML
	 {
		var xmlData:XML = <reminders></reminders>;
		
		if(dataColl)
		{
			var i:int = 0;
			for each(var item:ReminderVO in dataColl)
			{
				var xmlNode:XML = <reminder></reminder>
				xmlNode.date = item.date.date;
				xmlNode.month = item.date.month;
				xmlNode.fullYear = item.date.fullYear;
				xmlNode.description = item.description;
				
				xmlData.track[i] = xmlNode;
				i++;
			}
		}
		return xmlData;
	}
	
	/**
	 * This function retuns the number of days between 2 dates
	 */
	public static function getDaysDifference(minDate:Date, maxDate:Date):uint
	{
		// Check which date is larger
        return Math.ceil(( maxDate.getTime() - minDate.getTime() ) / MILLISECONDS_PER_DAY);
	} 

}
}