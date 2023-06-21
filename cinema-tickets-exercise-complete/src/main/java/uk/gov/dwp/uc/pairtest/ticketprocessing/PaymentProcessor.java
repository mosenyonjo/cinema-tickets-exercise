package uk.gov.dwp.uc.pairtest.ticketprocessing;

import lombok.AllArgsConstructor;
import thirdparty.paymentgateway.TicketPaymentService;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketRequest;

@AllArgsConstructor
public class PaymentProcessor {
    private final TicketPaymentService paymentService;

    public void makePayment(TicketPurchaseRequest ticketPurchaseRequest) {
        int totalAmount = calculateTotalAmount(ticketPurchaseRequest);
        paymentService.makePayment(ticketPurchaseRequest.accountId(), totalAmount);
    }

    private int calculateTotalAmount(TicketPurchaseRequest ticketPurchaseRequest) {
        int totalAmount = 0;
        for (TicketRequest ticketRequest : ticketPurchaseRequest.ticketTypeRequests()) {
            totalAmount += ticketRequest.quantity() * ticketRequest.ticketCategory().getPrice();
        }
        return totalAmount;
    }
}
