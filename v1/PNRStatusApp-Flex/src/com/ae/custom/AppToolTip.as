package com.ae.custom
{
import flash.filters.DropShadowFilter;

import mx.skins.ProgrammaticSkin;

/**
 * AppToolTip class extends the ProgrammaticSkin class to
 * display a new tooltip for the application
 */
public class AppToolTip extends ProgrammaticSkin
{
	//-------------------------------------------------------------------------
	//
	//	Methods
	//
	//-------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public function AppToolTip()
	{
		super();
	}
	
	/**
	 * override the updateDisplayList method to draw our version of the tooltip
	 */
	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
	{
		graphics.clear();
		graphics.lineStyle(0, 0, 0);
		graphics.beginFill(0x7AA4B9, .8);
		graphics.drawRoundRectComplex(0, 0, unscaledWidth, unscaledHeight, 0, 0, 0, 5);
		graphics.endFill();
		filters = [new DropShadowFilter()];
	}	
}
}