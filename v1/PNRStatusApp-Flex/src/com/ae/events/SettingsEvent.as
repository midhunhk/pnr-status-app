package com.ae.events
{
import flash.events.Event;
import flash.filesystem.File;

import mx.collections.ArrayCollection;

/**
 * The SettingsEvent is used to load and save data
 */
public class SettingsEvent extends Event
{
	//-------------------------------------------------------------------------
	//
	//	Variables
	//
	//-------------------------------------------------------------------------
	
	public static const LOAD_SETTINGS:String = "loadSettings";
	public static const SAVE_SETTINGS:String = "saveSettings";
	
	public static const SETTINGS_LOADED:String = "settingsLoaded";
	public static const REMINDERS_LOADED:String = "remindersLoaded";
	
	public static const EXPORT_REMINDERS:String = "exportReminders";
	public static const IMPORT_REMINDERS:String = "importReminders";
	
	/**
	 * File Objects
	 */
	public var settingsFile:File;
	public var remindersFile:File;
	
	/**
	 * General Purpose ArrayCollection
	 */
	public var collection:ArrayCollection;
	
	//-------------------------------------------------------------------------
	//
	//	Constructor
	//
	//-------------------------------------------------------------------------
	public function SettingsEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
	{
		super(type, bubbles, cancelable);
	}
	
}
}