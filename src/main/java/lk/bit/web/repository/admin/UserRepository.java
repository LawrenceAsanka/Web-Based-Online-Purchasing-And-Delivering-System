package lk.bit.web.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDTO, String> {
}
