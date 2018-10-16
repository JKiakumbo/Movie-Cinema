package pl.jkiakumbo.cinema.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jkiakumbo.cinema.domain.Event;
import pl.jkiakumbo.cinema.domain.Ticket;
import pl.jkiakumbo.cinema.domain.User;
import pl.jkiakumbo.cinema.domain.discount.Discount;
import pl.jkiakumbo.cinema.domain.discount.DiscountType;
import pl.jkiakumbo.cinema.domain.seat.Seat;
import pl.jkiakumbo.cinema.domain.seat.SeatBooked;
import pl.jkiakumbo.cinema.domain.seat.SeatType;
import pl.jkiakumbo.cinema.repository.SeatRepository;
import pl.jkiakumbo.cinema.repository.TicketRepository;
import pl.jkiakumbo.cinema.service.crud.DiscountService;
import pl.jkiakumbo.cinema.service.crud.EventService;
import pl.jkiakumbo.cinema.service.crud.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookingService {

    private TicketRepository ticketRepository;
    private SeatRepository seatRepository;
    private DiscountService discountService;
    private EventService eventService;
    private UserService userService;

    @Autowired
    public BookingService(TicketRepository ticketRepository,
                          SeatRepository seatRepository, DiscountService discountService,
                          EventService eventService,
                          UserService userService) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
        this.discountService = discountService;
        this.eventService = eventService;
        this.userService = userService;
    }

    @Transactional
    public List<Ticket> bookTicket(Event event, User user, List<Seat> seats) {

        if (checkIsSeatFree(seats, event) && getTicketsPrice(event, seats) <= user.getBill()) {

            List<Ticket> tickets = new ArrayList<>();

            for (Seat seat : seats) {

                Ticket ticket = generateTicket(event, user, seat);

                Discount discount = discountService.getDiscount(user);

                applyDiscountToTicket(ticket, discount);

                addTicketToUser(user, ticket);
                tickets.add(ticket);
            }


            seatRepository.saveAll(seats);
            userService.save(user);

            return ticketRepository.saveAll(tickets);
        } else {
            return new ArrayList<>();
        }
    }

    private Ticket generateTicket(Event event, User user, Seat seat) {
        bookSeat(event, seat);
        Double price = getPriceForSeat(seat, event.getBasicPrice());

        return new Ticket(user, price, event, seat);
    }

    private void addTicketToUser(User user, Ticket ticket) {
        user.addTicket(ticket);
        user.substractBill(ticket.getPrice());
    }

    public Double getTicketsPrice(Event event, List<Seat> seats) {
        Double eventBasicPrice = event.getBasicPrice();

        return seats
                .stream()
                .mapToDouble(seat ->
                        getPriceForSeat(seat, eventBasicPrice)).sum();
    }

    private void bookSeat(Event event, Seat seat) {

        SeatBooked seatBooked = new SeatBooked();
        seatBooked.setDateTime(event.getDate());
        seatBooked.setSeat(seat);
        seat.addBookingForSeat(seatBooked);
    }

    private void applyDiscountToTicket(Ticket ticket, Discount discount) {
        if (discount.getDiscountType() != DiscountType.NONE) {

            Double originalPrice = ticket.getPrice();

            double price = originalPrice - (originalPrice * discount.getDiscount());

            ticket.addDiscount(discount);
            ticket.setPrice(price);
        }
    }

    private boolean checkIsSeatFree(List<Seat> seats, Event event) {

        for (Seat seat : seats) {

            for (SeatBooked seatBooked : seat.getBookingForSeat()) {
                if (seatBooked.getDateTime().equals(event.getDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    private Double getPriceForSeat(Seat seat, Double eventBasePrice) {
        if (seat.getType() == SeatType.Vip) {
            return eventBasePrice * 1.2d;
        } else {
            return eventBasePrice;
        }
    }


    public List<Ticket> bookTicket(Long eventId, String email, List<Long> seatsIds) {

        Event event = eventService.findById(eventId);
        User user = userService.findByEmailWithTickets(email);
        List<Seat> seats = seatRepository.findAllWithBooking(seatsIds);

        return bookTicket(event, user, seats);
    }
}
