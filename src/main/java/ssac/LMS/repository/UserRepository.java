package ssac.LMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssac.LMS.domain.User;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}
