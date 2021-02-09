package lk.bit.web.repository;

import lk.bit.web.entity.OrderInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInvoiceRepository extends JpaRepository<OrderInvoice, String> {
}
