package lk.bit.web.repository.admin;

import lk.bit.web.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole,String> {
}