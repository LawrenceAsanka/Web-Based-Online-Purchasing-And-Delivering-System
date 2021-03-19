package lk.bit.web.business.custom;

import lk.bit.web.dto.ReturnDTO;
import lk.bit.web.entity.SuperEntity;
import lk.bit.web.util.tm.AssignReturnTM;
import lk.bit.web.util.tm.DeliveryReturnTM;
import lk.bit.web.util.tm.ReturnInvoiceTM;
import lk.bit.web.util.tm.ReturnTM;

import java.util.List;

public interface ReturnBO extends SuperEntity {

    void saveReturnDetail(ReturnDTO returnDTO);

    List<ReturnTM> readAllByStatus();

    List<ReturnTM> readAllByStatusCancel();

    List<ReturnTM> readAllByStatusConfirm();

    List<ReturnInvoiceTM> readAllReturnDetailsByReturnId(String returnId);

    boolean IsReturnExist(String returnId);

    void updateStatusToCancel(String returnId);

    void updateStatusToConfirm(String returnId);

    void saveAssignReturnAndUpdateStatus(String returnIdArray, String assignTo);

    List<AssignReturnTM> readAssignReturnDetail();

    List<DeliveryReturnTM> readAllAssignReturnDetailByAssignee(String assignee);
}
