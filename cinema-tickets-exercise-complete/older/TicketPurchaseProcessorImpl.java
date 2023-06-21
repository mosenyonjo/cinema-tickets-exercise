package uk.gov.dwp.uc.pairtest.older;

import lombok.AllArgsConstructor;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ticketpricing.TicketCategory;
import uk.gov.dwp.uc.pairtest.ticketprocessing.TicketPurchaseProcessor;

import static uk.gov.dwp.uc.pairtest.config.AppConfig.MAX_TICKETS_PER_PURCHASE;
import static uk.gov.dwp.uc.pairtest.constants.ErrorConstants.*;

@AllArgsConstructor
public class TicketPurchaseProcessorImpl implements TicketPurchaseProcessor {
    private final TicketPaymentService paymentService;
    private final SeatReservationService reservationService;

    @Override
    public void processTicketPurchase(TicketPurchaseRequest ticketPurchaseRequest) throws InvalidPurchaseException {
        validateTicketPurchaseRequest(ticketPurchaseRequest);
        validateTicketQuantity(ticketPurchaseRequest);

        int totalTicketQuantity = calculateTotalTicketQuantity(ticketPurchaseRequest.ticketTypeRequests());
        validateTotalTicketQuantity(totalTicketQuantity);

        int totalAmount = calculateTotalAmount(ticketPurchaseRequest);
        makePayment(ticketPurchaseRequest.accountId(), totalAmount);

        int totalSeatsToReserve = calculateTotalSeats(ticketPurchaseRequest);
        reserveSeats(ticketPurchaseRequest.accountId(), totalSeatsToReserve, ticketPurchaseRequest);
    }

    private void validateTicketPurchaseRequest(TicketPurchaseRequest ticketPurchaseRequest) {
        if (ticketPurchaseRequest.ticketTypeRequests().length == 0) {
            throw new InvalidPurchaseException(NO_TICKET_REQUESTS);
        }
    }

    private void validateTicketQuantity(TicketPurchaseRequest ticketPurchaseRequest) {
        for (TicketRequest ticketRequest : ticketPurchaseRequest.ticketTypeRequests()) {
            validateIndividualTicketQuantity(ticketRequest);
        }
    }

    private void validateIndividualTicketQuantity(TicketRequest ticketRequest) {
        if (ticketRequest.quantity() <= 0) {
            throw new InvalidPurchaseException(String.format(INVALID_TICKET_QUANTITY_FORMAT, ticketRequest.ticketCategory()));
        }
    }

    private void validateTotalTicketQuantity(int totalTicketQuantity) {
        if (totalTicketQuantity > MAX_TICKETS_PER_PURCHASE) {
            throw new InvalidPurchaseException(String.format(EXCEEDED_MAXIMUM_QUANTITY, MAX_TICKETS_PER_PURCHASE));
        }
    }

    private int calculateTotalAmount(TicketPurchaseRequest ticketPurchaseRequest) {
        int totalAmount = 0;
        for (TicketRequest ticketRequest : ticketPurchaseRequest.ticketTypeRequests()) {
            totalAmount += ticketRequest.quantity() * ticketRequest.ticketCategory().getPrice();
        }
        return totalAmount;
    }

    private void makePayment(long accountId, int totalAmountToPay) {
        paymentService.makePayment(accountId, totalAmountToPay);
    }


    private int calculateTotalSeats(TicketPurchaseRequest ticketPurchaseRequest) {
        int totalSeats = 0;
        for (TicketRequest ticketRequest : ticketPurchaseRequest.ticketTypeRequests()) {
            totalSeats += ticketRequest.quantity();
        }
        return totalSeats;
    }

    private void reserveSeats(long accountId, int totalSeatsToAllocate, TicketPurchaseRequest ticketPurchaseRequest) {
        int adultSeatsToReserve = totalSeatsToAllocate - countInfantTickets(ticketPurchaseRequest.ticketTypeRequests());
        reservationService.reserveSeat(accountId, adultSeatsToReserve);
        // Handle seat reservation errors if needed
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

    private int calculateTotalTicketQuantity(TicketRequest[] ticketRequests) {
        int totalQuantity = 0;
        for (TicketRequest ticketRequest : ticketRequests) {
            totalQuantity += ticketRequest.quantity();
        }
        return totalQuantity;
    }

}
