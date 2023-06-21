package uk.gov.dwp.uc.pairtest.ticketprocessing;

import lombok.AllArgsConstructor;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;
import uk.gov.dwp.uc.pairtest.ticketpricing.TicketCategory;

@AllArgsConstructor
public class SeatReservationProcessor {
    private final SeatReservationService reservationService;

     public void reserveSeats(TicketPurchaseRequest ticketPurchaseRequest) {
        int totalSeatsToReserve = calculateTotalSeats(ticketPurchaseRequest);
        int adultSeatsToReserve = totalSeatsToReserve - countInfantTickets(ticketPurchaseRequest.ticketTypeRequests());
        reservationService.reserveSeat(ticketPurchaseRequest.accountId(), adultSeatsToReserve);
    }

    private int calculateTotalSeats(TicketPurchaseRequest ticketPurchaseRequest) {
        int totalSeats = 0;
        for (TicketRequest ticketRequest : ticketPurchaseRequest.ticketTypeRequests()) {
            totalSeats += ticketRequest.quantity();
        }
        return totalSeats;
    }

    private int countInfantTickets(TicketRequest[] ticketRequests) {
        int infantTicketCount = 0;
        for (TicketRequest ticketRequest : ticketRequests) {
            if (ticketRequest.ticketCategory() == TicketCategory.INFANT) {
                infantTicketCount += ticketRequest.quantity();
            }
        }
        return infantTicketCount;
    }
}
