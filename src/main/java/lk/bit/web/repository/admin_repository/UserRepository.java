package lk.bit.web.repository.admin_repository;

import lk.bit.web.entity.admin_entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findFirstLastCustomerIdByOrderByIdDesc();
}
