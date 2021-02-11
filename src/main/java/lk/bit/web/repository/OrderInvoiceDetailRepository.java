package lk.bit.web.repository;

import lk.bit.web.entity.OrderInvoiceDetail;
import lk.bit.web.entity.OrderInvoiceDetailPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInvoiceDetailRepository extends JpaRepository<OrderInvoiceDetail, OrderInvoiceDetailPK> {
}
