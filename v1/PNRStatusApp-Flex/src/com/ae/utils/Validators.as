package com.ae.utils
{
	import flash.ui.Keyboard;
	
	
/**
 * Utility class that provides validation methods
 */
public class Validators
{
	/**
	 * This function returns true if pnr1 is a 3 digit number
	 * and pnr2 is a 7 digit number
	 */
	public static function isValidPNR(pnr1:String, pnr2:String):Boolean
	{
		var pattern1:RegExp = /^\d{3}$/;
		var pattern2:RegExp = /^\d{7}$/
		
		return (pattern1.test(pnr1) && pattern2.test(pnr2));
	}
	
	/**
	 * This function returns true if the supplied parameter is in the
	 * format ddd-ddddddd where d is a number
	 */
	public static function isValidPNR2(pnr:String):Boolean
	{
		var pattern:RegExp = /^\d{3}-\d{7}$/;
		return pattern.test(pnr);
	}
	
	/**
	 * Returns true or false based if entered key is numeric
	 */
	public static function isNumericKey(keyCode:uint):Boolean
	{
		if(keyCode >= Keyboard.NUMBER_0 && keyCode <= Keyboard.NUMBER_9)
			return true;
		if(keyCode >= Keyboard.NUMPAD_0 && keyCode <= Keyboard.NUMPAD_9)
			return true;
		return false;
	}

}
}