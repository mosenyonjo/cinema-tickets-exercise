package uk.gov.dwp.uc.pairtest.ticketvalidation.rules;

import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

/**
 * The TicketPurchaseValidationRule interface represents a rule for validating a ticket purchase request.
 * Implementing classes should provide specific validation logic for a ticket purchase request.
 */
public interface TicketPurchaseValidationRule {

    /**
     * Validates the given ticket purchase request.
     *
     * @param ticketPurchaseRequest the ticket purchase request to validate
     * @throws InvalidPurchaseException if the ticket purchase request is invalid
     */
    void validate(TicketPurchaseRequest ticketPurchaseRequest) throws InvalidPurchaseException;
}