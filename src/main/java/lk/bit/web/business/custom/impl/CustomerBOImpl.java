package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.dto.SignUpDTO;
import lk.bit.web.entity.Customer;
import lk.bit.web.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerBOImpl implements CustomerBO {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void saveCustomer(SignUpDTO signUpDetails) {
    customerRepository.save(new Customer(signUpDetails.getFirstName(), signUpDetails.getLastName(),
            signUpDetails.getEmail(), signUpDetails.getPassword(), "","ACTIVE"));
    }


}
