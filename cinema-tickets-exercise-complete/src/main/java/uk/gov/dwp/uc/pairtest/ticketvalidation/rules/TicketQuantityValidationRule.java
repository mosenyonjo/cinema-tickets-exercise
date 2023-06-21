package uk.gov.dwp.uc.pairtest.ticketvalidation.rules;


import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static uk.gov.dwp.uc.pairtest.constants.ErrorConstants.INVALID_TICKET_QUANTITY_FORMAT;

public class TicketQuantityValidationRule implements TicketPurchaseValidationRule {
    @Override
    public void validate(TicketPurchaseRequest ticketPurchaseRequest) throws InvalidPurchaseException {
        for (TicketRequest ticketRequest : ticketPurchaseRequest.ticketTypeRequests()) {
            if (ticketRequest.quantity() <= 0) {
                throw new InvalidPurchaseException(String.format(INVALID_TICKET_QUANTITY_FORMAT, ticketRequest.ticketCategory()));
            }
        }
    }
}