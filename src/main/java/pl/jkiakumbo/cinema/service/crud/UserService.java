package pl.jkiakumbo.cinema.service.crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jkiakumbo.cinema.domain.Role;
import pl.jkiakumbo.cinema.domain.User;
import pl.jkiakumbo.cinema.dto.UserDTO;
import pl.jkiakumbo.cinema.repository.RoleRepository;
import pl.jkiakumbo.cinema.repository.UserRepository;

@Service
public class UserService extends CRUDService<User> {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByEmailWithTickets(String email) {
        return userRepository.findByEmailWithTickets(email);
    }

    @Transactional
    public User registerNewUser(UserDTO userDTO) {

        User user = new User(userDTO.getEmail(),
                bCryptPasswordEncoder.encode(userDTO.getPassword()),
                userDTO.getBirthday(), userDTO.getBill());

        Role role = roleRepository.findByRole("ROLE_USER");
        role.addUser(user);

        user.setRole(role);

        return save(user);
    }

    @Transactional
    public void additionBill(User user, Double amount) {
        user.additionBill(amount);
        save(user);
    }
}
