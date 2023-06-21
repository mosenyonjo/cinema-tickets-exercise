package uk.gov.dwp.uc.pairtest.ticketpricing;

import static uk.gov.dwp.uc.pairtest.config.AppConfig.*;

public enum TicketCategory {
    INFANT(INFANT_TICKET_PRICE),
    CHILD(CHILD_TICKET_PRICE),
    ADULT(ADULT_TICKET_PRICE);

    private final int price;

    TicketCategory(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}