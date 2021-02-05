package lk.bit.web.repository;

import lk.bit.web.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {

    Optional<ConfirmationToken> findByToken(String token);
}
