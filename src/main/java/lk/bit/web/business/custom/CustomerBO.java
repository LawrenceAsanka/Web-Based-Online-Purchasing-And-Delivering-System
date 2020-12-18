package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.SignUpDTO;

public interface CustomerBO extends SuperBO {

    void saveCustomer(SignUpDTO signUpDetails);
}
