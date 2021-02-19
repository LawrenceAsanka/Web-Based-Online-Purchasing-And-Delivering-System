package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ComplaintBO;
import lk.bit.web.dto.ComplainDTO;
import lk.bit.web.entity.Complaint;
import lk.bit.web.entity.CustomerUser;
import lk.bit.web.repository.ComplaintRepository;
import lk.bit.web.repository.CustomerUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ComplaintBOImpl implements ComplaintBO {
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private CustomerUserRepository customerUserRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public void save(ComplainDTO complainDTO) {
        Optional<CustomerUser> optionalCustomerUser = customerUserRepository.findById(complainDTO.getCreatedBy());

        if (optionalCustomerUser.isPresent()) {

            Complaint complaint = mapper.map(complainDTO, Complaint.class);
            complaint.setId(getNewComplaintId());
            complaint.setCustomer(optionalCustomerUser.get());

            complaintRepository.save(complaint);
        }
    }

    @Override
    public boolean IsComplaintExist(String complaintId) {
        return false;
    }

    private String getNewComplaintId(){
        String lastComplaintId = complaintRepository.getComplaintId();
        String newId = "";

        if (lastComplaintId == null) {
            newId = "COM01";
        } else {
            String idNumber = lastComplaintId.replaceAll("COM", "");
            int id = Integer.parseInt(idNumber) + 1;
            if (id < 10) {
                newId = "COM0" + id;
            } else {
                newId = "COM" + id;
            }
        }
        return newId;
    }
}
