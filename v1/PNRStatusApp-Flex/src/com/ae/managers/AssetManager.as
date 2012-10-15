package com.ae.managers
{

/**
 * This class provedes the management for external assets for the project
 */
public class AssetManager
{
	/**
	 * @private
	 */
	private static var _instance:AssetManager;
	
	//-------------------------------------------------------------------------
	//
	//  Constructor
	//
	//-------------------------------------------------------------------------
    public function AssetManager() 
    {
          if(_instance!=null) {
                throw new Error("Singleton already instantiated");
          }
    }
    
    /**
     * Returns the singleton instance
     */ 
    public static function getInstance():AssetManager
    {
          if(_instance == null)
                _instance = new AssetManager();
          return _instance;
    }
    
    //-------------------------------------------------------------------------
	//
	//  Assets
	//
	//-------------------------------------------------------------------------
    
    [Embed(source="/resources/videos/spinner.swf")]
    [Bindable]public  var spinner : Class;
    
    [Embed(source="/resources/images/air_logo_small.png")]
    [Bindable]public  var Air_Logo : Class;
    
    [Embed(source="/resources/images/arrow_down.png")]
    [Bindable]public  var DownArrow : Class;
    
    [Embed(source="/resources/images/header_bg.png")]
    [Bindable]public  var HeaderBg : Class;
    
    [Embed(source="/resources/images/arrow_up.png")]
    [Bindable]public  var UpArrow : Class;
    
    [Embed(source="/resources/images/icons/selected_icon_16.png")]
    [Bindable]public  var SelectedIcon : Class;
    
    [Embed(source="/resources/images/icons/about_icon_032.png")]
    [Bindable]public  var AboutIcon : Class;
    
    [Embed(source="/resources/images/icons/r_icon_032.png")]
    [Bindable]public  var ReservationIcon : Class;
    
    [Embed(source="/resources/images/icons/remove_icon.png")]
    [Bindable]public  var RemoveIcon : Class;
    
    [Embed(source="/resources/images/icons/remove_icon_2.png")]
    [Bindable]public  var RemoveIcon2 : Class;
    
    [Embed(source="/resources/images/icons/clipboard_icon.png")]
    [Bindable]public  var ClipboardIcon : Class;

}
}