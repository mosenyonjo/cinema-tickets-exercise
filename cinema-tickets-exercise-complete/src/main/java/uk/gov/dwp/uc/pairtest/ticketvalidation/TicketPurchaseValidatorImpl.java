package uk.gov.dwp.uc.pairtest.ticketvalidation;

import lombok.AllArgsConstructor;
import uk.gov.dwp.uc.pairtest.constants.ErrorConstants;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ticketvalidation.rules.TicketPurchaseValidationRule;

@AllArgsConstructor
public class TicketPurchaseValidatorImpl implements TicketPurchaseValidator {
    private final TicketPurchaseValidationRule[] validationRules;

    @Override
    public void validateTicketPurchaseRequest(TicketPurchaseRequest ticketPurchaseRequest) throws InvalidPurchaseException {
        for (TicketPurchaseValidationRule rule : validationRules) {
            rule.validate(ticketPurchaseRequest);
        }
        validateEmptyRequest(ticketPurchaseRequest);
    }

    private void validateEmptyRequest(TicketPurchaseRequest ticketPurchaseRequest) throws InvalidPurchaseException {
        if (ticketPurchaseRequest.ticketTypeRequests().length == 0) {
            throw new InvalidPurchaseException(ErrorConstants.NO_TICKET_REQUESTS);
        }
    }
}
