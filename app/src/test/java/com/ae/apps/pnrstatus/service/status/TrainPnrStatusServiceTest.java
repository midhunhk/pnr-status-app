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