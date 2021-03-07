package lk.bit.web.repository;

import lk.bit.web.entity.CustomerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerUserRepository extends JpaRepository<CustomerUser, String> {

    CustomerUser getCustomerByCustomerEmail(String email);

    @Query(value = "SELECT id FROM customer_user ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public String getCustomerId();

    @Query(value = "SELECT COUNT(*) FROM customer_user", nativeQuery = true)
    public int getTotalCountOfRegisteredCustomers();

    @Query(value = "SELECT COUNT(*) FROM customer_user WHERE account_status=1", nativeQuery = true)
    public int getTotalCountOfActiveCustomers();

    @Query(value = "SELECT COUNT(*) FROM customer_user WHERE account_status=0", nativeQuery = true)
    public int getTotalCountOfDeactivateCustomers();

    @Query(value = "SELECT COUNT(*) FROM customer_user WHERE email_verified=1", nativeQuery = true)
    public int getTotalCountOfVerifiedCustomers();

    @Query(value = "SELECT COUNT(*) FROM customer_user WHERE email_verified=0", nativeQuery = true)
    public int getTotalCountOfUnVerifiedCustomers();

    @Query(value = "SELECT * FROM customer_user WHERE account_status=1", nativeQuery = true)
    public List<CustomerUser> getCustomerDetailsByStatusActive();

    @Query(value = "SELECT * FROM customer_user WHERE account_status=0", nativeQuery = true)
    public List<CustomerUser> getCustomerDetailsByStatusDeactivate();

    @Query(value = "SELECT * FROM customer_user WHERE email_verified=1", nativeQuery = true)
    public List<CustomerUser> getCustomerDetailsByStatusVerified();

    @Query(value = "SELECT * FROM customer_user WHERE email_verified=0", nativeQuery = true)
    public List<CustomerUser> getCustomerDetailsByStatusUnVerified();
}
