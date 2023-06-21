package uk.gov.dwp.uc.pairtest.domain;

import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ticketpricing.TicketCategory;

import static uk.gov.dwp.uc.pairtest.constants.ErrorConstants.NEGATIVE_QUANTITY;

/**
 * Should be an Immutable Object
 */
public record TicketRequest(TicketCategory ticketCategory, int quantity) {

    public TicketRequest {
        if (quantity <= 0) {
            throw new InvalidPurchaseException(NEGATIVE_QUANTITY);
        }
    }

    @Override
    public String toString() {
        return "TicketRequest{" +
                "ticketCategory=" + ticketCategory +
                ", quantity=" + quantity +
                '}';
    }
}

