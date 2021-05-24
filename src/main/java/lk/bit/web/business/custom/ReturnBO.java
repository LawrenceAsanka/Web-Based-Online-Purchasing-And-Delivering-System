package lk.bit.web.business.custom;

import lk.bit.web.dto.ReturnDTO;
import lk.bit.web.entity.SuperEntity;
import lk.bit.web.util.tm.*;

import java.io.IOException;
import java.util.List;

public interface ReturnBO extends SuperEntity {

    void saveReturnDetail(ReturnDTO returnDTO);

    List<ReturnTM> readAllByStatus();

    List<ReturnTM> readAllByStatusCancel();

    List<ReturnTM> readAllByStatusConfirm();

    List<ReturnInvoiceTM> readAllReturnDetailsByReturnId(String returnId);

    List<CompleteReturnTM> readAllCompleteReturnDetailsByAssignee(String assignee);

    boolean IsReturnExist(String returnId);

    void updateStatusToCancel(String returnId);

    void updateStatusToConfirm(String returnId);

    void saveAssignReturnAndUpdateStatus(String returnIdArray, String assignTo) throws IOException;

    List<AssignReturnTM> readAssignReturnDetailByStatusProcessing();

    List<AssignReturnTM> readAssignReturnDetailByStatusComplete();

    List<DeliveryReturnTM> readAllAssignReturnDetailByAssignee(String assignee);

    void saveCompleteReturnDetails(String returnId);

    List<ReturnDTO> readAllAssignReturnsDetailsByCustomerEmail(String email);

    List<ReturnDTO> readAllAssignReturnsDetailsByCustomerId(String id);

    int readNewReturnCount();

    int readAllTodayReturnCount();

    int readATotalCompleteReturnsCount();

    List<ReturnTM> readAllReturnDetailsByDateRange(String startDate, String endDate);

    List<AssignReturnTM> readReturnDetailsByStatusCompleteAndCreateDateRange(String startDate, String endDate);

    List<AssignReturnTM> readReturnDetailsByStatusCompleteAndReturnDateRange(String startDate, String endDate);
}
