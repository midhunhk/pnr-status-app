package com.ae.utils
{
import flash.events.IOErrorEvent;
import flash.filesystem.File;
import flash.filesystem.FileMode;
import flash.filesystem.FileStream;

/**
 * FileReadWrite is a utility class for performing read and write
 * to and from DiskFiles
 */
public class FileReadWrite
{
	//-------------------------------------------------------------------------
	//
	//	Variables
	//
	//-------------------------------------------------------------------------
	
	/**
	 * @private
	 */
	private static const xmlHeader:String = 
		"<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	
	//-------------------------------------------------------------------------
	//
	//	Methods
	//
	//-------------------------------------------------------------------------
	
	/**
     * writeToFile function can be used to write to an external file.
     * parameters :
     * @file:File object with the path to write
     * @fileContents : The contents to be written
     * @includeXmlHeader : Specify whether the function should add XML Header 
     * 		while writing the file.
     * @returns true or false based on whether file write was successful or not
     */		
	public static function writeToFile(file:File, fileContents:String,
		includeXmlHeader:Boolean=false):Boolean
	{
		try
		{
			if(includeXmlHeader)
				fileContents = xmlHeader + fileContents;
			var fileStream:FileStream = new FileStream();
			fileStream.addEventListener(IOErrorEvent.IO_ERROR, fault);
			fileStream.open(file, FileMode.WRITE);
			fileStream.writeUTFBytes(fileContents);
		}
		catch(error:Error)
		{
			trace(" writeToFile():Error " + error.message);
			return false;
		}
		finally
		{
			fileStream.close();
		}
		return true;
	}
	
	/**
	 * readXMLFile function can be used to read an XML file
	 * @file : File object of the file file to be read
	 */
	public static function readXMLFile(file:File):XML
	{
		var xmlFile:XML = null;
		if(file.exists)
		{
			var fileStream:FileStream = new FileStream();
			fileStream.addEventListener(IOErrorEvent.IO_ERROR, fault);
			fileStream.open(file, FileMode.READ);
			
			xmlFile = 
				new XML(fileStream.readUTFBytes( fileStream.bytesAvailable ) );
			fileStream.close();
		}
		return xmlFile;
	}
    
    /**
     * Function that is invoked on an IO Error that may occur on 
     * a FileStream object
     */      
	private static function fault(error:IOErrorEvent):void
	{
		error.target.removeEventListener(IOErrorEvent.IO_ERROR, fault);
       	trace("FileReadWrite.fault : " + error.toString());
	}

}
}