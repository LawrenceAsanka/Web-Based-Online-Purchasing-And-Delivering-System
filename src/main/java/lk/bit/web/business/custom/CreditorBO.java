package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.CreditDetailDTO;
import lk.bit.web.util.tm.CreditCollectionTM;

import java.util.List;

public interface CreditorBO extends SuperBO {

    int getCountOfNotSettleCreditByCustomer(String customerEmail);
    List<CreditDetailDTO> readAllCreditorDetails();
    List<CreditDetailDTO> readAllCreditorDetailsByDateAndStatus();
    List<CreditDetailDTO> readAllCreditDetailsByEmailNotSent();
    List<CreditDetailDTO> readAllCreditDetailsByEmailSent();
    List<CreditDetailDTO> readAllCreditDetailsByIsPaid();
    List<CreditDetailDTO> readAllCreditDetailsByIsNotPaid();
    List<CreditDetailDTO> readAllCreditDetailsByFilter(int filterStatus);
    void saveCreditAssign(String creditArray, String assignTo);
    List<CreditCollectionTM> readAllCreditDetailsByAssignee(String assignee);
    void updateCreditAmountAndDate(String creditId, String extendDate, String newAmount);
    void updateIsSettleStatus(String creditId);

}
