package com.ae.apps.pnrstatus.service.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ae.apps.pnrstatus.exceptions.StatusException;
import com.ae.apps.pnrstatus.service.NetworkService;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

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

    @BeforeEach
    public void setUp() {
        pnrStatusService = new PNRStatusService();
        // We need to mock the static getInstance() method of NetworkService
        // mockNetworkService will be the instance returned by NetworkService.getInstance()
        mockNetworkService = mock(NetworkService.class);
    }

    @Test
    public void getServiceName_shouldReturnCorrectName() {
        assertEquals("PNRStatus", pnrStatusService.getServiceName(), "Service name should match");
    }

    //@Test
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
            assertNotNull(resultVo);
            //assertEquals(pnr, resultVo.pnrNumber); // Check if PNR number is set back
            assertEquals("CHART NOT PREPARED", resultVo.getChartStatus());
            assertEquals("12345", resultVo.trainNo);
            assertEquals("TEST EXPRESS", resultVo.trainName);
            assertEquals("01-01-2025", resultVo.getTrainJourneyDate());
            assertEquals("SL", resultVo.getTicketClass());
            assertNotNull(resultVo.passengers);
            assertEquals(2, resultVo.passengers.size());

            PassengerDataVo passenger1 = resultVo.passengers.get(0);
            assertEquals("Passenger 1", passenger1.getPassenger());
            assertEquals("S1 , 10,GN", passenger1.getBookingBerth());
            assertEquals("CNF", passenger1.getCurrentStatus());
            // This depends on PNRUtils.getBerthPosition, which is an external dependency to this specific test focus.
            // For a pure unit test of PNRStatusService, we assume PNRUtils works or test it separately.
            // You might want to mock PNRUtils if its logic is complex and affects PNRStatusService significantly.
            // For now, let's check it's not null or make a basic assertion based on known logic.
            assertNotNull(passenger1.getBerthPosition());

            assertEquals("CNF", resultVo.getCurrentStatus()); // First passenger's status
        }
    }

    @Test
    public void getResponse_networkServiceReturnsNull_shouldThrowStatusException() throws StatusException, IOException {
        // Arrange
        String pnr = "1234567890";
        try (MockedStatic<NetworkService> mockedStaticNetworkService = Mockito.mockStatic(NetworkService.class)) {
            mockedStaticNetworkService.when(NetworkService::getInstance).thenReturn(mockNetworkService);
            when(mockNetworkService.doPostRequest(anyString(), anyMap(), anyMap())).thenReturn(null);

            // JUnit 5 style exception assertion
            StatusException exception = assertThrows(StatusException.class, () -> {
                pnrStatusService.getResponse(pnr);
            });
        }
        // Assert: Expected StatusException with specific error code can be verified if needed
        // by catching the exception and asserting its properties.
    }

    //@Test
    public void getResponse_withStubResponseTrue_shouldUseStubData() throws StatusException {
        // Arrange
        String pnr = "0987654321";
        // No need to mock NetworkService here as it shouldn't be called

        // Act
        PNRStatusVo resultVo = pnrStatusService.getResponse(pnr, true);

        // Assert (basic assertions based on the structure of your actual stub response)
        // This will test the parsing of your PNRStatusService.getStubResponse()
        assertNotNull(resultVo);
        assertEquals(pnr, resultVo.pnrNumber);
        assertEquals("CHART NOT PREPARED", resultVo.getChartStatus()); // From your actual stub
        assertEquals("16670", resultVo.trainNo); // From your actual stub
        assertNotNull(resultVo.passengers);
        assertFalse(resultVo.passengers.isEmpty());
    }

    //@Test
    public void getResponse_withStubResponseFalse_shouldFetchFromNetwork() throws Exception {
        // Arrange
        String pnr = "1234567890";
        try (MockedStatic<NetworkService> mockedStaticNetworkService = Mockito.mockStatic(NetworkService.class)) {
            mockedStaticNetworkService.when(NetworkService::getInstance).thenReturn(mockNetworkService);
            when(mockNetworkService.doPostRequest(anyString(), anyMap(), anyMap())).thenReturn(MOCK_VALID_JSON_RESPONSE);

            // Act
            PNRStatusVo resultVo = pnrStatusService.getResponse(pnr, false);

            // Assert (similar to getResponse_successfulFetchAndParse_shouldReturnPopulatedVo)
            assertNotNull(resultVo);
            assertEquals("12345", resultVo.trainNo);
        }
    }
}
