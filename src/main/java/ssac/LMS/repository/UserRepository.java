package ssac.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssac.LMS.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);


}
