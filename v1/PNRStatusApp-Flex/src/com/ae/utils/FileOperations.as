package com.ae.utils
{
import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.FileListEvent;
import flash.filesystem.File;
import flash.net.FileFilter;

[Event(name="complete", type="flash.events.Event")]

/**
 * FileOperations class helps to select files or folders
 */
public class FileOperations extends EventDispatcher
{
	//-------------------------------------------------------------------------
	//
	//	Variables
	//
	//-------------------------------------------------------------------------
	
	private var file:File;
	
	/**
	 * fileFilter
	 */
	private var _fileFilter:FileFilter;
	
	public function set fileFilter(value:FileFilter):void
	{
		_fileFilter = value;
	}
	
	public function get fileFilter():FileFilter
	{
		return _fileFilter;
	} 
	
	/**
	 * selectedFile
	 */
	private var _selectedFile:File;
	
	public function set selectedFile(value:File):void
	{
		_selectedFile = value;
	}
	
	public function get selectedFile():File
	{
		return _selectedFile;
	} 
	
	//-------------------------------------------------------------------------
	//
	//	Methods
	//
	//-------------------------------------------------------------------------
	
	public function FileOperations()
	{
		file = File.documentsDirectory;
	}
	
	//---------------------------------
	// File Operation
	//---------------------------------
	
	public function browseForSave(message:String):void 
	{
		file.browseForSave(message);
		file.addEventListener(Event.SELECT, onFileSaveSelect);
	}
	
	public function browseForOpenMultiple(message:String, overrideFileFilter:FileFilter = null):void 
	{
		var tempFileFilter:FileFilter = 
			(overrideFileFilter != null)?overrideFileFilter : fileFilter;
		if(tempFileFilter)
			file.browseForOpenMultiple(message, [tempFileFilter]);
		else
			file.browseForOpenMultiple(message);
		file.addEventListener(FileListEvent.SELECT_MULTIPLE, onMultipleSelect);
	}
	
	public function browseForOpen(message:String):void
	{
		if(fileFilter)
			file.browseForOpen(message, [fileFilter]);
		else
			file.browseForOpen(message);
		file.addEventListener(Event.SELECT, onFileSelect);
	}
	
	//---------------------------------
	// File Operation event Handlers
	//---------------------------------
	
	/**
	 * This method will be invoked when saving a file
	 */
	private function onFileSaveSelect(event:Event):void
	{
		file.removeEventListener(Event.SELECT, onFileSaveSelect);
		selectedFile = event.target as File;
		dispatchEvent(new Event("complete"));
	}
	
	/**
	 * This method will be invoked for opening a file
	 */
	private function onFileSelect(event:Event):void
	{
		file.removeEventListener(Event.SELECT, onFileSelect);
		selectedFile = event.target as File;
		dispatchEvent(new Event("complete"));
	}
	
	/**
	 * Invoked when multiple files are selected
	 */
	private function onMultipleSelect(event:FileListEvent):void
	{
		file.removeEventListener(FileListEvent.SELECT_MULTIPLE, onMultipleSelect);

	}
}

}