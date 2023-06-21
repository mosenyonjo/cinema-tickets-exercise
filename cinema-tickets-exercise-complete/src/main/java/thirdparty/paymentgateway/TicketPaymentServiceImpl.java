package thirdparty.paymentgateway;

public class TicketPaymentServiceImpl  implements TicketPaymentService {

    @Override
    public void makePayment(long accountId, int totalAmountToPay) {
        // Implement the logic to process the payment
        //Real implementation omitted, will assume code works and will take the payment using a card pre linked to the account.

        // This implementation assumes a successful payment without any actual payment processing logic
        System.out.println("Payment successful for account ID: " + accountId + ", Amount: £" + totalAmountToPay);
    }

}
