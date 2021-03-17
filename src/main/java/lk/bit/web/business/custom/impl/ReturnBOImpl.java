package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.OrderInvoiceBO;
import lk.bit.web.business.custom.ReturnBO;
import lk.bit.web.dto.ReturnDTO;
import lk.bit.web.dto.ReturnDetailDTO;
import lk.bit.web.entity.Return;
import lk.bit.web.entity.ReturnDetail;
import lk.bit.web.repository.OrderInvoiceRepository;
import lk.bit.web.repository.ReturnDetailRepository;
import lk.bit.web.repository.ReturnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
