package lk.bit.web.repository;

import lk.bit.web.entity.CustomEntity;
import lk.bit.web.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SystemUserRepository extends JpaRepository<SystemUser, String> {

    @Query(value = "SELECT su.id FROM system_user su ORDER BY su.id DESC LIMIT 1", nativeQuery = true)
    String getLastUserId();

    @Query(value = "SELECT u.id AS userId,u.firstName AS firstName,u.lastName AS lastName,u.address AS address," +
            " u.nic as NIC,u.contact AS contact,u.username AS username,u.role AS userRole,u.status AS userStatus" +
            " FROM SystemUser u ORDER BY u.id")
    List<CustomEntity> getAllUsers();

    @Query(value = "SELECT SU.* FROM system_user SU WHERE role='ROLE_SALESPERSON' ORDER BY id" ,nativeQuery = true)
    List<SystemUser> getSystemUsersByRoleSalesPerson();

    @Query(value = "SELECT * FROM system_user WHERE username=?1",nativeQuery = true)
    SystemUser findSystemUser(String userName);

}
