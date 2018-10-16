package pl.jkiakumbo.cinema.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.jkiakumbo.cinema.domain.discount.Discount;
import pl.jkiakumbo.cinema.domain.discount.DiscountType;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Discount findByDiscountType(DiscountType type);
}
