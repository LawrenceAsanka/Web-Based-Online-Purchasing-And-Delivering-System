package lk.bit.web.business.custom;

import lk.bit.web.dto.ReturnDTO;
import lk.bit.web.entity.SuperEntity;
import lk.bit.web.repository.ReturnRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface ReturnBO extends SuperEntity {

    void saveReturnDetail(ReturnDTO returnDTO);
}
