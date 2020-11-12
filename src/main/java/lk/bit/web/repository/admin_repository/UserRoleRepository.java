package lk.bit.web.repository.admin_repository;

import lk.bit.web.entity.admin_entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
}
