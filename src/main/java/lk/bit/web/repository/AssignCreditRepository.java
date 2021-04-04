package lk.bit.web.repository;

import lk.bit.web.entity.AssignCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignCreditRepository extends JpaRepository<AssignCredit, Integer> {

    @Query(value = "SELECT id FROM assigned_credit WHERE credit_id=?1", nativeQuery = true)
    int readAssignCreditByCreditId(String creditId);
}
