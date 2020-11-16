package lk.bit.web.repository;

import lk.bit.web.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    
    UserRole findByName(String role);
}
