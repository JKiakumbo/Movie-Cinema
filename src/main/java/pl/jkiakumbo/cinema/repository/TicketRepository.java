package pl.jkiakumbo.cinema.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.jkiakumbo.cinema.domain.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
