package uk.gov.dwp.uc.pairtest.ticketvalidation;

import static uk.gov.dwp.uc.pairtest.constants.ErrorConstants.*;

import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ticketpricing.TicketCategory;
import static uk.gov.dwp.uc.pairtest.config.AppConfig.*;
public class TicketValidatorImpl implements TicketPurchaseValidator {

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
            validateTicketRequest(ticketRequest);
            validateTicketQuantity(ticketRequest);

            totalTickets += ticketRequest.quantity();

            if (ticketRequest.ticketCategory() == TicketCategory.ADULT) {
                adultTicketQuantity += ticketRequest.quantity();
                adultTicketRequested = true;
            } else if (ticketRequest.ticketCategory() == TicketCategory.CHILD) {
                childTicketQuantity += ticketRequest.quantity();
            }
        }

        if (!adultTicketRequested) {
            throw new InvalidPurchaseException(ADULT_TICKET_REQUIRED);
        }

        if (adultTicketQuantity > MAXIMUM_ADULT_TICKETS_PER_PURCHASE) {
            throw new InvalidPurchaseException(String.format(EXCEEDED_MAXIMUM_QUANTITY, MAXIMUM_ADULT_TICKETS_PER_PURCHASE) + " for ADULT.");
        }

        if (childTicketQuantity > MAXIMUM_CHILD_TICKETS_PER_PURCHASE) {
            throw new InvalidPurchaseException(String.format(EXCEEDED_MAXIMUM_QUANTITY, MAXIMUM_CHILD_TICKETS_PER_PURCHASE) + " for CHILD.");
        }

        if (totalTickets > MAXIMUM_TICKETS_PER_PURCHASE) {
            throw new InvalidPurchaseException(String.format(EXCEEDED_MAXIMUM_QUANTITY, MAXIMUM_TICKETS_PER_PURCHASE));
        }
    }

    private void validateTicketRequest(TicketRequest ticketRequest) throws InvalidPurchaseException {
        if (ticketRequest.quantity() <= 0) {
            throw new InvalidPurchaseException(String.format(INVALID_TICKET_QUANTITY, ticketRequest.ticketCategory()));
        }

        if (ticketRequest.ticketCategory() == null) {
            throw new InvalidPurchaseException(INVALID_TICKET_CATEGORY);
        }
    }

    private void validateTicketQuantity(TicketRequest ticketRequest) throws InvalidPurchaseException {
        if (ticketRequest.quantity() < 1 || ticketRequest.quantity() > 20) {
            throw new InvalidPurchaseException(String.format(INVALID_TICKET_QUANTITY_FORMAT, ticketRequest.ticketCategory()));
        }
    }
}