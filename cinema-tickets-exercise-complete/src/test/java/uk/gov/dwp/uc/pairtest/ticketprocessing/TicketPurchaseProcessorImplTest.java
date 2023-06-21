package uk.gov.dwp.uc.pairtest.ticketprocessing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ticketpricing.TicketCategory;
import uk.gov.dwp.uc.pairtest.ticketvalidation.TicketPurchaseValidator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static uk.gov.dwp.uc.pairtest.constants.ErrorConstants.NO_TICKET_REQUESTS;

class TicketPurchaseProcessorImplTest {
    @Mock
    private TicketPaymentService paymentService;

    @Mock
    private SeatReservationService reservationService;

    @Mock
    private TicketPurchaseValidator purchaseValidator;

    @Mock
    private PaymentProcessor paymentProcessor;

    @Mock
    private SeatReservationProcessor seatReservationProcessor;

    private TicketPurchaseProcessorImpl ticketProcessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        doNothing().when(purchaseValidator).validateTicketPurchaseRequest(any());
        ticketProcessor = new TicketPurchaseProcessorImpl(purchaseValidator, paymentProcessor, seatReservationProcessor);
    }

    @Test
    void processTicketPurchase_ValidRequest_Success() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, 2),
                new TicketRequest(TicketCategory.CHILD, 1)
        });

        // Act & Assert
        assertDoesNotThrow(() -> ticketProcessor.processTicketPurchase(request));
        verify(purchaseValidator, times(1)).validateTicketPurchaseRequest(request);
        verify(paymentProcessor, times(1)).makePayment(request);
        verify(seatReservationProcessor, times(1)).reserveSeats(request);
    }

    @Test
    void processTicketPurchase_MaximumQuantityAndTypes_Success() {
        // Arrange
        TicketRequest[] ticketRequests = new TicketRequest[20];
        for (int i = 0; i < 10; i++) {
            ticketRequests[i] = new TicketRequest(TicketCategory.ADULT, 1);
            ticketRequests[i + 10] = new TicketRequest(TicketCategory.CHILD, 1);
        }
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, ticketRequests);

        // Act & Assert
        assertDoesNotThrow(() -> ticketProcessor.processTicketPurchase(request));
        verify(purchaseValidator, times(1)).validateTicketPurchaseRequest(request);
        verify(paymentProcessor, times(1)).makePayment(request);
        verify(seatReservationProcessor, times(1)).reserveSeats(request);
    }

    @Test
    void processTicketPurchase_ValidQuantitiesAndTypes_Success() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, 2),
                new TicketRequest(TicketCategory.CHILD, 1),
                new TicketRequest(TicketCategory.INFANT, 1)
        });

        // Act & Assert
        assertDoesNotThrow(() -> ticketProcessor.processTicketPurchase(request));
        verify(purchaseValidator, times(1)).validateTicketPurchaseRequest(request);
        verify(paymentProcessor, times(1)).makePayment(request);
        verify(seatReservationProcessor, times(1)).reserveSeats(request);
    }

    @Test
    void processTicketPurchase_InfantTicketsWithAdult_Success() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, 2),
                new TicketRequest(TicketCategory.INFANT, 2)
        });

        // Act & Assert
        assertDoesNotThrow(() -> ticketProcessor.processTicketPurchase(request));
        verify(purchaseValidator, times(1)).validateTicketPurchaseRequest(request);
        verify(paymentProcessor, times(1)).makePayment(request);
        verify(seatReservationProcessor, times(1)).reserveSeats(request);
    }


    @Test
    void processTicketPurchase_EmptyRequest_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{});

        // Mock the behavior of the purchaseValidator
        doThrow(new InvalidPurchaseException(NO_TICKET_REQUESTS))
                .when(purchaseValidator).validateTicketPurchaseRequest(request);

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () ->
                ticketProcessor.processTicketPurchase(request));
        assertEquals(NO_TICKET_REQUESTS, exception.getMessage());
        verifyNoInteractions(paymentProcessor, seatReservationProcessor);
    }
}
