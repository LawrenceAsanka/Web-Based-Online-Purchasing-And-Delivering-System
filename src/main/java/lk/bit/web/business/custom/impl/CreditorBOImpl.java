package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.CreditorBO;
import lk.bit.web.dto.CreditDetailDTO;
import lk.bit.web.entity.*;
import lk.bit.web.repository.*;
import lk.bit.web.util.tm.CreditCollectionTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class CreditorBOImpl implements CreditorBO {
    @Autowired
    private CreditorRepository creditorRepository;
    @Autowired
    private CustomerUserRepository customerUserRepository;
    @Autowired
    private AssignCreditRepository assignCreditRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private OrderInvoiceRepository orderInvoiceRepository;

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

    @Override
    public List<CreditCollectionTM> readAllCreditDetailsByAssignee(String assignee) {
        List<CreditCollectionTM> creditCollectionTMList = new ArrayList<>();
        SystemUser systemUser = systemUserRepository.findSystemUser(assignee);

        if (systemUser != null) {
            List<CustomEntity13> creditList = creditorRepository.readCreditDetailsByAssignee(systemUser.getId());

            for (CustomEntity13 credit : creditList) {

                Optional<OrderInvoice> optionalOrderInvoice = orderInvoiceRepository.findById(credit.getOrderId());
                Optional<CreditDetail> optionalCreditDetail = creditorRepository.findById(credit.getCreditId());

                if (optionalOrderInvoice.isPresent() && optionalCreditDetail.isPresent()) {
                    OrderInvoice order = optionalOrderInvoice.get();
                    CreditDetail creditDetail = optionalCreditDetail.get();

                    String creditDate = creditDetail.getCreditDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                    String address = order.getShop().getAddress1() + " " + order.getShop().getAddress2() + " " + order.getShop().getCity()+
                            " "+ order.getShop().getDistrict();

                    CreditCollectionTM creditCollectionTM = new CreditCollectionTM(
                            credit.getCreditId(), order.getCustomerUser().getCustomerId()+"-"+order.getCustomerUser().getCustomerFirstName(),
                            order.getShop().getShopName(), address,creditDate,creditDetail.getTotalCreditAmount().toString()
                            );

                    creditCollectionTMList.add(creditCollectionTM);
                }
            }
        }
        return creditCollectionTMList;
    }

    @Override
    public void updateCreditAmountAndDate(String creditId, String extendDate, String newAmount) {
        Optional<CreditDetail> optionalCreditDetail = creditorRepository.findById(creditId);

        if (optionalCreditDetail.isPresent()) {
            // update date and amount
            LocalDateTime lastDateToSettle = optionalCreditDetail.get().getLastDateToSettle();

            optionalCreditDetail.get().setTotalCreditAmount(new BigDecimal(newAmount));
            optionalCreditDetail.get().setLastDateToSettle(lastDateToSettle.plusDays(Long.parseLong(extendDate)));
            optionalCreditDetail.get().setIsAssigned(0);

            creditorRepository.save(optionalCreditDetail.get());

            int assignCreditId = assignCreditRepository.readAssignCreditByCreditId(creditId);
            assignCreditRepository.deleteById(assignCreditId);
        }
    }

    @Override
    public void updateIsSettleStatus(String creditId) {
        Optional<CreditDetail> optionalCreditDetail = creditorRepository.findById(creditId);

        if (optionalCreditDetail.isPresent()) {
            optionalCreditDetail.get().setIsSettle(1);

            creditorRepository.save(optionalCreditDetail.get());
        }
    }
}
