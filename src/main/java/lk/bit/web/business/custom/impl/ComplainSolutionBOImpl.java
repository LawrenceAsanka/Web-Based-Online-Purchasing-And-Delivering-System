package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ComplainSolutionBO;
import lk.bit.web.dto.ComplainSolutionDTO;
import lk.bit.web.entity.Complain;
import lk.bit.web.entity.ComplainSolution;
import lk.bit.web.entity.SystemUser;
import lk.bit.web.repository.ComplainRepository;
import lk.bit.web.repository.ComplainSolutionRepository;
import lk.bit.web.repository.SystemUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ComplainSolutionBOImpl implements ComplainSolutionBO {
    @Autowired
    private ComplainSolutionRepository complainSolutionRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private ComplainRepository complainRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public void save(ComplainSolutionDTO complainSolutionDTO) {

        SystemUser systemUser = systemUserRepository.findSystemUser(complainSolutionDTO.getCreatedBy());
        Complain complain = complainRepository.findById(complainSolutionDTO.getComplaintId()).get();

        ComplainSolution complainSolution = mapper.map(complainSolutionDTO, ComplainSolution.class);

        complainSolution.setId(getSolutionId());
        complainSolution.setComplain(complain);
        complainSolution.setSystemUser(systemUser);

        complainSolutionRepository.save(complainSolution);

        //update status in complain table
        complain.setResponseStatus(1);
        complainRepository.save(complain);
    }

    @Override
    public int getUnReadMsgCount() {
        return complainSolutionRepository.getUnReadMsgCount();
    }

    private String getSolutionId(){
        String lastSolutionId = complainSolutionRepository.getLastSolutionId();
        String newId = "";

        if (lastSolutionId == null) {
            newId = "SOL01";
        } else {
            String idNumber = lastSolutionId.replaceAll("SOL", "");
            int id = Integer.parseInt(idNumber) + 1;
            if (id < 10) {
                newId = "SOL0" + id;
            } else {
                newId = "SOL" + id;
            }
        }
        return newId;
    }
}
