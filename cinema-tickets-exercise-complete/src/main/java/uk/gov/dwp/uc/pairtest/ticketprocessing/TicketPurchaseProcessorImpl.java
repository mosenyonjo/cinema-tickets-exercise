package uk.gov.dwp.uc.pairtest.ticketprocessing;

import lombok.AllArgsConstructor;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ticketvalidation.TicketPurchaseValidator;

@AllArgsConstructor
public class TicketPurchaseProcessorImpl implements TicketPurchaseProcessor {
    private final TicketPurchaseValidator ticketPurchaseValidator;
    private final PaymentProcessor paymentProcessor;
    private final SeatReservationProcessor seatReservationProcessor;

    @Override
    public void processTicketPurchase(TicketPurchaseRequest ticketPurchaseRequest) throws InvalidPurchaseException {
        ticketPurchaseValidator.validateTicketPurchaseRequest(ticketPurchaseRequest);
        paymentProcessor.makePayment(ticketPurchaseRequest);
        seatReservationProcessor.reserveSeats(ticketPurchaseRequest);
    }
}