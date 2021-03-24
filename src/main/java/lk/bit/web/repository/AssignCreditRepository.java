package lk.bit.web.repository;

import lk.bit.web.entity.AssignCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignCreditRepository extends JpaRepository<AssignCredit, Integer> {
}
