package uk.gov.dwp.uc.pairtest.ticketprocessing;

import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;

/**
 * This interface defines the contract for processing ticket purchases.
 * Implementing classes are responsible for handling the ticket purchase logic.
 */
public interface TicketPurchaseProcessor {

    /**
     * Processes the ticket purchase request.
     *
     * @param ticketPurchaseRequest The ticket purchase request to be processed.
     */
    void processTicketPurchase(TicketPurchaseRequest ticketPurchaseRequest);
}
