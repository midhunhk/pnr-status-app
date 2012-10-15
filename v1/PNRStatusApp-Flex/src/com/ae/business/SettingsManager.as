package com.ae.business
{
import com.ae.utils.FileReadWrite;
import com.ae.utils.Utils;

import flash.filesystem.File;

import mx.collections.ArrayCollection;

/**
 * SettingsManager
 */
public class SettingsManager
{
	//-------------------------------------------------------------------------
	//
	//	Methods
	//
	//-------------------------------------------------------------------------
	
	/**
	 * Method to load settings fromi disk
	 */
	public function loadSettings(settingsFile:File):ArrayCollection
	{
		var coll:ArrayCollection;
		if(settingsFile && settingsFile.exists)
		{
			var tempXML:XML = FileReadWrite.readXMLFile(settingsFile);
			coll = Utils.getPNRCollFromXML(tempXML);
		}
		else
		{
			throw(new Error("File Does not exist!"));
		}
		
		return coll;
	}
	
	/**
	 * Method to load reminders collection from the disk
	 */
	public function loadReminders(remindersFile:File):ArrayCollection
	{
		var coll:ArrayCollection;
		if(remindersFile && remindersFile.exists)
		{
			var tempXML:XML = FileReadWrite.readXMLFile(remindersFile);
			coll = Utils.getRemindersCollFromXML(tempXML);
		}
		else
		{
			throw(new Error("File Does not exist!"));
		}
		
		return coll;
	}
	
	/**
	 * Save the settings
	 */
	public function saveSettings(settingsFile:File, coll:ArrayCollection):void
	{
		if(settingsFile && coll)
		{
			var temp:XML = Utils.getPNRCollAsXML(coll);
			FileReadWrite.writeToFile(settingsFile, temp.toXMLString(), true);
		}			
	}
	
	/**
	 * 
	 * Save the Reminders
	 */
	public function saveReminders(file:File, coll:ArrayCollection):void
	{
		if(file && coll)
		{
			var temp:XML = Utils.getRemindersCollAsXML(coll);
			FileReadWrite.writeToFile(file, temp.toXMLString(), true);
		}			
	}
	
	/**
	 * Function to export reminders to a specified file,
	 * uses the saveReminders function
	 */
	public function exportReminders(file:File, coll:ArrayCollection):void
	{
		if(file.nativePath.lastIndexOf(".rem") == -1)
			file = new File(file.nativePath + ".rem");
		saveReminders(file, coll);
	}
	
	/**
	 * Function to import reminders from a specified file,
	 * used the loadReminders function
	 */
	public function importReminders(file:File):ArrayCollection
	{
		return loadReminders(file);
	}

}
}