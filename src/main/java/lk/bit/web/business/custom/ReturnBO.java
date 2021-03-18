package lk.bit.web.business.custom;

import lk.bit.web.dto.ReturnDTO;
import lk.bit.web.entity.SuperEntity;
import lk.bit.web.util.tm.ReturnTM;

import java.util.List;

public interface ReturnBO extends SuperEntity {

    void saveReturnDetail(ReturnDTO returnDTO);

    List<ReturnTM> readAllByStatusCancel();
}
