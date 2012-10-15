package com.ae.business
{
import com.ae.vo.PnrNumberVO;
import com.ae.vo.StatusHistoryVO;

import mx.collections.ArrayCollection;

/**
 * The Model class for PNRData for this application.
 */
public class PNRDataManager
{
	//-------------------------------------------------------------------------
	//
	//	Variables
	//
	//-------------------------------------------------------------------------
	
	[Bindable]
	public var pnrDataColl:ArrayCollection = new ArrayCollection();
	
	//-------------------------------------------------------------------------
	//
	//	Methods
	//
	//-------------------------------------------------------------------------
	/**
	 * This method sets the pnrDataColl
	 */
	public function setPnrDataColl(coll:ArrayCollection):void
	{
		pnrDataColl = coll;
	}
	
	/**
	 * This method will return the array collection
	 */
	public function getPnrDataColl():ArrayCollection
	{
		return pnrDataColl;
	}
	
	/**
	 * This method will add a new pnrvo to the collection
	 */
	public function addPnrRow(pnrVO:PnrNumberVO):Boolean
	{
		// See if its a duplicate item
		for each(var item:PnrNumberVO in pnrDataColl)
		{
			if( item.equals(pnrVO) )
				return false;
		}
		// Add the item at front
		pnrDataColl.addItemAt(pnrVO, 0);
		return true;
	}
	
	/**
	 * This method removes the pnrVo from the collection
	 */
	public function removePnrRow(pnrVo:PnrNumberVO):void
	{
		var index:int = pnrDataColl.getItemIndex(pnrVo)
		if(index >= 0)
			pnrDataColl.removeItemAt(index);
	}
	
	/**
	 * This method will update a pnrrow present in the collection
	 */
	public function updatePnrRow(oldPnrVo:PnrNumberVO, newPnrVo:PnrNumberVO, 
		statusHistoryVO:StatusHistoryVO):void
	{
		var index:int = pnrDataColl.getItemIndex(oldPnrVo);
		var pnrVo:PnrNumberVO = pnrDataColl[index];
		if(newPnrVo != null)
		{
			//pnrVo.pnr1 = newPnrVo.pnr1;
			//pnrVo.pnr2 = newPnrVo.pnr2;
			pnrVo.notes = newPnrVo.notes;
		}
		if(statusHistoryVO != null)
		{
			pnrVo.historyColl.addItem(statusHistoryVO);
		}
	}
	
	public function clearHistory(pnrVo:PnrNumberVO):void
	{
		pnrVo.historyColl = new ArrayCollection();
	}

}
}