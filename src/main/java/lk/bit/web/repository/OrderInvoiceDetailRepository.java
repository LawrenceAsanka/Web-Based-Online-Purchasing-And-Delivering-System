package lk.bit.web.repository;

import lk.bit.web.entity.CustomEntity5;
import lk.bit.web.entity.OrderInvoiceDetail;
import lk.bit.web.entity.OrderInvoiceDetailPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderInvoiceDetailRepository extends JpaRepository<OrderInvoiceDetail, OrderInvoiceDetailPK> {

}
