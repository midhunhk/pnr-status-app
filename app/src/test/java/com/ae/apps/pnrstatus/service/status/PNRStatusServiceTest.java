package com.ae.apps.pnrstatus.service.status;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ae.apps.pnrstatus.exceptions.StatusException;
import com.ae.apps.pnrstatus.service.NetworkService;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
public class PNRStatusServiceTest {

    private PNRStatusService pnrStatusService;
    private NetworkService mockNetworkService;

    // A sample valid JSON response similar to your stub but more controlled for testing
    private final String MOCK_VALID_JSON_RESPONSE = "{" +
            "'Charting':'CHART NOT PREPARED'," +
            "'Journey':\"[['Train Number','12345'],['Train Name','TEST EXPRESS']," +
            "['Boarding Date','01-01-2025'],['From','SRC'],['To','DEST']," +
            "['Reserved Upto','DEST'],['Boarding Point','SRC'],['Class','SL']\"," +
            "'Status':[" +
            "['S. No.','Booking Status(Coach No , Berth No., Quota)','Current Status (Coach No , Berth No.)']," +
            "['Passenger 1','S1 , 10,GN','CNF']," +
            "['Passenger 2','S1 , 11,GN','WL 1']" +
            "]," +
            "'Legend':{}" + // Simplified legend for this test
            "}";

    @Before
    public void setUp() {
        pnrStatusService = new PNRStatusService();
        // We need to mock the static getInstance() method of NetworkService
        // mockNetworkService will be the instance returned by NetworkService.getInstance()
        mockNetworkService = mock(NetworkService.class);
    }

    @Test
    public void getServiceName_shouldReturnCorrectName() {
        Assert.assertEquals("PNRStatus", pnrStatusService.getServiceName());
    }

    @Test
    public void getResponse_successfulFetchAndParse_shouldReturnPopulatedVo() throws Exception {
        // Arrange
        String pnr = "1234567890";
        // Use try-with-resources for MockedStatic
        try (MockedStatic<NetworkService> mockedStaticNetworkService = Mockito.mockStatic(NetworkService.class)) {
            mockedStaticNetworkService.when(NetworkService::getInstance).thenReturn(mockNetworkService);
            when(mockNetworkService.doPostRequest(anyString(), anyMap(), anyMap())).thenReturn(MOCK_VALID_JSON_RESPONSE);

            // Act
            PNRStatusVo resultVo = pnrStatusService.getResponse(pnr);

            // Assert
            Assert.assertNotNull(resultVo);
            //Assert.assertEquals(pnr, resultVo.pnrNumber); // Check if PNR number is set back
            Assert.assertEquals("CHART NOT PREPARED", resultVo.getChartStatus());
            Assert.assertEquals("12345", resultVo.trainNo);
            Assert.assertEquals("TEST EXPRESS", resultVo.trainName);
            Assert.assertEquals("01-01-2025", resultVo.getTrainJourneyDate());
            Assert.assertEquals("SL", resultVo.getTicketClass());
            Assert.assertNotNull(resultVo.passengers);
            Assert.assertEquals(2, resultVo.passengers.size());

            PassengerDataVo passenger1 = resultVo.passengers.get(0);
            Assert.assertEquals("Passenger 1", passenger1.getPassenger());
            Assert.assertEquals("S1 , 10,GN", passenger1.getBookingBerth());
            Assert.assertEquals("CNF", passenger1.getCurrentStatus());
            // This depends on PNRUtils.getBerthPosition, which is an external dependency to this specific test focus.
            // For a pure unit test of PNRStatusService, we assume PNRUtils works or test it separately.
            // You might want to mock PNRUtils if its logic is complex and affects PNRStatusService significantly.
            // For now, let's check it's not null or make a basic assertion based on known logic.
            Assert.assertNotNull(passenger1.getBerthPosition());

            Assert.assertEquals("CNF", resultVo.getCurrentStatus()); // First passenger's status
        }
    }

    @Test(expected = StatusException.class)
    public void getResponse_networkServiceReturnsNull_shouldThrowStatusException() throws StatusException, IOException {
        // Arrange
        String pnr = "1234567890";
        try (MockedStatic<NetworkService> mockedStaticNetworkService = Mockito.mockStatic(NetworkService.class)) {
            mockedStaticNetworkService.when(NetworkService::getInstance).thenReturn(mockNetworkService);
            when(mockNetworkService.doPostRequest(anyString(), anyMap(), anyMap())).thenReturn(null);

            // Act
            pnrStatusService.getResponse(pnr);
        }
        // Assert: Expected StatusException with specific error code can be verified if needed
        // by catching the exception and asserting its properties.
    }

    @Test
    public void getResponse_withStubResponseTrue_shouldUseStubData() throws StatusException {
        // Arrange
        String pnr = "0987654321";
        // No need to mock NetworkService here as it shouldn't be called

        // Act
        PNRStatusVo resultVo = pnrStatusService.getResponse(pnr, true);

        // Assert (basic assertions based on the structure of your actual stub response)
        // This will test the parsing of your PNRStatusService.getStubResponse()
        Assert.assertNotNull(resultVo);
        Assert.assertEquals(pnr, resultVo.pnrNumber);
        Assert.assertEquals("CHART NOT PREPARED", resultVo.getChartStatus()); // From your actual stub
        Assert.assertEquals("16670", resultVo.trainNo); // From your actual stub
        Assert.assertNotNull(resultVo.passengers);
        Assert.assertFalse(resultVo.passengers.isEmpty());
    }

    @Test
    public void getResponse_withStubResponseFalse_shouldFetchFromNetwork() throws Exception {
        // Arrange
        String pnr = "1234567890";
        try (MockedStatic<NetworkService> mockedStaticNetworkService = Mockito.mockStatic(NetworkService.class)) {
            mockedStaticNetworkService.when(NetworkService::getInstance).thenReturn(mockNetworkService);
            when(mockNetworkService.doPostRequest(anyString(), anyMap(), anyMap())).thenReturn(MOCK_VALID_JSON_RESPONSE);

            // Act
            PNRStatusVo resultVo = pnrStatusService.getResponse(pnr, false);

            // Assert (similar to getResponse_successfulFetchAndParse_shouldReturnPopulatedVo)
            Assert.assertNotNull(resultVo);
            Assert.assertEquals("12345", resultVo.trainNo);
        }
    }
}
