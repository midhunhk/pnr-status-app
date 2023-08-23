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
package com.ae.apps.pnrstatus.service

import com.ae.apps.pnrstatus.exceptions.InvalidServiceException
import com.ae.apps.pnrstatus.service.status.IndianRailService
import com.ae.apps.pnrstatus.service.status.IrctcPnrStatusService
import com.ae.apps.pnrstatus.service.status.IxigoService
import com.ae.apps.pnrstatus.service.status.PNRStatusService
import com.ae.apps.pnrstatus.service.status.PnrApiService
import com.ae.apps.pnrstatus.service.status.TrainPnrStatusService

/**
 * The StatusServiceFactory returns an implementation of the IStatusService class based on the request.
 *
 */
object StatusServiceFactory {
    private const val IXIGO_SERVICE = 1
    private const val PNRAPI_SERVICE = 2
    private const val INDIAN_RAIL_SERVICE = 3
    private const val PNR_STATUS_SERVICE = 4
    private const val IRCTC_PNR_STATUS_SERVICE = 5
    const val TRAIN_PNR_STATUS_SERVICE = 6

    /**
     * Returns an IStatusService implementation based on the value of the type
     *
     * @param type the type of service requested
     * @return an instance of StatusService based on the requested type
     * @throws InvalidServiceException if the value of type is invalid
     */
    @Throws(InvalidServiceException::class)
    fun getService(type: Int): IStatusService {
        // Identify the type of service to create
        var statusService: IStatusService? = null
        when (type) {
            IXIGO_SERVICE -> statusService = IxigoService()
            INDIAN_RAIL_SERVICE -> statusService = IndianRailService()
            PNRAPI_SERVICE -> statusService = PnrApiService()
            PNR_STATUS_SERVICE -> statusService = PNRStatusService()
            IRCTC_PNR_STATUS_SERVICE -> statusService = IrctcPnrStatusService()
            TRAIN_PNR_STATUS_SERVICE -> statusService = TrainPnrStatusService()
        }
        if (null == statusService) {
            throw InvalidServiceException()
        }
        return statusService
    }

    /**
     * Returns an instance of IStatusService based on the type
     *
     * @param type requested type
     * @return returns an instance of StatusService based on teh requested type
     * @throws InvalidServiceException if the value of type is invalid
     */
    @Throws(InvalidServiceException::class)
    fun getService(type: String): IStatusService {
        return getService(type.toInt())
    }
}