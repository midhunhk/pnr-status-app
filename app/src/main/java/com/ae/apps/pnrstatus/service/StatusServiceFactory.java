/*
 * MIT License
 *
 * Copyright (c) 2019 Midhun Harikumar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ae.apps.pnrstatus.service;

import com.ae.apps.pnrstatus.exceptions.InvalidServiceException;
import com.ae.apps.pnrstatus.service.status.IndianRailService;
import com.ae.apps.pnrstatus.service.status.IrctcPnrStatusService;
import com.ae.apps.pnrstatus.service.status.IxigoService;
import com.ae.apps.pnrstatus.service.status.PNRStatusService;
import com.ae.apps.pnrstatus.service.status.PnrApiService;
import com.ae.apps.pnrstatus.service.status.TrainPnrStatusService;

/**
 * The StatusServiceFactory returns an implementation of the IStatusService class based on the request.
 *
 * @author Midhun_Harikumar
 */
public class StatusServiceFactory {
    private static final int IXIGO_SERVICE = 1;
    private static final int PNRAPI_SERVICE = 2;
    private static final int INDIAN_RAIL_SERVICE = 3;
    private static final int PNR_STATUS_SERVICE = 4;
    private static final int IRCTC_PNR_STATUS_SERVICE = 5;
    public static final int TRAIN_PNR_STATUS_SERVICE = 6;

    /**
     * Returns an IStatusService implementation based on the value of the type
     *
     * @param type the type of service requested
     * @return an instance of StatusService based on the requested type
     * @throws InvalidServiceException if the value of type is invalid
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
            case TRAIN_PNR_STATUS_SERVICE:
                statusService = new TrainPnrStatusService();
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
     * @param type requested type
     * @return returns an instance of StatusService based on teh requested type
     * @throws InvalidServiceException if the value of type is invalid
     */
    public static IStatusService getService(String type) throws InvalidServiceException {
        return getService(Integer.valueOf(type));
    }
}