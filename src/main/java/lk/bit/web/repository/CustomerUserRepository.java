package lk.bit.web.repository;

import lk.bit.web.entity.CustomerUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerUserRepository extends JpaRepository<CustomerUser, String> {

    CustomerUser getCustomerByCustomerEmail(String email);
}
