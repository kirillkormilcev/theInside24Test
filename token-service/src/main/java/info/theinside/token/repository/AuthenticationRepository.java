package info.theinside.token.repository;

import info.theinside.token.model.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
    Optional<Authentication> findByName(String name);
}
