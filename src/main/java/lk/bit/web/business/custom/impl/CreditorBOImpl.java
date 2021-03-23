package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.CreditorBO;
import lk.bit.web.dto.CreditorDTO;
import lk.bit.web.entity.CustomerUser;
import lk.bit.web.repository.CreditorRepository;
import lk.bit.web.repository.CustomerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreditorBOImpl implements CreditorBO {
    @Autowired
    private CreditorRepository creditorRepository;
    @Autowired
    private CustomerUserRepository customerUserRepository;

    @Override
    public int getCountOfNotSettleCreditByCustomer(String customerEmail) {
        CustomerUser customer = customerUserRepository.getCustomerByCustomerEmail(customerEmail);
        int countOfNotSettleCreditByCustomer = creditorRepository.getCountOfNotSettleCreditByCustomer(customer.getCustomerId());
        System.out.println(countOfNotSettleCreditByCustomer);
        return countOfNotSettleCreditByCustomer;
    }
}
