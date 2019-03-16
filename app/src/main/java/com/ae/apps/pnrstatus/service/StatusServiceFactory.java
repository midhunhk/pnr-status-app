/*
 * Copyright 2014 Midhun Harikumar
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ae.apps.pnrstatus.service;

import com.ae.apps.pnrstatus.exceptions.InvalidServiceException;
import com.ae.apps.pnrstatus.service.status.IndianRailService;
import com.ae.apps.pnrstatus.service.status.IrctcPnrStatusService;
import com.ae.apps.pnrstatus.service.status.IxigoService;
import com.ae.apps.pnrstatus.service.status.PNRStatusService;
import com.ae.apps.pnrstatus.service.status.PnrApiService;

/**
 * The StatusServiceFactory returns an implementation of the IStatusService class based on the request.
 * 
 * @author Midhun_Harikumar
 * 
 */
public class StatusServiceFactory {
	public static final int	IXIGO_SERVICE				= 1;
	public static final int	PNRAPI_SERVICE				= 2;
	public static final int	INDIAN_RAIL_SERVICE			= 3;
	public static final int	PNR_STATUS_SERVICE			= 4;
	public static final int	IRCTC_PNR_STATUS_SERVICE	= 5;

	/**
	 * Returns an IStatusService implementation based on the value of the type
	 * 
	 * @param type
	 *            the type of service requested
	 * @return
	 * @throws InvalidServiceException
	 *             if the value of type is invalid
	 */
	public static IStatusService getService(int type) throws InvalidServiceException {
		// Identify the type of service to create
		IStatusService statusService = null;
		switch (type) {
		case IXIGO_SERVICE:
			statusService = new IxigoService();
			break;
		case INDIAN_RAIL_SERVICE:
			statusService = new IndianRailService();
			break;
		case PNRAPI_SERVICE:
			statusService = new PnrApiService();
			break;
		case PNR_STATUS_SERVICE:
			statusService = new PNRStatusService();
			break;
		case IRCTC_PNR_STATUS_SERVICE:
			statusService = new IrctcPnrStatusService();
			break;
		}
		if (null == statusService) {
			throw new InvalidServiceException();
		}

		return statusService;
	}

	/**
	 * Returns an instance of IStatusService based on the type
	 * 
	 * @param type
	 * @return
	 * @throws InvalidServiceException
	 */
	public static IStatusService getService(String type) throws InvalidServiceException {
		return getService(Integer.valueOf(type));
	}
}