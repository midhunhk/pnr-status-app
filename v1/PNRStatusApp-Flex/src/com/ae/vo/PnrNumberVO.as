package com.ae.vo
{
import mx.collections.ArrayCollection;
	
/**
 * VO for holding the PNR Number
 */
[Bindable]
public class PnrNumberVO
{
	public var pnr1:String;
	
	public var pnr2:String;
	 
	public var notes:String;
	
	public var historyColl:ArrayCollection = new ArrayCollection();
	
	/**
	 * Method to compare two PnrNumberVo's
	 */
	public function equals(object:PnrNumberVO):Boolean
	{
		if(object.pnr1 == this.pnr1 && object.pnr2 == this.pnr2)
			return true;
		return false;
	}
	
	/**
	 * This method will copy the values from an existing object
	 */
	public function createFrom(object:PnrNumberVO):PnrNumberVO
	{
		this.pnr1 = object.pnr1;
		this.pnr2 = object.pnr2
		this.notes = object.notes;
		return this;
	}
}

}