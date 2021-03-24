package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.CreditorBO;
import lk.bit.web.dto.CreditDetailDTO;
import lk.bit.web.entity.AssignCredit;
import lk.bit.web.entity.CreditDetail;
import lk.bit.web.entity.CustomerUser;
import lk.bit.web.entity.SystemUser;
import lk.bit.web.repository.AssignCreditRepository;
import lk.bit.web.repository.CreditorRepository;
import lk.bit.web.repository.CustomerUserRepository;
import lk.bit.web.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CreditorBOImpl implements CreditorBO {
    @Autowired
    private CreditorRepository creditorRepository;
    @Autowired
    private CustomerUserRepository customerUserRepository;
    @Autowired
    private AssignCreditRepository assignCreditRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;

    @Override
    public int getCountOfNotSettleCreditByCustomer(String customerEmail) {
        CustomerUser customer = customerUserRepository.getCustomerByCustomerEmail(customerEmail);

        return creditorRepository.getCountOfNotSettleCreditByCustomer(customer.getCustomerId());
    }

    @Override
    public List<CreditDetailDTO> readAllCreditorDetails() {
        List<CreditDetail> creditDetailList = creditorRepository.findAll();
        List<CreditDetailDTO> creditDetailDTOList = new ArrayList<>();

        for (CreditDetail creditDetail : creditDetailList) {
            if (creditDetail.getOrderInvoice().getStatus() >= 2) {

                CreditDetailDTO creditDetailDTO = new CreditDetailDTO();
                creditDetailDTO.setId(creditDetail.getId());
                creditDetailDTO.setNicFrontImage(creditDetail.getNicFrontImage());
                creditDetailDTO.setNicBackImage(creditDetail.getNicBackImage());
                creditDetailDTO.setTotalCreditAmount(creditDetail.getTotalCreditAmount().toString());

                String creditDate = creditDetail.getCreditDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                String lastDateToSettle = creditDetail.getLastDateToSettle().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

                creditDetailDTO.setCreditDate(creditDate);
                creditDetailDTO.setLastDateToSettle(lastDateToSettle);
                creditDetailDTO.setIsEmailSent(creditDetail.getIsEmailSent());
                creditDetailDTO.setIsSettle(creditDetail.getIsSettle());
                creditDetailDTO.setCustomerId(creditDetail.getCustomerUser().getCustomerId());

                creditDetailDTOList.add(creditDetailDTO);
            }
        }

        return creditDetailDTOList;
    }

    @Override
    public List<CreditDetailDTO> readAllCreditorDetailsByDateAndStatus() {
        List<CreditDetail> creditDetailList = creditorRepository.findAll();
        List<CreditDetailDTO> creditDetailDTOList = new ArrayList<>();

        for (CreditDetail creditDetail : creditDetailList) {
            if (creditDetail.getLastDateToSettle().toLocalDate().equals(LocalDateTime.now().toLocalDate()) && creditDetail.getIsAssigned() == 0 &&
            creditDetail.getOrderInvoice().getStatus() == 5) {

                CreditDetailDTO creditDetailDTO = new CreditDetailDTO();
                creditDetailDTO.setId(creditDetail.getId());
                creditDetailDTO.setNicFrontImage(creditDetail.getNicFrontImage());
                creditDetailDTO.setNicBackImage(creditDetail.getNicBackImage());
                creditDetailDTO.setTotalCreditAmount(creditDetail.getTotalCreditAmount().toString());

                String creditDate = creditDetail.getCreditDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                String lastDateToSettle = creditDetail.getLastDateToSettle().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

                creditDetailDTO.setCreditDate(creditDate);
                creditDetailDTO.setLastDateToSettle(lastDateToSettle);
                creditDetailDTO.setIsEmailSent(creditDetail.getIsEmailSent());
                creditDetailDTO.setIsSettle(creditDetail.getIsSettle());
                creditDetailDTO.setCustomerId(creditDetail.getCustomerUser().getCustomerId());

                creditDetailDTOList.add(creditDetailDTO);
            }
        }
        return creditDetailDTOList;
    }

    @Override
    public List<CreditDetailDTO> readAllCreditDetailsByEmailNotSent() {
        List<CreditDetail> creditDetailList = creditorRepository.findAll();
        List<CreditDetailDTO> creditDetailDTOList = new ArrayList<>();

        for (CreditDetail creditDetail : creditDetailList) {
            if (creditDetail.getIsEmailSent() == 0) {
                CreditDetailDTO creditDetailDTO = new CreditDetailDTO();
                creditDetailDTO.setId(creditDetail.getId());
                creditDetailDTO.setNicFrontImage(creditDetail.getNicFrontImage());
                creditDetailDTO.setNicBackImage(creditDetail.getNicBackImage());
                creditDetailDTO.setTotalCreditAmount(creditDetail.getTotalCreditAmount().toString());

                String creditDate = creditDetail.getCreditDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                String lastDateToSettle = creditDetail.getLastDateToSettle().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

                creditDetailDTO.setCreditDate(creditDate);
                creditDetailDTO.setLastDateToSettle(lastDateToSettle);
                creditDetailDTO.setIsEmailSent(creditDetail.getIsEmailSent());
                creditDetailDTO.setIsSettle(creditDetail.getIsSettle());
                creditDetailDTO.setCustomerId(creditDetail.getCustomerUser().getCustomerId());

                creditDetailDTOList.add(creditDetailDTO);
            }
        }
        return creditDetailDTOList;
    }

    @Override
    public List<CreditDetailDTO> readAllCreditDetailsByEmailSent() {
        List<CreditDetail> creditDetailList = creditorRepository.findAll();
        List<CreditDetailDTO> creditDetailDTOList = new ArrayList<>();

        for (CreditDetail creditDetail : creditDetailList) {
            if (creditDetail.getIsEmailSent() == 1) {
                CreditDetailDTO creditDetailDTO = new CreditDetailDTO();
                creditDetailDTO.setId(creditDetail.getId());
                creditDetailDTO.setNicFrontImage(creditDetail.getNicFrontImage());
                creditDetailDTO.setNicBackImage(creditDetail.getNicBackImage());
                creditDetailDTO.setTotalCreditAmount(creditDetail.getTotalCreditAmount().toString());

                String creditDate = creditDetail.getCreditDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                String lastDateToSettle = creditDetail.getLastDateToSettle().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

                creditDetailDTO.setCreditDate(creditDate);
                creditDetailDTO.setLastDateToSettle(lastDateToSettle);
                creditDetailDTO.setIsEmailSent(creditDetail.getIsEmailSent());
                creditDetailDTO.setIsSettle(creditDetail.getIsSettle());
                creditDetailDTO.setCustomerId(creditDetail.getCustomerUser().getCustomerId());

                creditDetailDTOList.add(creditDetailDTO);
            }
        }
        return creditDetailDTOList;
    }

    @Override
    public List<CreditDetailDTO> readAllCreditDetailsByIsPaid() {
        List<CreditDetail> creditDetailList = creditorRepository.findAll();
        List<CreditDetailDTO> creditDetailDTOList = new ArrayList<>();

        for (CreditDetail creditDetail : creditDetailList) {
            if (creditDetail.getIsSettle() == 1) {
                CreditDetailDTO creditDetailDTO = new CreditDetailDTO();
                creditDetailDTO.setId(creditDetail.getId());
                creditDetailDTO.setNicFrontImage(creditDetail.getNicFrontImage());
                creditDetailDTO.setNicBackImage(creditDetail.getNicBackImage());
                creditDetailDTO.setTotalCreditAmount(creditDetail.getTotalCreditAmount().toString());

                String creditDate = creditDetail.getCreditDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                String lastDateToSettle = creditDetail.getLastDateToSettle().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

                creditDetailDTO.setCreditDate(creditDate);
                creditDetailDTO.setLastDateToSettle(lastDateToSettle);
                creditDetailDTO.setIsEmailSent(creditDetail.getIsEmailSent());
                creditDetailDTO.setIsSettle(creditDetail.getIsSettle());
                creditDetailDTO.setCustomerId(creditDetail.getCustomerUser().getCustomerId());

                creditDetailDTOList.add(creditDetailDTO);
            }
        }
        return creditDetailDTOList;
    }

    @Override
    public List<CreditDetailDTO> readAllCreditDetailsByIsNotPaid() {
        List<CreditDetail> creditDetailList = creditorRepository.findAll();
        List<CreditDetailDTO> creditDetailDTOList = new ArrayList<>();

        for (CreditDetail creditDetail : creditDetailList) {
            if (creditDetail.getIsSettle() == 0) {
                CreditDetailDTO creditDetailDTO = new CreditDetailDTO();
                creditDetailDTO.setId(creditDetail.getId());
                creditDetailDTO.setNicFrontImage(creditDetail.getNicFrontImage());
                creditDetailDTO.setNicBackImage(creditDetail.getNicBackImage());
                creditDetailDTO.setTotalCreditAmount(creditDetail.getTotalCreditAmount().toString());

                String creditDate = creditDetail.getCreditDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                String lastDateToSettle = creditDetail.getLastDateToSettle().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

                creditDetailDTO.setCreditDate(creditDate);
                creditDetailDTO.setLastDateToSettle(lastDateToSettle);
                creditDetailDTO.setIsEmailSent(creditDetail.getIsEmailSent());
                creditDetailDTO.setIsSettle(creditDetail.getIsSettle());
                creditDetailDTO.setCustomerId(creditDetail.getCustomerUser().getCustomerId());

                creditDetailDTOList.add(creditDetailDTO);
            }
        }
        return creditDetailDTOList;
    }

    @Override
    public List<CreditDetailDTO> readAllCreditDetailsByFilter(int filterStatus) {
        List<CreditDetailDTO> creditDetailDTOList = new ArrayList<>();
        if (filterStatus == 0) {
            creditDetailDTOList = readAllCreditDetailsByEmailSent();
        } else if (filterStatus == 1) {
            creditDetailDTOList = readAllCreditDetailsByEmailNotSent();
        } else if (filterStatus == 2) {
            creditDetailDTOList = readAllCreditDetailsByIsPaid();
        } else if (filterStatus == 3) {
            creditDetailDTOList = readAllCreditDetailsByIsNotPaid();
        } else if (filterStatus == 4) {
            creditDetailDTOList = readAllCreditorDetails();
        }
        return creditDetailDTOList;
    }

    @Override
    public void saveCreditAssign(String creditArray, String assignTo) {
        Optional<SystemUser> optionalSystemUser = systemUserRepository.findById(assignTo);

        String[] splitCreditId = creditArray.split(",");

        for (String creditId : splitCreditId) {
            Optional<CreditDetail> optionalCreditor = creditorRepository.findById(creditId);
            if (optionalCreditor.isPresent() && optionalSystemUser.isPresent()) {
                assignCreditRepository.save(new AssignCredit(optionalCreditor.get(), optionalSystemUser.get()));

                optionalCreditor.get().setIsAssigned(1);
                creditorRepository.save(optionalCreditor.get());
            }
        }
    }
}
