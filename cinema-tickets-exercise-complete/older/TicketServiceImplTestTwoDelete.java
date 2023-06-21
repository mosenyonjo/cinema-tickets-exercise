package uk.gov.dwp.uc.pairtest.older;

class TicketServiceImplTestTwoDelete {
    /*
    @Mock
    private TicketValidator ticketValidator;

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
    void purchaseTickets_ValidQuantitiesAndTypes_Success() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, 2),
                new TicketRequest(TicketCategory.CHILD, 1),
                new TicketRequest(TicketCategory.INFANT, 1)
        });

        // Act & Assert
        assertDoesNotThrow(() -> ticketService.purchaseTickets(request));
        verify(paymentService, times(1)).makePayment(eq(1L), anyInt());
        verify(reservationService, times(1)).reserveSeat(eq(1L), anyInt());
    }

    @Test
    void purchaseTickets_InvalidRequest_ThrowsInvalidPurchaseException() {
        // Arrange & Act & Assert
        InvalidPurchaseException exception1 = assertThrows(InvalidPurchaseException.class, () -> {
            new TicketRequest(TicketCategory.CHILD, 0);
        });
        assertEquals(String.format(NEGATIVE_QUANTITY, TicketCategory.CHILD), exception1.getMessage());

        InvalidPurchaseException exception2 = assertThrows(InvalidPurchaseException.class, () -> {
            new TicketRequest(TicketCategory.ADULT, -1);
        });
        assertEquals(String.format(NEGATIVE_QUANTITY, TicketCategory.ADULT), exception2.getMessage());

        verifyNoInteractions(paymentService, reservationService);
    }

    @Test
    void purchaseTickets_InfantTicketRequestWithoutAdult_ThrowsInvalidTicketRequestException() {
        // Arrange
        long accountId = 12345;
        TicketRequest[] ticketRequests = {
                new TicketRequest(TicketCategory.INFANT, 1),
                new TicketRequest(TicketCategory.CHILD, 1)
        };
        TicketPurchaseRequest request = new TicketPurchaseRequest(accountId, ticketRequests);

        // Act & Assert
        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(request));
        verifyNoInteractions(paymentService, reservationService);
    }

    @Test
    void purchaseTickets_ExceedMaximumTicketsPerPurchase_ThrowsInvalidTicketRequestException() {
        // Arrange
        long accountId = 12345;
        TicketRequest[] ticketRequests = new TicketRequest[21];
        for (int i = 0; i < 21; i++) {
            ticketRequests[i] = new TicketRequest(TicketCategory.ADULT, 1);
        }
        TicketPurchaseRequest request = new TicketPurchaseRequest(accountId, ticketRequests);

        // Act & Assert
        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(request));
        verifyNoInteractions(paymentService, reservationService);
    }

    @Test
    void purchaseTickets_WithoutAdultTicket_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.CHILD, 2),
                new TicketRequest(TicketCategory.INFANT, 1)
        });

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(request));
        assertEquals(ADULT_TICKET_REQUIRED, exception.getMessage());
        verifyNoInteractions(paymentService, reservationService);
    }

    @Test
    void purchaseTickets_ExceedMaximumQuantity_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketRequest[] ticketRequests = new TicketRequest[21];
        for (int i = 0; i < 21; i++) {
            ticketRequests[i] = new TicketRequest(TicketCategory.ADULT, 1);
        }
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, ticketRequests);

        // Act & Assert
        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(request));
        verifyNoInteractions(paymentService, reservationService);
    }

    @Test
    void purchaseTickets_InfantTicketsWithoutAdult_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.INFANT, 2),
                new TicketRequest(TicketCategory.CHILD, 1)
        });

        // Act & Assert
        assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(request));
        verifyNoInteractions(paymentService, reservationService);
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
        verify(paymentService, times(1)).makePayment(eq(1L), anyInt());
        verify(reservationService, times(1)).reserveSeat(eq(1L), eq(2)); // Only reserve seats for adult tickets
        verify(reservationService, never()).reserveSeat(eq(1L), eq(0)); // Ensure infants are not allocated seats
    }

    @Test
    void purchaseTickets_EmptyRequest_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{});

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(request));
        assertEquals(NO_TICKET_REQUESTS, exception.getMessage());
        verifyNoInteractions(paymentService, reservationService);
    }

    @Test
    void purchaseTickets_MaximumAdultTickets_Success() {
        // Arrange
        long accountId = 1;
        TicketRequest[] ticketRequests = new TicketRequest[20];
        for (int i = 0; i < 20; i++) {
            ticketRequests[i] = new TicketRequest(TicketCategory.ADULT, 1);
        }
        TicketPurchaseRequest request = new TicketPurchaseRequest(accountId, ticketRequests);

        // Act & Assert
        assertDoesNotThrow(() -> ticketService.purchaseTickets(request));
        verify(paymentService, times(1)).makePayment(eq(accountId), anyInt());
        verify(reservationService, times(1)).reserveSeat(eq(accountId), anyInt());
    }

    @Test
    void purchaseTickets_InvalidTicketCategory_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, 2),
                new TicketRequest(null, 1) // Invalid ticket category
        });

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(request));
        assertEquals(INVALID_TICKET_CATEGORY, exception.getMessage());
        verifyNoInteractions(paymentService, reservationService);
    }

    @Test
    void purchaseTickets_max_InvalidTicketRequest_ThrowsInvalidPurchaseException() {
        // Arrange
        TicketPurchaseRequest request = new TicketPurchaseRequest(1, new TicketRequest[]{
                new TicketRequest(TicketCategory.ADULT, Integer.MAX_VALUE),  // Invalid ticket quantity
                new TicketRequest(TicketCategory.CHILD, Integer.MAX_VALUE),                 // Exceed maximum quantity
                new TicketRequest(TicketCategory.INFANT, Integer.MAX_VALUE)                 // Exceed maximum quantity
        });

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(request));
        assertEquals(String.format(EXCEEDED_MAXIMUM_QUANTITY, 20), exception.getMessage());
        verifyNoInteractions(paymentService, reservationService);
    }
*/
}

