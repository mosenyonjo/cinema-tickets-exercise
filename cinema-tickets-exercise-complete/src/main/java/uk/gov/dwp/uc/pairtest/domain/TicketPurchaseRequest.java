package uk.gov.dwp.uc.pairtest.domain;

/**
 * Should be an Immutable Object
 */
public record TicketPurchaseRequest(long accountId, TicketRequest[] ticketTypeRequests) {

}

