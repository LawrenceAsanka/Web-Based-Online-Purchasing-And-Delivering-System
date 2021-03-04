package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ComplainBO;
import lk.bit.web.dto.ComplainDTO;
import lk.bit.web.entity.Complain;
import lk.bit.web.entity.ComplainSolution;
import lk.bit.web.entity.CustomEntity6;
import lk.bit.web.entity.CustomerUser;
import lk.bit.web.repository.ComplainRepository;
import lk.bit.web.repository.ComplainSolutionRepository;
import lk.bit.web.repository.CustomerUserRepository;
import lk.bit.web.util.tm.SolutionTM;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class ComplainBOImpl implements ComplainBO {
    @Autowired
    private ComplainRepository complainRepository;
    @Autowired
    private CustomerUserRepository customerUserRepository;
    @Autowired
    private ComplainSolutionRepository complainSolutionRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public boolean IsComplaintExist(String complaintId) {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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

    @Override
    @Transactional(readOnly = true)
    public List<ComplainDTO> getComplaintDetailsByCustomerId(String customerId) {

        List<Complain> complainList = complainRepository.getComplainDetailsByCustomerId(customerId);

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
    @Transactional(readOnly = true)
    public void updateIsDeleted(String complaintId) {
        Optional<Complain> optionalComplain = complainRepository.findById(complaintId);

        if (optionalComplain.isPresent()) {

            optionalComplain.get().setIsDeletedByAdmin(1);

            complainRepository.save(optionalComplain.get());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalComplaintCount() {
       return complainRepository.getTotalComplainCount();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolutionTM> getAllComplainAndComplainSolutionDetails() {
        List<CustomEntity6> complainSolutionDetails = complainRepository.getAllComplainAndComplainSolutionDetails();
        List<SolutionTM> solutionDetails = new ArrayList<>();

        for (CustomEntity6 complainSolutionDetail : complainSolutionDetails) {
            SolutionTM solutionTM = new SolutionTM();

            solutionTM.setComplainId(complainSolutionDetail.getComplainId());
            solutionTM.setComplaintSubject(complainSolutionDetail.getComplainSubject());
            solutionTM.setComplaintDesc(complainSolutionDetail.getComplainDesc());

            String complainCreatedDate = complainSolutionDetail.getComplainCreatedDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            solutionTM.setComplainCreatedDate(complainCreatedDate);

            String solutionCreatedDate = complainSolutionDetail.getSolutionCreatedDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            solutionTM.setSolutionCreatedDate(solutionCreatedDate);

            solutionTM.setSolutionDesc(complainSolutionDetail.getSolutionDesc());
            solutionTM.setSolutionStatus(complainSolutionDetail.getSolutionStatus());

            solutionDetails.add(solutionTM);
        }

        return solutionDetails;
    }

    @Override
    public List<SolutionTM> getAllComplainAndComplainSolutionDetailsById(String complainId) {
        List<CustomEntity6> complainSolutionDetails = complainRepository.getAllComplainAndComplainSolutionDetailsById(complainId);
        List<SolutionTM> solutionDetails = new ArrayList<>();

        if (complainSolutionDetails != null) {

            //Update mark as read
            ComplainSolution complainSolution = complainSolutionRepository.getComplainSolutionByComplainId(complainId);
            complainSolution.setStatus(1);

            complainSolutionRepository.save(complainSolution);

            for (CustomEntity6 complainSolutionDetail : complainSolutionDetails) {
                System.out.println(complainSolutionDetail.getComplainId());
                SolutionTM solutionTM = new SolutionTM();

                solutionTM.setComplainId(complainSolutionDetail.getComplainId());
                solutionTM.setComplaintSubject(complainSolutionDetail.getComplainSubject());
                solutionTM.setComplaintDesc(complainSolutionDetail.getComplainDesc());

                String complainCreatedDate = complainSolutionDetail.getComplainCreatedDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                solutionTM.setComplainCreatedDate(complainCreatedDate);

                String solutionCreatedDate = complainSolutionDetail.getSolutionCreatedDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
                solutionTM.setSolutionCreatedDate(solutionCreatedDate);

                solutionTM.setSolutionDesc(complainSolutionDetail.getSolutionDesc());
                solutionTM.setSolutionStatus(complainSolutionDetail.getSolutionStatus());

                solutionDetails.add(solutionTM);
            }
        }


        return solutionDetails;
    }

    @Override
    public void updateIsDeletedByCustomer(String complainId) {
        Optional<Complain> optionalComplain = complainRepository.findById(complainId);

        if (optionalComplain.isPresent()) {

            optionalComplain.get().setIsDeleteByCustomer(1);
            complainRepository.save(optionalComplain.get());
        }
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
