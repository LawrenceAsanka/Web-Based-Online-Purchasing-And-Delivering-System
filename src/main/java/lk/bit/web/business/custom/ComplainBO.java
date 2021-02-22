package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.ComplainDTO;
import lk.bit.web.util.SolutionTM;

import java.util.List;

public interface ComplainBO extends SuperBO {

    public void save(ComplainDTO complainDTO);
    public boolean IsComplaintExist(String complaintId);
    public List<ComplainDTO> getComplaintDetailsByAdmin();
    public List<ComplainDTO> getComplaintDetailsByStatus(int status);
    public List<ComplainDTO> getComplaintDetailsByCustomerId(String customerId);
    public void updateIsDeleted(String complaintId);
    public int getTotalComplaintCount();
    public List<SolutionTM> getAllComplainAndComplainSolutionDetails();
    public List<SolutionTM> getAllComplainAndComplainSolutionDetailsById(String complainId);
    public void updateIsDeletedByCustomer(String complainId);
}
