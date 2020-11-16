package lk.bit.web.repository;

import lk.bit.web.entity.CustomEntity;
import lk.bit.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    User findFirstLastCustomerIdByOrderByIdDesc();

    @Query(value = "SELECT u.id AS userId,u.firstName AS firstName,u.lastName AS lastName,u.address AS address," +
            " u.nic as NIC,u.contact AS contact,u.username AS username,ur.name AS userRole" +
            " FROM User u INNER JOIN u.userRole ur ORDER BY u.id")
    List<CustomEntity> getAllUsers();

    @Query(value = "SELECT u FROM User u GROUP BY u.id")
    List<User> getUsers();
}
