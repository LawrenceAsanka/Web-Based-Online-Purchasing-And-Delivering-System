package lk.bit.web.business.custom;

import lk.bit.web.dto.ComplainSolutionDTO;
import lk.bit.web.entity.ComplainSolution;

public interface ComplainSolutionBO {

    public void save(ComplainSolutionDTO complainSolutionDTO);
    public int getUnReadMsgCount();
}
