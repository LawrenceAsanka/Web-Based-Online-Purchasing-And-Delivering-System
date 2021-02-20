package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ComplainBO;
import lk.bit.web.dto.ComplainDTO;
import lk.bit.web.entity.Complain;
import lk.bit.web.entity.CustomerUser;
import lk.bit.web.repository.ComplainRepository;
import lk.bit.web.repository.CustomerUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ComplainBOImpl implements ComplainBO {
    @Autowired
    private ComplainRepository complainRepository;
    @Autowired
    private CustomerUserRepository customerUserRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public void save(ComplainDTO complainDTO) {
        Optional<CustomerUser> optionalCustomerUser = customerUserRepository.findById(complainDTO.getCreatedBy());

        if (optionalCustomerUser.isPresent()) {

            Complain complain = mapper.map(complainDTO, Complain.class);
            complain.setId(getNewComplaintId());
            complain.setCustomer(optionalCustomerUser.get());

            complainRepository.save(complain);
        }
    }

    @Override
    public boolean IsComplaintExist(String complaintId) {
        return true;
    }

    @Override
    public List<ComplainDTO> getComplaintDetailsByAdmin() {
        List<Complain> complainList = complainRepository.getComplaintDetailsByAdminStatus();
        List<ComplainDTO> complainDTOList = new ArrayList<>();

        for (Complain complain : complainList) {

            ComplainDTO complainDTO = mapper.map(complain, ComplainDTO.class);
            complainDTO.setId(complain.getId());

            String createdDateTime = complain.getCreatedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            complainDTO.setCreatedDateTime(createdDateTime);

            complainDTO.setIsDeletedByCustomer(complain.getIsDeleteByCustomer());
            complainDTO.setIsDeletedByAdmin(complain.getIsDeletedByAdmin());
            complainDTO.setMsgDescription(complain.getMsgDescription());
            complainDTO.setMsgSubject(complain.getMsgSubject());
            complainDTO.setResponseStatus(complain.getResponseStatus());
            complainDTO.setCreatedBy(complain.getCustomer().getCustomerId()+ "-" +complain.getCustomer().getCustomerFirstName());

            complainDTOList.add(complainDTO);
        }

        return complainDTOList;
    }

    @Override
    public List<ComplainDTO> getComplaintDetailsByStatus(int status) {
        List<Complain> complainList = new ArrayList<>();

        if (status == 2) {
            complainList = complainRepository.getComplaintDetailsByAdminStatus();
        } else {
            complainList = complainRepository.getComplaintDetailsByStatus(status);
        }

        List<ComplainDTO> complainDTOList = new ArrayList<>();

        for (Complain complain : complainList) {

            ComplainDTO complainDTO = mapper.map(complain, ComplainDTO.class);
            complainDTO.setId(complain.getId());

            String createdDateTime = complain.getCreatedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            complainDTO.setCreatedDateTime(createdDateTime);

            complainDTO.setIsDeletedByCustomer(complain.getIsDeleteByCustomer());
            complainDTO.setIsDeletedByAdmin(complain.getIsDeletedByAdmin());
            complainDTO.setMsgDescription(complain.getMsgDescription());
            complainDTO.setMsgSubject(complain.getMsgSubject());
            complainDTO.setResponseStatus(complain.getResponseStatus());
            complainDTO.setCreatedBy(complain.getCustomer().getCustomerId()+ "-" +complain.getCustomer().getCustomerFirstName());

            complainDTOList.add(complainDTO);
        }

        return complainDTOList;
    }

    private String getNewComplaintId(){
        String lastComplaintId = complainRepository.getComplaintId();
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

    private ComplainDTO getComplainDTO(Complain complain){
        return mapper.map(complain, ComplainDTO.class);
    }
}
