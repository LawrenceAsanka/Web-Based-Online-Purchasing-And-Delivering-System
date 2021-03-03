package lk.bit.web.repository;

import lk.bit.web.entity.AssignOrderInvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignOrderInvoiceDetailRepository extends JpaRepository<AssignOrderInvoiceDetail, Integer> {


}
