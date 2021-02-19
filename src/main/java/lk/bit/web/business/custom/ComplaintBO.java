package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.ComplainDTO;
import lk.bit.web.entity.Complaint;

public interface ComplaintBO extends SuperBO {

    public void save(ComplainDTO complainDTO);
    public boolean IsComplaintExist(String complaintId);
}
