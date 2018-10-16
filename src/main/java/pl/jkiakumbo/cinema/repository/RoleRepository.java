package pl.jkiakumbo.cinema.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.jkiakumbo.cinema.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(String role);
}
