package com.ae.apps.pnrstatus.service;

import com.ae.apps.pnrstatus.exception.InvalidServiceException;

/**
 * The StatusServiceFactory returns an implementation of the 
 * IStatusService class based on the request.
 * @author Midhun_Harikumar
 *
 */
public class StatusServiceFactory 
{
	public static final int IXIGO_SERVICE = 1;
	public static final int PNRAPI_SERVICE = 2;
	public static final int INDIAN_RAIL_SERVICE = 3;
	
	/**
	 * The getService() method returns an IStatusService implementation
	 * based on the value of the type
	 * @param type the type of service requested
	 * @return
	 * @throws InvalidServiceException if the value of type is invalid 
	 */
	public static IStatusService getService(int type) throws InvalidServiceException
	{
		// Identify the type of service to create
		IStatusService statusService = null;
		switch(type)
		{
			case IXIGO_SERVICE:
				statusService = new IxigoService();
				break;
			case INDIAN_RAIL_SERVICE:
				statusService = new IndianRailService();
				break;
			case PNRAPI_SERVICE:
				statusService = new PnrApiService();
				break;
		}
		if(null == statusService)
			throw new InvalidServiceException();
		
		return statusService;
	}
}