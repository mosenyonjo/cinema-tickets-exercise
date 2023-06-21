package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ticketpricing.TicketCategory;
import uk.gov.dwp.uc.pairtest.ticketprocessing.SeatReservationProcessor;
import uk.gov.dwp.uc.pairtest.ticketprocessing.TicketPurchaseProcessor;
import uk.gov.dwp.uc.pairtest.ticketprocessing.TicketPurchaseProcessorImpl;
import uk.gov.dwp.uc.pairtest.ticketvalidation.TicketPurchaseValidator;
import uk.gov.dwp.uc.pairtest.ticketvalidation.TicketValidatorImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.dwp.uc.pairtest.constants.ErrorConstants.*;

class TicketServiceImplTest {


    @Mock
    private TicketPurchaseValidator ticketValidator;

    @Mock
    private TicketPurchaseProcessor ticketProcessor;

    private TicketServiceImpl ticketService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ticketService = new TicketServiceImpl(ticketValidator, ticketProcessor);
    }

    @Test
    void purchaseTickets_ValidRequest_Success() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, 2),
                new TicketRequest(TicketCategory.CHILD, 1)
        });

        // Act & Assert
        assertDoesNotThrow(() -> ticketService.purchaseTickets(request));
        verify(ticketValidator, times(1)).validateTicketPurchaseRequest(request);
        verify(ticketProcessor, times(1)).processTicketPurchase(request);
    }

    @Test
    void purchaseTickets_InvalidRequest_ThrowsInvalidPurchaseException() {
        // Arrange & Act
        InvalidPurchaseException exception1 = assertThrows(InvalidPurchaseException.class, () ->
                new TicketRequest(TicketCategory.CHILD, 0));
        assertEquals(NEGATIVE_QUANTITY, exception1.getMessage());

        InvalidPurchaseException exception2 = assertThrows(InvalidPurchaseException.class, () ->
                new TicketRequest(TicketCategory.ADULT, -1));
        assertEquals(NEGATIVE_QUANTITY, exception2.getMessage());
    }

    @Test
    void purchaseTickets_InfantTicketRequestWithoutAdult_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseValidator ticketValidator = new TicketValidatorImpl(); // Create an instance of the updated TicketValidator
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.INFANT, 1),
                new TicketRequest(TicketCategory.CHILD, 1)
        });

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketPurchaseRequest(request));

        assertEquals(ADULT_TICKET_REQUIRED, exception.getMessage());
    }


    @Test
    void purchaseTickets_ExceedMaximumTicketsPerPurchase_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseValidator ticketValidator = new TicketValidatorImpl(); // Create an instance of the updated TicketValidator
        TicketPurchaseValidator purchaseValidatorMock = Mockito.mock(TicketPurchaseValidator.class); // Create a mock of TicketPurchaseValidator
        uk.gov.dwp.uc.pairtest.ticketprocessing.PaymentProcessor paymentProcessorMock = Mockito.mock(uk.gov.dwp.uc.pairtest.ticketprocessing.PaymentProcessor.class); // Create a mock of PaymentProcessor
        SeatReservationProcessor seatReservationProcessorMock = Mockito.mock(SeatReservationProcessor.class); // Create a mock of SeatReservationProcessor
        TicketPurchaseProcessor ticketPurchaseProcessor = new TicketPurchaseProcessorImpl(purchaseValidatorMock, paymentProcessorMock, seatReservationProcessorMock); // Use the updated TicketPurchaseProcessor
        TicketService ticketService = new TicketServiceImpl(ticketValidator, ticketPurchaseProcessor); // Use the updated TicketServiceImpl

        TicketRequest[] ticketRequests = new TicketRequest[21];
        for (int i = 0; i < 21; i++) {
            ticketRequests[i] = new TicketRequest(TicketCategory.ADULT, 1);
        }
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, ticketRequests);

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () ->
                ticketService.purchaseTickets(request));

        assertEquals(String.format(EXCEEDED_MAXIMUM_QUANTITY + " for ADULT.", 10), exception.getMessage());
    }


    @Test
    void purchaseTickets_WithoutAdultTicket_ThrowsInvalidPurchaseException() {
        TicketPurchaseValidator ticketValidator = new TicketValidatorImpl(); // Create an instance of the updated TicketValidator


        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.CHILD, 2),
                new TicketRequest(TicketCategory.INFANT, 1)
        });

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketPurchaseRequest(request));

        assertEquals(ADULT_TICKET_REQUIRED, exception.getMessage());
    }

    @Test
    void purchaseTickets_ExceedMaximumQuantity_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseValidator ticketValidator = new TicketValidatorImpl(); // Create an instance of the updated TicketValidator

        TicketRequest[] ticketRequests = new TicketRequest[21];
        for (int i = 0; i < 21; i++) {
            ticketRequests[i] = new TicketRequest(TicketCategory.ADULT, 1);
        }
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, ticketRequests);

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketPurchaseRequest(request));
        assertEquals(String.format(EXCEEDED_MAXIMUM_QUANTITY + " for ADULT.", 10), exception.getMessage());
    }

    @Test
    void purchaseTickets_InfantTicketsWithoutAdult_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseValidator ticketValidator = new TicketValidatorImpl(); // Create an instance of the updated TicketValidator

        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.INFANT, 2),
                new TicketRequest(TicketCategory.CHILD, 1)
        });

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketPurchaseRequest(request));
        assertEquals(ADULT_TICKET_REQUIRED, exception.getMessage());
    }

    @Test
    void purchaseTickets_InfantTicketsWithAdult_Success() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, 2),
                new TicketRequest(TicketCategory.INFANT, 2)
        });

        // Act & Assert
        assertDoesNotThrow(() -> ticketService.purchaseTickets(request));
        verify(ticketProcessor, times(1)).processTicketPurchase(request);
    }

    @Test
    void purchaseTickets_EmptyRequest_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseValidator ticketValidator = new TicketValidatorImpl(); // Create an instance of the updated TicketValidator

        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{});

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketPurchaseRequest(request));
        assertEquals(NO_TICKET_REQUESTS, exception.getMessage());
    }

    @Test
    void purchaseTickets_InvalidTicketCategory_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseValidator ticketValidator = new TicketValidatorImpl(); // Create an instance of the updated TicketValidator

        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, 2),
                new TicketRequest(null, 1) // Invalid ticket category
        });

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketPurchaseRequest(request));
        assertEquals(INVALID_TICKET_CATEGORY, exception.getMessage());
    }

    @Test
    void purchaseTickets_max_InvalidTicketRequest_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseValidator ticketValidator = new TicketValidatorImpl(); // Create an instance of the updated TicketValidator

        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, Integer.MAX_VALUE),  // Invalid ticket quantity
                new TicketRequest(TicketCategory.CHILD, Integer.MAX_VALUE),  // Exceed maximum quantity
                new TicketRequest(TicketCategory.INFANT, Integer.MAX_VALUE)  // Exceed maximum quantity
        });

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () ->
                ticketValidator.validateTicketPurchaseRequest(request));
        assertEquals(String.format("Invalid ticket quantity for ADULT.", 10), exception.getMessage());
    }

}
