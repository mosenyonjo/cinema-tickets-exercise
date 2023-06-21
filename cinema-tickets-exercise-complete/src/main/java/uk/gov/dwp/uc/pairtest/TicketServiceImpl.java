package uk.gov.dwp.uc.pairtest;

import lombok.AllArgsConstructor;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ticketprocessing.TicketPurchaseProcessor;
import uk.gov.dwp.uc.pairtest.ticketvalidation.TicketPurchaseValidator;

@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketPurchaseValidator ticketValidator;
    private final TicketPurchaseProcessor ticketPurchaseProcessor;

    @Override
    public void purchaseTickets(TicketPurchaseRequest ticketPurchaseRequest) throws InvalidPurchaseException {
        ticketValidator.validateTicketPurchaseRequest(ticketPurchaseRequest);
        ticketPurchaseProcessor.processTicketPurchase(ticketPurchaseRequest);
    }
}



