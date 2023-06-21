package uk.gov.dwp.uc.pairtest.ticketvalidation;


import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

/**
 * The TicketPurchaseValidator interface defines the contract for validating a ticket purchase request.
 * Implementing classes should provide the implementation for validating the ticket purchase request.
 */

public interface TicketPurchaseValidator {


    /**
     * Validates the given ticket purchase request.
     *
     * @param ticketPurchaseRequest the ticket purchase request to validate
     * @throws InvalidPurchaseException if the ticket purchase request is invalid
     */
    void validateTicketPurchaseRequest(TicketPurchaseRequest ticketPurchaseRequest) throws InvalidPurchaseException;
}