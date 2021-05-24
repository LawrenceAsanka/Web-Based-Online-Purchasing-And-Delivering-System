package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ReturnBO;
import lk.bit.web.dto.ReturnDTO;
import lk.bit.web.dto.ReturnDetailDTO;
import lk.bit.web.entity.*;
import lk.bit.web.repository.*;
import lk.bit.web.util.message.TextMsgSender;
import lk.bit.web.util.tm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class ReturnBOImpl implements ReturnBO {

    @Autowired
    private ReturnRepository returnRepository;
    @Autowired
    private ReturnDetailRepository returnDetailRepository;
    @Autowired
    private OrderInvoiceRepository orderInvoiceRepository;
    @Autowired
    private AssignReturnRepository assignReturnRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private CompleteReturnRepository completeReturnRepository;
    @Autowired
    private CustomerUserRepository customerUserRepository;
    @Autowired
    private ShopRepository shopRepository;

    @Override
    public void saveReturnDetail(ReturnDTO returnDTO) {

        // save return
        String returnId = getNewReturnId();
        returnRepository.save(new Return(returnId, orderInvoiceRepository.findById(returnDTO.getOrderId()).get()));

        // save return detail
        List<ReturnDetailDTO> returnDetailDTOList = returnDTO.getReturnDetailDTOList();
        for (ReturnDetailDTO returnDetailDTO : returnDetailDTOList) {

            returnDetailRepository.save(new ReturnDetail(returnId, returnDetailDTO.getProductId(), returnDetailDTO.getReturnQty(),
                    returnDetailDTO.getReasonToReturn()));
        }

    }

    @Override
    public List<ReturnTM> readAllByStatus() {
        List<Return> returnList = returnRepository.readAllByStatus();
        List<ReturnTM> returnTMList = new ArrayList<>();

        for (Return returnRepo : returnList) {
            ReturnTM returnTM = new ReturnTM();

            returnTM.setReturnId(returnRepo.getId());

            String createdDateTime = returnRepo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            returnTM.setCreatedDateTime(createdDateTime);

            returnTM.setOrderId(returnRepo.getOrderId().getOrderId());

            OrderInvoice orderInvoice = orderInvoiceRepository.findById(returnRepo.getOrderId().getOrderId()).get();
            returnTM.setCustomerId(orderInvoice.getCustomerUser().getCustomerId());
            returnTM.setCustomer(orderInvoice.getCustomerUser().getCustomerFirstName() + " " + orderInvoice.getCustomerUser().getCustomerLastName());

            returnTMList.add(returnTM);
        }

        return returnTMList;
    }

    @Override
    public List<ReturnTM> readAllByStatusCancel() {
        List<Return> returnList = returnRepository.readAllByStatusCancel();
        List<ReturnTM> returnTMList = new ArrayList<>();

        for (Return returnRepo : returnList) {
            ReturnTM returnTM = new ReturnTM();

            returnTM.setReturnId(returnRepo.getId());

            String createdDateTime = returnRepo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            returnTM.setCreatedDateTime(createdDateTime);

            returnTM.setOrderId(returnRepo.getOrderId().getOrderId());

            OrderInvoice orderInvoice = orderInvoiceRepository.findById(returnRepo.getOrderId().getOrderId()).get();
            returnTM.setCustomerId(orderInvoice.getCustomerUser().getCustomerId());
            returnTM.setCustomer(orderInvoice.getCustomerUser().getCustomerFirstName() + " " + orderInvoice.getCustomerUser().getCustomerLastName());

            returnTMList.add(returnTM);
        }

        return returnTMList;
    }

    @Override
    public List<ReturnTM> readAllByStatusConfirm() {
        List<Return> returnList = returnRepository.readAllByStatusConfirm();
        List<ReturnTM> returnTMList = new ArrayList<>();

        for (Return returnRepo : returnList) {
            ReturnTM returnTM = new ReturnTM();

            returnTM.setReturnId(returnRepo.getId());

            String createdDateTime = returnRepo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            returnTM.setCreatedDateTime(createdDateTime);

            returnTM.setOrderId(returnRepo.getOrderId().getOrderId());

            OrderInvoice orderInvoice = orderInvoiceRepository.findById(returnRepo.getOrderId().getOrderId()).get();
            returnTM.setCustomerId(orderInvoice.getCustomerUser().getCustomerId());
            returnTM.setCustomer(orderInvoice.getCustomerUser().getCustomerFirstName() + " " + orderInvoice.getCustomerUser().getCustomerLastName());

            returnTMList.add(returnTM);
        }

        return returnTMList;
    }

    @Override
    public List<ReturnInvoiceTM> readAllReturnDetailsByReturnId(String returnId) {
        List<ReturnDetail> returnDetailList = returnDetailRepository.readReturnDetailByReturnId(returnId);
        List<ReturnInvoiceTM> returnDetailTM = new ArrayList<>();

        for (ReturnDetail returnDetail : returnDetailList) {
            ReturnInvoiceTM returnInvoiceTM = new ReturnInvoiceTM();

            returnInvoiceTM.setProductId(returnDetail.getProduct().getProductId());
            returnInvoiceTM.setProductName(returnDetail.getProduct().getProductName());
            returnInvoiceTM.setProductImagePath(returnDetail.getProduct().getImageOne());
            returnInvoiceTM.setReasonToReturn(returnDetail.getReasonToReturn());
            returnInvoiceTM.setReturnQty(returnDetail.getReturnQty());

            returnDetailTM.add(returnInvoiceTM);
        }

        return returnDetailTM;
    }

    @Override
    public List<CompleteReturnTM> readAllCompleteReturnDetailsByAssignee(String assignee) {
        SystemUser systemUser = systemUserRepository.findSystemUser(assignee);
        List<CompleteReturnTM> completeReturnTMList = new ArrayList<>();

        if (systemUser != null) {
            List<CustomEntity12> completeReturnDetailsByAssignee = completeReturnRepository.readAllCompleteReturnDetailsByAssignee(systemUser.getId());

            for (CustomEntity12 detail : completeReturnDetailsByAssignee) {
                CompleteReturnTM completeReturnTM = new CompleteReturnTM();

                completeReturnTM.setReturnId(detail.getReturnId());

                String assignedDateTime = detail.getAssignedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                String completedDateTime = detail.getCompletedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

                completeReturnTM.setAssignedDateTime(assignedDateTime);
                completeReturnTM.setCompletedDateTime(completedDateTime);

                OrderInvoice orderInvoice = orderInvoiceRepository.findById(detail.getOrderId()).get();
                completeReturnTM.setShop(orderInvoice.getShop().getShopName());
                completeReturnTM.setAddress(
                        orderInvoice.getShop().getAddress1()+" "+
                                orderInvoice.getShop().getAddress2()+" "+
                                orderInvoice.getShop().getCity()+" "+
                                orderInvoice.getShop().getDistrict()
                );

                completeReturnTMList.add(completeReturnTM);
            }

        }
        return completeReturnTMList;
    }

    @Override
    public boolean IsReturnExist(String returnId) {
        return returnRepository.existsById(returnId);
    }

    @Override
    public void updateStatusToCancel(String returnId) {
        Optional<Return> returnOptional = returnRepository.findById(returnId);

        if (returnOptional.isPresent()) {
            returnOptional.get().setStatus(1);

            returnRepository.save(returnOptional.get());
        }
    }

    @Override
    public void updateStatusToConfirm(String returnId) {
        Optional<Return> returnOptional = returnRepository.findById(returnId);

        if (returnOptional.isPresent()) {
            returnOptional.get().setStatus(2);

            returnRepository.save(returnOptional.get());
        }
    }

    @Override
    public void saveAssignReturnAndUpdateStatus(String returnIdArray, String assignTo) throws IOException {
        String[] splitArray = returnIdArray.split(",");
        Optional<SystemUser> optionalSystemUser = systemUserRepository.findById(assignTo);

        for (String returnId : splitArray) {

            Optional<Return> optionalReturn = returnRepository.findById(returnId);

            if (optionalReturn.isPresent() && optionalSystemUser.isPresent()) {

                optionalReturn.get().setStatus(3);

                assignReturnRepository.save(new AssignReturn(optionalReturn.get(), optionalSystemUser.get()));
                returnRepository.save(optionalReturn.get());
            }
        }
        String finalMessage = "You+have+assigned+some+returns.+Please+check.Thank+You.+FROM+VG+DISTRIBUTORS";
        /*System.out.println(finalMessage);
        System.out.println(orderIdList);*/
        TextMsgSender textMsgSender = new TextMsgSender();
        textMsgSender.sendTextMsg(optionalSystemUser.get().getContact(), finalMessage );
    }

    @Override
    public List<AssignReturnTM> readAssignReturnDetailByStatusProcessing() {
        List<Return> returnList = returnRepository.readAllByStatusProcessing();
        List<AssignReturnTM> assignReturnDetails = new ArrayList<>();

        for (Return returnDetail : returnList) {

            AssignReturnTM assignReturnTM = new AssignReturnTM();

            assignReturnTM.setReturnId(returnDetail.getId());
            assignReturnTM.setOrderId(returnDetail.getOrderId().getOrderId());

            String createdDateTime = returnDetail.getCreatedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            assignReturnTM.setCreatedDateTime(createdDateTime);

            OrderInvoice orderInvoice = orderInvoiceRepository.findById(returnDetail.getOrderId().getOrderId()).get();
            assignReturnTM.setCustomerId(orderInvoice.getCustomerUser().getCustomerId());
            assignReturnTM.setCustomerName(orderInvoice.getCustomerUser().getCustomerFirstName() + " " + orderInvoice.getCustomerUser().getCustomerLastName());

            AssignReturn assignReturn = assignReturnRepository.readAssignReturnDetailByReturnId(returnDetail.getId());
            //complete assign eka read karnna
            SystemUser systemUser = systemUserRepository.findById(assignReturn.getAssigneeId().getId()).get();
            assignReturnTM.setAssignTo(systemUser.getFirstName() + " " + systemUser.getLastName());

            String assignedDateTime = assignReturn.getAssignedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            assignReturnTM.setAssignedDateTime(assignedDateTime);

            assignReturnDetails.add(assignReturnTM);

        }

        return assignReturnDetails;
    }

    @Override
    public List<AssignReturnTM> readAssignReturnDetailByStatusComplete() {
        List<Return> returnList = returnRepository.readAllByStatusComplete();
        List<AssignReturnTM> assignReturnDetails = new ArrayList<>();

        for (Return returnDetail : returnList) {

            AssignReturnTM assignReturnTM = new AssignReturnTM();

            assignReturnTM.setReturnId(returnDetail.getId());
            assignReturnTM.setOrderId(returnDetail.getOrderId().getOrderId());

            String createdDateTime = returnDetail.getCreatedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            assignReturnTM.setCreatedDateTime(createdDateTime);

            OrderInvoice orderInvoice = orderInvoiceRepository.findById(returnDetail.getOrderId().getOrderId()).get();
            assignReturnTM.setCustomerId(orderInvoice.getCustomerUser().getCustomerId());
            assignReturnTM.setCustomerName(orderInvoice.getCustomerUser().getCustomerFirstName() + " " + orderInvoice.getCustomerUser().getCustomerLastName());

            AssignReturn assignReturn = assignReturnRepository.readAssignReturnDetailByReturnId(returnDetail.getId());
            CompleteReturn completeReturn = completeReturnRepository.readCompleteReturnByAssignReturnId(assignReturn.getId());

            SystemUser systemUser = systemUserRepository.findById(assignReturn.getAssigneeId().getId()).get();
            assignReturnTM.setAssignTo(systemUser.getFirstName() + " " + systemUser.getLastName());

            String returnDateTime = completeReturn.getDeliveredDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            assignReturnTM.setAssignedDateTime(returnDateTime);

            assignReturnDetails.add(assignReturnTM);

        }

        return assignReturnDetails;
    }

    @Override
    public List<DeliveryReturnTM> readAllAssignReturnDetailByAssignee(String assignee) {
        SystemUser systemUser = systemUserRepository.findSystemUser(assignee);
        List<DeliveryReturnTM> assignDetailTM = new ArrayList<>();

        if (systemUser != null) {
            List<CustomEntity11> assignDetails = assignReturnRepository.readAssignReturnDetailByAssignee(systemUser.getId());

            for (CustomEntity11 assignDetail : assignDetails) {
                DeliveryReturnTM deliveryReturnTM = new DeliveryReturnTM();
//                System.out.println(deliveryReturnTM.getAssignedDateTime());

                deliveryReturnTM.setReturnId(assignDetail.getReturnId());
                deliveryReturnTM.setOrderId(assignDetail.getOrderId());

                String assignedDateTime = assignDetail.getAssignedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                deliveryReturnTM.setAssignedDateTime(assignedDateTime);

                Optional<OrderInvoice> orderInvoice = orderInvoiceRepository.findById(assignDetail.getOrderId());

                if (orderInvoice.isPresent()) {

                    deliveryReturnTM.setCustomerName(orderInvoice.get().getCustomerUser().getCustomerFirstName()+" "+
                            orderInvoice.get().getCustomerUser().getCustomerLastName());
                    deliveryReturnTM.setReturnAddress(
                                    orderInvoice.get().getShop().getAddress1()+" "+
                                    orderInvoice.get().getShop().getAddress2()+" "+
                                    orderInvoice.get().getShop().getCity()+" "+
                                    orderInvoice.get().getShop().getDistrict()
                    );
                }

                assignDetailTM.add(deliveryReturnTM);
            }

        }
        return assignDetailTM;
    }

    @Override
    public void saveCompleteReturnDetails(String returnId) {
        Optional<Return> optionalReturn = returnRepository.findById(returnId);

        if (optionalReturn.isPresent()) {
            AssignReturn assignReturn = assignReturnRepository.readAssignReturnDetailByReturnId(returnId);

            // save complete return
            completeReturnRepository.save(new CompleteReturn(getNewCompleteReturnId(), assignReturn));

            // update status
            optionalReturn.get().setStatus(4);
            returnRepository.save(optionalReturn.get());
        }

    }

    @Override
    public List<ReturnDTO> readAllAssignReturnsDetailsByCustomerEmail(String email) {
        CustomerUser customerUser = customerUserRepository.getCustomerByCustomerEmail(email);
        List<ReturnDTO> returnDTOList = new ArrayList<>();

        if (customerUser != null) {
            List<Return> returnList = returnRepository.findAll();

            for (Return detail : returnList) {
                if (detail.getOrderId().getCustomerUser().equals(customerUser)) {
                    ReturnDTO returnDTO = new ReturnDTO();

                    returnDTO.setReturnId(detail.getId());

                    String createdDateTime = detail.getCreatedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                    returnDTO.setCreatedDateTime(createdDateTime);

                    returnDTO.setOrderId(detail.getOrderId().getOrderId());
                    returnDTO.setStatus(detail.getStatus());

                    returnDTOList.add(returnDTO);
                } else {
                    returnDTOList.add(null);
                }
            }

        } else {
            returnDTOList.add(null);
        }
        return returnDTOList;
    }

    @Override
    public List<ReturnDTO> readAllAssignReturnsDetailsByCustomerId(String id) {
        Optional<CustomerUser> optionalCustomerUser = customerUserRepository.findById(id);
        List<ReturnDTO> returnDTOList = new ArrayList<>();

        if (optionalCustomerUser.isPresent()) {
            List<Return> returnList = returnRepository.findAll();

            for (Return detail : returnList) {
                if (detail.getOrderId().getCustomerUser().equals(optionalCustomerUser.get())) {
                    ReturnDTO returnDTO = new ReturnDTO();

                    returnDTO.setReturnId(detail.getId());

                    String createdDateTime = detail.getCreatedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                    returnDTO.setCreatedDateTime(createdDateTime);

                    returnDTO.setOrderId(detail.getOrderId().getShop().getShopName());
                    returnDTO.setStatus(detail.getStatus());

                    returnDTOList.add(returnDTO);
                }else {
                    returnDTOList.add(null);
                }
            }
        } else {
            returnDTOList.add(null);
        }
        return returnDTOList;
    }

    @Override
    public int readNewReturnCount() {
        return returnRepository.readNewReturnCount();
    }

    @Override
    public int readAllTodayReturnCount() {
        return assignReturnRepository.readAllTodayReturnCount(LocalDate.now().toString());
    }

    @Override
    public int readATotalCompleteReturnsCount() {
        return completeReturnRepository.readAllCompleteReturnCount(LocalDate.now().toString());
    }

    @Override
    public List<ReturnTM> readAllReturnDetailsByDateRange(String startDate, String endDate) {
        List<Return> returnList = returnRepository.readAllByStatusCancel();
        List<ReturnTM> returnTMList = new ArrayList<>();

        for (Return returnRepo : returnList) {
            ReturnTM returnTM = new ReturnTM();

            LocalDate localDate = returnRepo.getCreatedDateTime().toLocalDate();
            if (localDate.isEqual(LocalDate.parse(startDate)) || localDate.isEqual(LocalDate.parse(endDate)) ||
                    (localDate.isAfter(LocalDate.parse(startDate)) && localDate.isBefore(LocalDate.parse(endDate)))) {

                returnTM.setReturnId(returnRepo.getId());

                String createdDateTime = returnRepo.getCreatedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                returnTM.setCreatedDateTime(createdDateTime);

                returnTM.setOrderId(returnRepo.getOrderId().getOrderId());

                OrderInvoice orderInvoice = orderInvoiceRepository.findById(returnRepo.getOrderId().getOrderId()).get();
                returnTM.setCustomerId(orderInvoice.getCustomerUser().getCustomerId());
                returnTM.setCustomer(orderInvoice.getCustomerUser().getCustomerFirstName() + " " + orderInvoice.getCustomerUser().getCustomerLastName());

                returnTMList.add(returnTM);
            }
        }

        return returnTMList;
    }

    @Override
    public List<AssignReturnTM> readReturnDetailsByStatusCompleteAndCreateDateRange(String startDate, String endDate) {
        List<Return> returnList = returnRepository.readAllByStatusComplete();
        List<AssignReturnTM> assignReturnDetails = new ArrayList<>();

        for (Return returnDetail : returnList) {
            AssignReturnTM assignReturnTM = new AssignReturnTM();

            LocalDate localDate = returnDetail.getCreatedDateTime().toLocalDate();
            if (localDate.isEqual(LocalDate.parse(startDate)) || localDate.isEqual(LocalDate.parse(endDate)) ||
                    (localDate.isAfter(LocalDate.parse(startDate)) && localDate.isBefore(LocalDate.parse(endDate)))) {

                assignReturnTM.setReturnId(returnDetail.getId());
                assignReturnTM.setOrderId(returnDetail.getOrderId().getOrderId());

                String createdDateTime = returnDetail.getCreatedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                assignReturnTM.setCreatedDateTime(createdDateTime);

                OrderInvoice orderInvoice = orderInvoiceRepository.findById(returnDetail.getOrderId().getOrderId()).get();
                assignReturnTM.setCustomerId(orderInvoice.getCustomerUser().getCustomerId());
                assignReturnTM.setCustomerName(orderInvoice.getCustomerUser().getCustomerFirstName() + " " + orderInvoice.getCustomerUser().getCustomerLastName());

                AssignReturn assignReturn = assignReturnRepository.readAssignReturnDetailByReturnId(returnDetail.getId());
                CompleteReturn completeReturn = completeReturnRepository.readCompleteReturnByAssignReturnId(assignReturn.getId());

                SystemUser systemUser = systemUserRepository.findById(assignReturn.getAssigneeId().getId()).get();
                assignReturnTM.setAssignTo(systemUser.getFirstName() + " " + systemUser.getLastName());

                String returnDateTime = completeReturn.getDeliveredDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                assignReturnTM.setAssignedDateTime(returnDateTime);

                assignReturnDetails.add(assignReturnTM);
            }

        }

        return assignReturnDetails;
    }

    @Override
    public List<AssignReturnTM> readReturnDetailsByStatusCompleteAndReturnDateRange(String startDate, String endDate) {
        List<Return> returnList = returnRepository.readAllByStatusComplete();
        List<AssignReturnTM> assignReturnDetails = new ArrayList<>();

        for (Return returnDetail : returnList) {
            AssignReturnTM assignReturnTM = new AssignReturnTM();

                assignReturnTM.setReturnId(returnDetail.getId());
                assignReturnTM.setOrderId(returnDetail.getOrderId().getOrderId());

                String createdDateTime = returnDetail.getCreatedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                assignReturnTM.setCreatedDateTime(createdDateTime);

                OrderInvoice orderInvoice = orderInvoiceRepository.findById(returnDetail.getOrderId().getOrderId()).get();
                assignReturnTM.setCustomerId(orderInvoice.getCustomerUser().getCustomerId());
                assignReturnTM.setCustomerName(orderInvoice.getCustomerUser().getCustomerFirstName() + " " + orderInvoice.getCustomerUser().getCustomerLastName());

                AssignReturn assignReturn = assignReturnRepository.readAssignReturnDetailByReturnId(returnDetail.getId());
                CompleteReturn completeReturn = completeReturnRepository.readCompleteReturnByAssignReturnId(assignReturn.getId());

                SystemUser systemUser = systemUserRepository.findById(assignReturn.getAssigneeId().getId()).get();
                assignReturnTM.setAssignTo(systemUser.getFirstName() + " " + systemUser.getLastName());

                String returnDateTime = completeReturn.getDeliveredDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                assignReturnTM.setAssignedDateTime(returnDateTime);

            LocalDate localDate = completeReturn.getDeliveredDateTime().toLocalDate();
            if (localDate.isEqual(LocalDate.parse(startDate)) || localDate.isEqual(LocalDate.parse(endDate)) ||
                    (localDate.isAfter(LocalDate.parse(startDate)) && localDate.isBefore(LocalDate.parse(endDate)))) {

                assignReturnDetails.add(assignReturnTM);
            }

        }

        return assignReturnDetails;
    }

    private String getNewReturnId() {
        String returnId = returnRepository.getReturnId();
        String newReturnId = "";
        if (returnId == null) {
            newReturnId = "RT01";
        } else {
            String id = returnId.replaceAll("RT", "");
            int newId = Integer.parseInt(id) + 1;
            if (newId < 10) {
                newReturnId = "RT0" + newId;
            } else {
                newReturnId = "RT" + newId;
            }
        }
        return newReturnId;
    }

    private String getNewCompleteReturnId() {
        String completeReturnId = completeReturnRepository.getCompleteReturnId();
        String newCompleteReturnId = "";
        if (completeReturnId == null) {
            newCompleteReturnId = "CR01";
        } else {
            String id = completeReturnId.replaceAll("CR", "");
            int newId = Integer.parseInt(id) + 1;
            if (newId < 10) {
                newCompleteReturnId = "CR0" + newId;
            } else {
                newCompleteReturnId = "CR" + newId;
            }
        }
        return newCompleteReturnId;
    }
}
