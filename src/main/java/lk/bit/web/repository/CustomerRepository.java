package lk.bit.web.repository;

import lk.bit.web.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer getCustomerByCustomerEmail(String email);
}
