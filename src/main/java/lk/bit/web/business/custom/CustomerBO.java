package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.CustomerDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerBO extends UserDetailsService,SuperBO {

    String saveCustomer(CustomerDTO signUpDetails);
    void checkIsEmailAndStatusEnable(String email);
    CustomerDTO findCustomerByEmail(String email);
    CustomerDTO findCustomerById(String id);
    void updateCustomer(MultipartFile multipartFile , String userData, String customerId);
    void updatePassword(String customerId, String password);
    List<CustomerDTO> readAllCustomerDetails();
    void updateCustomerStatus(String customerId, int status);
    int getTotalCountOfRegistered();
    int getTotalCountOfActive();
    int getTotalCountOfDeactivate();
    int getTotalCountOfVerified();
    int getTotalCountOfUnVerified();
    List<CustomerDTO> getCustomerDetailsByStatus(int status);
}
