package pl.jkiakumbo.cinema.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jkiakumbo.cinema.domain.Cinema;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    Cinema findByName(String name);
}
