package pl.jkiakumbo.cinema.service.crud;


import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jkiakumbo.cinema.domain.User;
import pl.jkiakumbo.cinema.domain.discount.Discount;
import pl.jkiakumbo.cinema.domain.discount.DiscountType;
import pl.jkiakumbo.cinema.repository.DiscountRepository;

@Service
public class DiscountService extends CRUDService<Discount> {

    private DiscountRepository discountRepository;

    @Autowired
    public DiscountService(DiscountRepository discountRepository) {
        super(discountRepository);
        this.discountRepository = discountRepository;
    }

    public Discount getDiscount(User user) {

        if (isUserNextTicketAliquotTen(user)) {
            return findByType(DiscountType.TENTH_TICKET);
        } else if (isUserBirthdayToday(user)) {
            return findByType(DiscountType.BIRTHDAY);
        } else {
            return findByType(DiscountType.NONE);
        }
    }

    public Discount findByType(DiscountType discountType) {
        return discountRepository.findByDiscountType(discountType);
    }

    private boolean isUserNextTicketAliquotTen(User user) {
        return (user.getTickets().size() + 1) % 10 == 0;
    }

    private boolean isUserBirthdayToday(User user) {
        DateTime today = new DateTime();

        if (today.getMonthOfYear() == user.getBirthday().getMonthOfYear()) {
            return today.getDayOfMonth() == user.getBirthday().getDayOfMonth();
        }

        return false;
    }
}
