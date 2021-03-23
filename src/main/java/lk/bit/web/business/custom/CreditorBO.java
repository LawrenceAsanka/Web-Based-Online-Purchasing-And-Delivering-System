package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.CreditorDTO;

public interface CreditorBO extends SuperBO {

    int getCountOfNotSettleCreditByCustomer(String customerEmail);
}
