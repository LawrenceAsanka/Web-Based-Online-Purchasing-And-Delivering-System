package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.CreditorDTO;

import java.util.List;

public interface CreditorBO extends SuperBO {

    int getCountOfNotSettleCreditByCustomer(String customerEmail);
    List<CreditorDTO> readAllCreditorDetails();
    List<CreditorDTO> readAllCreditorDetailsByDateAndStatus();
    List<CreditorDTO> readAllCreditDetailsByEmailNotSent();
    List<CreditorDTO> readAllCreditDetailsByEmailSent();
    List<CreditorDTO> readAllCreditDetailsByIsPaid();
    List<CreditorDTO> readAllCreditDetailsByIsNotPaid();
    List<CreditorDTO> readAllCreditDetailsByFilter(int filterStatus);
}
