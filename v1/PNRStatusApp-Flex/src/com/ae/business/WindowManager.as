package com.ae.business
{
import com.ae.events.ShowWindowEvent;
import com.ae.views.AboutWindow;
import com.ae.views.MoreInfoPanel;
import com.ae.views.ReservationHelper;
import com.ae.views.StatusHistoryWindow;

import flash.display.DisplayObject;

import mx.collections.ArrayCollection;
import mx.core.Application;
import mx.managers.PopUpManager;
	
/**
 * This class is used to managed to display PopUp Windows on this 
 * application. 
 */
public class WindowManager
{
	//-------------------------------------------------------------------------
	//
	//	Variables
	//
	//-------------------------------------------------------------------------
	
	/**
	 * Reference to the parent object
	 */
	private var _parentWindow:DisplayObject;
	
	//-------------------------------------------------------------------------
	//
	//	Variables
	//
	//-------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public function WindowManager()
	{
		_parentWindow = Application.application as DisplayObject;
	}
	
	/**
	 * This function will create an instance of the MoreInfoPanel,
	 * set the required data and add it to the PopUpManager
	 */
	public function showMoreInfoPanel(event:ShowWindowEvent=null):void
	{
		if(event)
		{
			event.stopImmediatePropagation();
			// Create an instance of MoreInfoPanel
			var moreInfo:MoreInfoPanel = new MoreInfoPanel();
			moreInfo.width = 350;
			moreInfo.height = 390;
			moreInfo.resultVO = event.resultVO;
			
			// Show it as a PopUp
			PopUpManager.addPopUp(moreInfo, _parentWindow, true);
			PopUpManager.centerPopUp(moreInfo);
		}
	}
	
	public function showStatusHistoryPanel(event:ShowWindowEvent=null):void
	{
		if(event)
		{
			event.stopImmediatePropagation();
			
			// Show a window with stuff
			var statusHistory:StatusHistoryWindow = new StatusHistoryWindow();
			statusHistory.width = 300;
			statusHistory.height = 280;
			statusHistory.data = event.pnrNumberVO;
			
			PopUpManager.addPopUp(statusHistory, _parentWindow, true);
			PopUpManager.centerPopUp(statusHistory);
		}
	}
	
	/**
	 * This function will create an instance of the AboutWindow
	 * and add it to the PopUpManager
	 */
	public function showAboutWindow(event:ShowWindowEvent=null):void
	{
		// Create an instance of AboutWindow
		var aboutWindow:AboutWindow = new AboutWindow();			
		aboutWindow.width = 300;
		aboutWindow.height = 320;
		
		// Show it as a PopUp
		PopUpManager.addPopUp(aboutWindow, _parentWindow, true);
		PopUpManager.centerPopUp(aboutWindow);
	}
	
	/**
	 * This function will create an instance of the ReservationHelper
	 * and add it to the PopUpManager
	 */
	public function showReservationHelper(event:ShowWindowEvent=null, 
		remindersColl:ArrayCollection=null):void
	{
		// Create an instance of reservationHelper
		var reservationHelper:ReservationHelper = new ReservationHelper();
		reservationHelper.remindersColl = remindersColl;			
		reservationHelper.width = 400;
		reservationHelper.height = 380;
		
		// Show it as a PopUp
		PopUpManager.addPopUp(reservationHelper, _parentWindow, true);
		PopUpManager.centerPopUp(reservationHelper);
	}
}
}