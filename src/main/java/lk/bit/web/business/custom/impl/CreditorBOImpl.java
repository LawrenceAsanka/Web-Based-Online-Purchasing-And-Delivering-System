package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.CreditorBO;
import lk.bit.web.dto.CreditorDTO;
import lk.bit.web.entity.Creditor;
import lk.bit.web.entity.CustomerUser;
import lk.bit.web.repository.CreditorRepository;
import lk.bit.web.repository.CustomerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<CreditorDTO> readAllCreditorDetails() {
        List<Creditor> creditorList = creditorRepository.findAll();
        List<CreditorDTO> creditorDTOList = new ArrayList<>();

        for (Creditor creditor : creditorList) {
            CreditorDTO creditorDTO = new CreditorDTO();
            creditorDTO.setId(creditor.getId());
            creditorDTO.setNicFrontImage(creditor.getNicFrontImage());
            creditorDTO.setNicBackImage(creditor.getNicBackImage());
            creditorDTO.setTotalCreditAmount(creditor.getTotalCreditAmount().toString());

            String creditDate = creditor.getCredit_date().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            String lastDateToSettle = creditor.getLastDateToSettle().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

            creditorDTO.setCreditDate(creditDate);
            creditorDTO.setLastDateToSettle(lastDateToSettle);
            creditorDTO.setIsEmailSent(creditor.getIsEmailSent());
            creditorDTO.setIsSettle(creditor.getIsSettle());
            creditorDTO.setCustomerId(creditor.getCustomerUser().getCustomerId());

            creditorDTOList.add(creditorDTO);
        }

        return creditorDTOList;
    }

    @Override
    public List<CreditorDTO> readAllCreditorDetailsByDateAndStatus() {
        List<Creditor> creditorList = creditorRepository.findAll();
        List<CreditorDTO> creditorDTOList = new ArrayList<>();

        for (Creditor creditor : creditorList) {
            if (creditor.getLastDateToSettle().toLocalDate().equals(LocalDateTime.now().toLocalDate())) {
                CreditorDTO creditorDTO = new CreditorDTO();
                creditorDTO.setId(creditor.getId());
                creditorDTO.setNicFrontImage(creditor.getNicFrontImage());
                creditorDTO.setNicBackImage(creditor.getNicBackImage());
                creditorDTO.setTotalCreditAmount(creditor.getTotalCreditAmount().toString());

                String creditDate = creditor.getCredit_date().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                String lastDateToSettle = creditor.getLastDateToSettle().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

                creditorDTO.setCreditDate(creditDate);
                creditorDTO.setLastDateToSettle(lastDateToSettle);
                creditorDTO.setIsEmailSent(creditor.getIsEmailSent());
                creditorDTO.setIsSettle(creditor.getIsSettle());
                creditorDTO.setCustomerId(creditor.getCustomerUser().getCustomerId());

                creditorDTOList.add(creditorDTO);
            }
        }
        return creditorDTOList;
    }

    @Override
    public List<CreditorDTO> readAllCreditDetailsByEmailNotSent() {
        List<Creditor> creditorList = creditorRepository.findAll();
        List<CreditorDTO> creditorDTOList = new ArrayList<>();

        for (Creditor creditor : creditorList) {
            if (creditor.getIsEmailSent() == 0) {
                CreditorDTO creditorDTO = new CreditorDTO();
                creditorDTO.setId(creditor.getId());
                creditorDTO.setNicFrontImage(creditor.getNicFrontImage());
                creditorDTO.setNicBackImage(creditor.getNicBackImage());
                creditorDTO.setTotalCreditAmount(creditor.getTotalCreditAmount().toString());

                String creditDate = creditor.getCredit_date().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                String lastDateToSettle = creditor.getLastDateToSettle().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

                creditorDTO.setCreditDate(creditDate);
                creditorDTO.setLastDateToSettle(lastDateToSettle);
                creditorDTO.setIsEmailSent(creditor.getIsEmailSent());
                creditorDTO.setIsSettle(creditor.getIsSettle());
                creditorDTO.setCustomerId(creditor.getCustomerUser().getCustomerId());

                creditorDTOList.add(creditorDTO);
            }
        }
        return creditorDTOList;
    }

    @Override
    public List<CreditorDTO> readAllCreditDetailsByEmailSent() {
        List<Creditor> creditorList = creditorRepository.findAll();
        List<CreditorDTO> creditorDTOList = new ArrayList<>();

        for (Creditor creditor : creditorList) {
            if (creditor.getIsEmailSent() == 1) {
                CreditorDTO creditorDTO = new CreditorDTO();
                creditorDTO.setId(creditor.getId());
                creditorDTO.setNicFrontImage(creditor.getNicFrontImage());
                creditorDTO.setNicBackImage(creditor.getNicBackImage());
                creditorDTO.setTotalCreditAmount(creditor.getTotalCreditAmount().toString());

                String creditDate = creditor.getCredit_date().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                String lastDateToSettle = creditor.getLastDateToSettle().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

                creditorDTO.setCreditDate(creditDate);
                creditorDTO.setLastDateToSettle(lastDateToSettle);
                creditorDTO.setIsEmailSent(creditor.getIsEmailSent());
                creditorDTO.setIsSettle(creditor.getIsSettle());
                creditorDTO.setCustomerId(creditor.getCustomerUser().getCustomerId());

                creditorDTOList.add(creditorDTO);
            }
        }
        return creditorDTOList;
    }

    @Override
    public List<CreditorDTO> readAllCreditDetailsByIsPaid() {
        List<Creditor> creditorList = creditorRepository.findAll();
        List<CreditorDTO> creditorDTOList = new ArrayList<>();

        for (Creditor creditor : creditorList) {
            if (creditor.getIsSettle() == 1) {
                CreditorDTO creditorDTO = new CreditorDTO();
                creditorDTO.setId(creditor.getId());
                creditorDTO.setNicFrontImage(creditor.getNicFrontImage());
                creditorDTO.setNicBackImage(creditor.getNicBackImage());
                creditorDTO.setTotalCreditAmount(creditor.getTotalCreditAmount().toString());

                String creditDate = creditor.getCredit_date().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                String lastDateToSettle = creditor.getLastDateToSettle().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

                creditorDTO.setCreditDate(creditDate);
                creditorDTO.setLastDateToSettle(lastDateToSettle);
                creditorDTO.setIsEmailSent(creditor.getIsEmailSent());
                creditorDTO.setIsSettle(creditor.getIsSettle());
                creditorDTO.setCustomerId(creditor.getCustomerUser().getCustomerId());

                creditorDTOList.add(creditorDTO);
            }
        }
        return creditorDTOList;
    }

    @Override
    public List<CreditorDTO> readAllCreditDetailsByIsNotPaid() {
        List<Creditor> creditorList = creditorRepository.findAll();
        List<CreditorDTO> creditorDTOList = new ArrayList<>();

        for (Creditor creditor : creditorList) {
            if (creditor.getIsSettle() == 0) {
                CreditorDTO creditorDTO = new CreditorDTO();
                creditorDTO.setId(creditor.getId());
                creditorDTO.setNicFrontImage(creditor.getNicFrontImage());
                creditorDTO.setNicBackImage(creditor.getNicBackImage());
                creditorDTO.setTotalCreditAmount(creditor.getTotalCreditAmount().toString());

                String creditDate = creditor.getCredit_date().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                String lastDateToSettle = creditor.getLastDateToSettle().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

                creditorDTO.setCreditDate(creditDate);
                creditorDTO.setLastDateToSettle(lastDateToSettle);
                creditorDTO.setIsEmailSent(creditor.getIsEmailSent());
                creditorDTO.setIsSettle(creditor.getIsSettle());
                creditorDTO.setCustomerId(creditor.getCustomerUser().getCustomerId());

                creditorDTOList.add(creditorDTO);
            }
        }
        return creditorDTOList;
    }

    @Override
    public List<CreditorDTO> readAllCreditDetailsByFilter(int filterStatus) {
        List<CreditorDTO> creditorDTOList = new ArrayList<>();
        if (filterStatus == 0) {
            creditorDTOList = readAllCreditDetailsByEmailSent();
        } else if (filterStatus == 1) {
            creditorDTOList = readAllCreditDetailsByEmailNotSent();
        } else if (filterStatus == 2) {
            creditorDTOList = readAllCreditDetailsByIsPaid();
        } else if (filterStatus == 3) {
            creditorDTOList = readAllCreditDetailsByIsNotPaid();
        } else if (filterStatus == 4) {
            creditorDTOList = readAllCreditorDetails();
        }
        return creditorDTOList;
    }
}
