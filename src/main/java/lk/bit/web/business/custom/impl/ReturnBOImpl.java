package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ReturnBO;
import lk.bit.web.dto.ReturnDTO;
import lk.bit.web.dto.ReturnDetailDTO;
import lk.bit.web.entity.*;
import lk.bit.web.repository.*;
import lk.bit.web.util.tm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    public void saveAssignReturnAndUpdateStatus(String returnIdArray, String assignTo) {
        String[] splitArray = returnIdArray.split(",");

        for (String returnId : splitArray) {

            Optional<Return> optionalReturn = returnRepository.findById(returnId);
            Optional<SystemUser> optionalSystemUser = systemUserRepository.findById(assignTo);

            if (optionalReturn.isPresent() && optionalSystemUser.isPresent()) {

                optionalReturn.get().setStatus(3);

                assignReturnRepository.save(new AssignReturn(optionalReturn.get(), optionalSystemUser.get()));
                returnRepository.save(optionalReturn.get());
            }
        }
    }

    @Override
    public List<AssignReturnTM> readAssignReturnDetail() {
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
            SystemUser systemUser = systemUserRepository.findById(assignReturn.getAssigneeId().getId()).get();
            assignReturnTM.setAssignTo(systemUser.getFirstName() + " " + systemUser.getLastName());

            String assignedDateTime = assignReturn.getAssignedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            assignReturnTM.setAssignedDateTime(assignedDateTime);

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
