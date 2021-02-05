package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.SignUpDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomerBO extends UserDetailsService,SuperBO {

    String saveCustomer(SignUpDTO signUpDetails);
}
