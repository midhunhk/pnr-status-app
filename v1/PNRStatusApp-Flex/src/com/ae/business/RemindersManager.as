package com.ae.business
{
import com.ae.vo.ReminderVO;

import mx.collections.ArrayCollection;
	
/**
 * The Model class for Reminders
 */
public class RemindersManager
{
	//-------------------------------------------------------------------------
	//
	//	Variables
	//
	//-------------------------------------------------------------------------
	[Bindable]
	public var remindersColl:ArrayCollection = new ArrayCollection();
	
	//-------------------------------------------------------------------------
	//
	//	Methods
	//
	//-------------------------------------------------------------------------
	
	/**
	 * Method to set the reminders collection
	 */
	public function setRemindersColl(coll:ArrayCollection):void
	{
		remindersColl = coll;
	}
	
	/**
	 * Method that returns the reminders collection
	 */
	public function getRemindersColl():ArrayCollection
	{
		return remindersColl;
	}
	
	/**
	 * Adds a new arraycollection to the exisiting coll.
	 */
	public function addRemindersColl(coll2:ArrayCollection):void
	{
		remindersColl.source = remindersColl.source.concat(coll2.toArray());
	}
	
	/**
	 * Method to add a new reminder
	 */
	public function addReminder(reminderVo:ReminderVO):void
	{
		remindersColl.addItem(reminderVo);
	}
	
	/**
	 * This method removes the pnrVo from the collection
	 */
	public function removeReminder(reminderVo:ReminderVO):void
	{
		var index:int = remindersColl.getItemIndex(reminderVo)
		if(index >= 0)
			remindersColl.removeItemAt(index);
	}
	
	/**
	 * This method will update a pnrrow present in
	 */
	public function updateReminder(reminderVo:ReminderVO, newReminderVo:ReminderVO):void
	{
		var index:int = remindersColl.getItemIndex(reminderVo);
		if(index >= 0)
			remindersColl[index] = newReminderVo;
	}	

}
}