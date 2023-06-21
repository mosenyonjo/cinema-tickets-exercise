package uk.gov.dwp.uc.pairtest.older;

import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ticketpricing.TicketCategory;
import uk.gov.dwp.uc.pairtest.ticketvalidation.TicketValidator;

import static uk.gov.dwp.uc.pairtest.constants.ErrorConstants.*;

public class TicketValidatorImpl implements TicketValidator {
    @Override
    public void validateTicketPurchaseRequest(TicketPurchaseRequest ticketPurchaseRequest) throws InvalidPurchaseException {
        TicketRequest[] ticketRequests = ticketPurchaseRequest.ticketTypeRequests();
        if (ticketRequests == null || ticketRequests.length == 0) {
            throw new InvalidPurchaseException(NO_TICKET_REQUESTS);
        }

        int totalTickets = 0;
        int adultTicketQuantity = 0;
        int childTicketQuantity = 0;
        boolean adultTicketRequested = false;


        for (TicketRequest ticketRequest : ticketRequests) {
            validateTicketQuantity(ticketRequest.ticketCategory(), ticketRequest.quantity());
            totalTickets += ticketRequest.quantity();

            if (ticketRequest.ticketCategory() == TicketCategory.ADULT) {
                adultTicketQuantity += ticketRequest.quantity();
                adultTicketRequested = true;
            } else if (ticketRequest.ticketCategory() == TicketCategory.CHILD) {
                childTicketQuantity += ticketRequest.quantity();
            }

            totalTickets += ticketRequest.quantity();

        }

        for (TicketRequest ticketRequest : ticketRequests) {
            validateTicketRequest(ticketRequest);
        }

        for (TicketRequest ticketRequest : ticketRequests) {
            validateTicketRequest(ticketRequest);
            totalTickets += ticketRequest.quantity();

            if (ticketRequest.ticketCategory() == TicketCategory.ADULT) {
                adultTicketRequested = true;
            }
        }

        if (!adultTicketRequested) {
            throw new InvalidPurchaseException(ADULT_TICKET_REQUIRED);
        }

        if (adultTicketQuantity > 10) {
            throw new InvalidPurchaseException(String.format(EXCEEDED_MAXIMUM_QUANTITY, 10) + " for ADULT.");
        }

        if (childTicketQuantity > 10) {
            throw new InvalidPurchaseException(String.format(EXCEEDED_MAXIMUM_QUANTITY, 10) + " for CHILD.");
        }

        if (totalTickets > 20) {
            throw new InvalidPurchaseException(String.format(EXCEEDED_MAXIMUM_QUANTITY, 20));
        }
    }

    private void validateTicketRequest(TicketRequest ticketRequest) {
        if (ticketRequest.quantity() <= 0) {
            throw new InvalidPurchaseException(String.format(INVALID_TICKET_QUANTITY, ticketRequest.ticketCategory()));
        }

        if (ticketRequest.ticketCategory() == null) {
            throw new InvalidPurchaseException(INVALID_TICKET_CATEGORY);
        }
    }

    private void validateTicketQuantity(TicketCategory ticketCategory, int quantity) throws InvalidPurchaseException {
        if (quantity < 1 || quantity > 20) {
            throw new InvalidPurchaseException(String.format(INVALID_TICKET_QUANTITY_FORMAT, ticketCategory));
        }
    }
}
