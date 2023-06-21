# Ticket Booking System

The Ticket Booking System is a Java application that facilitates the purchase of tickets for various events. It provides functionality for validating ticket purchases, processing payments, and seat reservations.

## Project Structure

The project follows a modular structure to organize the codebase effectively. Here's an overview of the main packages and their contents:

- `config`: Contains the application configuration files, such as `AppConfig`, which holds constants and configuration values.
- `constants`: Defines constant values used throughout the application, including error messages.
- `domain`: Contains domain models and entities, such as `TicketPurchaseRequest` and `TicketRequest`, representing ticket-related data structures.
- `exception`: Contains custom exception classes, such as `InvalidPurchaseException`, to handle exceptional scenarios during ticket validation and processing.
- `payment`: Contains interfaces and implementations for payment-related functionalities, such as `PaymentProcessor`, responsible for processing ticket payments.
- `seatreservation`: Contains interfaces and implementations for seat reservation functionalities, such as `SeatReservationProcessor`, responsible for managing seat reservations.
- `ticketbooking`: Contains the `TicketCategory` enum, representing different ticket categories and their corresponding prices.
- `ticketprocessing`: Contains interfaces and implementations for the ticket purchase processing pipeline, including `TicketPurchaseProcessor` and its implementation `TicketPurchaseProcessorImpl`.
- `ticketvalidation`: Contains interfaces and implementations for ticket validation functionalities, including `TicketValidator` and its implementation `TicketValidatorImpl`, which validates ticket purchase requests.
- `ticketvalidation.rules`: Contains additional ticket validation rules, implemented as separate classes, to enforce specific validation criteria.

## Usage

To use the Ticket Booking System, you can integrate it into your Java application or utilize it as a standalone library. Here's an example of how to use the system:

1. Create an instance of `TicketValidator` by initializing `TicketValidatorImpl`.
2. Create an instance of `TicketPurchaseRequest` representing the ticket purchase details, including ticket types and quantities.
3. Call the `validateTicketPurchaseRequest` method on the `TicketValidator` instance, passing the `TicketPurchaseRequest`.
4. Handle any thrown `InvalidPurchaseException` to handle invalid ticket purchases.
5. If the ticket purchase is valid, proceed with payment processing and seat reservation using the appropriate components.

Make sure to consult the JavaDoc and code documentation for detailed information on the available classes, methods, and their usage.

## Testing

The project includes unit tests to ensure the correctness of the implemented functionalities. The tests are located in the `test` directory and are organized to cover various scenarios and edge cases.

To run the tests, use your preferred testing framework (e.g., JUnit) and execute the test classes under the corresponding package directories.

## Contributing

Contributions to the Ticket Booking System are welcome! If you find any issues or have ideas for improvements, feel free to open an issue or submit a pull request. Please follow the existing coding style and conventions when contributing.
