package pl.jkiakumbo.cinema.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.jkiakumbo.cinema.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @EntityGraph("user.tickets")
    @Query("SELECT user FROM User user WHERE email = :email")
    User findByEmailWithTickets(@Param("email") String email);
}
