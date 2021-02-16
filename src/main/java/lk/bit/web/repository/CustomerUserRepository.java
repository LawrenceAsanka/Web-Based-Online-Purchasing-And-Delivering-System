package lk.bit.web.repository;

import lk.bit.web.entity.CustomerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerUserRepository extends JpaRepository<CustomerUser, String> {

    CustomerUser getCustomerByCustomerEmail(String email);

    @Query(value = "SELECT id FROM customer_user ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public String getCustomerId();
}
