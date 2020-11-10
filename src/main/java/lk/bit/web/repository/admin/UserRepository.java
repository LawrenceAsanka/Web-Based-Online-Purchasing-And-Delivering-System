package lk.bit.web.repository.admin;

import lk.bit.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
