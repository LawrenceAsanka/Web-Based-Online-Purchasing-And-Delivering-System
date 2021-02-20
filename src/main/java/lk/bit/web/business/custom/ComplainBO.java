package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.ComplainDTO;

import java.util.List;

public interface ComplainBO extends SuperBO {

    public void save(ComplainDTO complainDTO);
    public boolean IsComplaintExist(String complaintId);
    public List<ComplainDTO> getComplaintDetailsByAdmin();
    public List<ComplainDTO> getComplaintDetailsByStatus(int status);
}
