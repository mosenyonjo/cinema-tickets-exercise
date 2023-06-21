package uk.gov.dwp.uc.pairtest.ticketvalidation.rules;


import uk.gov.dwp.uc.pairtest.config.AppConfig;
import uk.gov.dwp.uc.pairtest.constants.ErrorConstants;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static uk.gov.dwp.uc.pairtest.config.AppConfig.MAXIMUM_TICKETS_PER_PURCHASE;
public class TotalTicketQuantityValidationRule implements TicketPurchaseValidationRule {
    @Override
    public void validate(TicketPurchaseRequest ticketPurchaseRequest) throws InvalidPurchaseException {
        int totalTicketQuantity = calculateTotalTicketQuantity(ticketPurchaseRequest.ticketTypeRequests());
        if (totalTicketQuantity > MAXIMUM_TICKETS_PER_PURCHASE) {
            throw new InvalidPurchaseException(String.format(ErrorConstants.EXCEEDED_MAXIMUM_QUANTITY, AppConfig.MAXIMUM_TICKETS_PER_PURCHASE));
        }
    }

    private int calculateTotalTicketQuantity(TicketRequest[] ticketRequests) {
        int totalQuantity = 0;
        for (TicketRequest ticketRequest : ticketRequests) {
            totalQuantity += ticketRequest.quantity();
        }
        return totalQuantity;
    }
}