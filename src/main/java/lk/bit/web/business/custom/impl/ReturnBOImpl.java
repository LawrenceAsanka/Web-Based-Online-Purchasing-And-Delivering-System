package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ReturnBO;
import lk.bit.web.dto.ReturnDTO;
import lk.bit.web.dto.ReturnDetailDTO;
import lk.bit.web.entity.OrderInvoice;
import lk.bit.web.entity.Return;
import lk.bit.web.entity.ReturnDetail;
import lk.bit.web.repository.OrderInvoiceRepository;
import lk.bit.web.repository.ReturnDetailRepository;
import lk.bit.web.repository.ReturnRepository;
import lk.bit.web.util.tm.ReturnInvoiceTM;
import lk.bit.web.util.tm.ReturnTM;
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
            returnTM.setCustomer(orderInvoice.getCustomerUser().getCustomerFirstName()+ " " + orderInvoice.getCustomerUser().getCustomerLastName());

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
            returnTM.setCustomer(orderInvoice.getCustomerUser().getCustomerFirstName()+ " " + orderInvoice.getCustomerUser().getCustomerLastName());

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
            returnTM.setCustomer(orderInvoice.getCustomerUser().getCustomerFirstName()+ " " + orderInvoice.getCustomerUser().getCustomerLastName());

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

    private String getNewReturnId(){
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
                newReturnId = "RT"+newId;
            }
        }
        return newReturnId;
    }
}
