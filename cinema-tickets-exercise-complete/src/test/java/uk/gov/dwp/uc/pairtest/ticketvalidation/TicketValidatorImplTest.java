package uk.gov.dwp.uc.pairtest.ticketvalidation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ticketpricing.TicketCategory;

import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.dwp.uc.pairtest.constants.ErrorConstants.ADULT_TICKET_REQUIRED;
import static uk.gov.dwp.uc.pairtest.constants.ErrorConstants.EXCEEDED_MAXIMUM_QUANTITY;

class TicketValidatorImplTest {

    private TicketValidatorImpl ticketValidatorImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ticketValidatorImpl = new TicketValidatorImpl();
    }

    @Test
    void validateTicketPurchaseRequest_ValidRequest_NoExceptionThrown() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, 2),
                new TicketRequest(TicketCategory.CHILD, 1)
        });

        // Act & Assert
        assertDoesNotThrow(() -> ticketValidatorImpl.validateTicketPurchaseRequest(request));
    }

    @Test
    void validateTicketPurchaseRequest_NoTicketRequests_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{});

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketValidatorImpl.validateTicketPurchaseRequest(request));
        assertEquals("No ticket requests found.", exception.getMessage());
    }

    @Test
    void validateTicketPurchaseRequest_InvalidTicketQuantity_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseValidator ticketValidator = new TicketValidatorImpl(); // Create an instance of the updated implementation
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, 11),
                new TicketRequest(TicketCategory.CHILD, 2)
        });

        // Act & Assert
        InvalidPurchaseException exception1 = assertThrows(InvalidPurchaseException.class,
                () -> ticketValidator.validateTicketPurchaseRequest(request));
        assertEquals(String.format(EXCEEDED_MAXIMUM_QUANTITY, 10) + " for ADULT.", exception1.getMessage());


        //Second test for child
        TicketPurchaseRequest request2 = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, 9),
                new TicketRequest(TicketCategory.CHILD, 11)
        });

        InvalidPurchaseException exception2 = assertThrows(InvalidPurchaseException.class,
                () -> ticketValidator.validateTicketPurchaseRequest(request2));
        assertEquals(String.format(EXCEEDED_MAXIMUM_QUANTITY, 10) + " for CHILD.", exception2.getMessage());
    }


    @Test
    void validateTicketPurchaseRequest_InvalidTicketCategory_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, 2),
                new TicketRequest(null, 1)
        });

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketValidatorImpl.validateTicketPurchaseRequest(request));
        assertEquals("Invalid ticket category.", exception.getMessage());
    }

    @Test
    void validateTicketPurchaseRequest_ExceedMaximumQuantity_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketRequest[] ticketRequests = new TicketRequest[21];
        for (int i = 0; i < 21; i++) {
            ticketRequests[i] = new TicketRequest(TicketCategory.ADULT, 1);
        }
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, ticketRequests);

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketValidatorImpl.validateTicketPurchaseRequest(request));
        assertEquals(String.format(EXCEEDED_MAXIMUM_QUANTITY, 10) + " for ADULT.", exception.getMessage());
    }

    @Test
    void validateTicketPurchaseRequest_NoAdultTicket_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.CHILD, 2),
                new TicketRequest(TicketCategory.INFANT, 1)
        });

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class,
                () -> ticketValidatorImpl.validateTicketPurchaseRequest(request));
        assertEquals(ADULT_TICKET_REQUIRED, exception.getMessage());
    }
}
