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

package com.ae.apps.pnrstatus.service.status;

import com.ae.apps.pnrstatus.exceptions.InvalidServiceException;
import com.ae.apps.pnrstatus.exceptions.StatusException;
import com.ae.apps.pnrstatus.service.IStatusService;
import com.ae.apps.pnrstatus.service.StatusServiceFactory;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TrainPnrStatusServiceTest {

    private static final String PNR_NUMBER = "1234567890";

    private IStatusService service;

    @Before
    public void setUp() throws InvalidServiceException {
        service = StatusServiceFactory.getService(StatusServiceFactory.TRAIN_PNR_STATUS_SERVICE);
    }

    @Test
    public void testCorrectServiceCreated(){
        assertNotNull(service);
        TrainPnrStatusService trainPnrStatusService = (TrainPnrStatusService) service;
        assertNotNull(trainPnrStatusService);
    }

    @org.junit.Test
    public void testGetResponse() throws StatusException {
        PNRStatusVo statusVo = service.getResponse(PNR_NUMBER, true);
        assertNotNull(statusVo);
    }
}