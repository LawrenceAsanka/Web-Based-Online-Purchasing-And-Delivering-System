package lk.bit.web.repository;

import lk.bit.web.entity.ReturnDetail;
import lk.bit.web.entity.ReturnDetailPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReturnDetailRepository extends JpaRepository<ReturnDetail, ReturnDetailPK> {

    @Query(value = "SELECT * FROM return_detail WHERE return_id=?1",nativeQuery = true)
    public List<ReturnDetail> readReturnDetailByReturnId(String returnID);
}
