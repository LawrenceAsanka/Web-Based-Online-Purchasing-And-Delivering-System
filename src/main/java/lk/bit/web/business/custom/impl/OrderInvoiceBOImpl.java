package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.OrderInvoiceBO;
import lk.bit.web.dto.OrderInvoiceDTO;
import lk.bit.web.dto.OrderInvoiceDetailDTO;
import lk.bit.web.entity.*;
import lk.bit.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class OrderInvoiceBOImpl implements OrderInvoiceBO {
    @Autowired
    private OrderInvoiceDetailRepository orderInvoiceDetailRepository;
    @Autowired
    private OrderInvoiceRepository orderInvoiceRepository;
    @Autowired
    private CustomerUserRepository customerUserRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void saveOrder(OrderInvoiceDTO orderInvoiceDTO) {

        CustomerUser customer = customerUserRepository.getCustomerByCustomerEmail(orderInvoiceDTO.getCustomerId());
        Optional<Shop> shop = shopRepository.findById(orderInvoiceDTO.getShopId());
        String orderId = getOrderId();

        if (customer != null && shop.isPresent()) {
            orderInvoiceRepository.save(new OrderInvoice(orderId, customer, shop.get()));

            List<OrderInvoiceDetailDTO> orderInvoiceDetail = orderInvoiceDTO.getOrderInvoiceDetail();
            for (OrderInvoiceDetailDTO orderInvoiceDetailDTO : orderInvoiceDetail) {
                orderInvoiceDetailRepository.save(new OrderInvoiceDetail(
                        orderId, orderInvoiceDetailDTO.getProductId(), orderInvoiceDetailDTO.getTotal(),
                        orderInvoiceDetailDTO.getDiscount()
                ));

                Optional<Product> product = productRepository.findById(orderInvoiceDetailDTO.getProductId());
                if (product.isPresent()) {
                    int currentQuantity = product.get().getCurrentQuantity();
                    product.get().setCurrentQuantity(currentQuantity - orderInvoiceDetailDTO.getQuantity());

                    productRepository.save(product.get());
                }
            }

        }
    }

    @Override
    // check every one minutes
    @Scheduled(cron = "*/60 * * * * *")
    public void autoConfirmOrder() {
        List<OrderInvoice> orderInvoiceList = orderInvoiceRepository.getOrderInvoiceByStatus();

        for (OrderInvoice orderInvoice : orderInvoiceList) {
            LocalDateTime createdDateAndTime = orderInvoice.getCreatedDateAndTime();
            long hourDifference = ChronoUnit.HOURS.between(createdDateAndTime, LocalDateTime.now());
            if (hourDifference >= 3) {
                orderInvoice.setStatus(2);
                orderInvoiceRepository.save(orderInvoice);
            }
        }
    }

    private String getOrderId(){
        String lastOrderId = orderInvoiceRepository.getOrderId();
        String newId = "";

        if (lastOrderId == null) {
            newId = "OD01";
        } else {
            String replaceId = lastOrderId.replaceAll("OD", "");
            int id = Integer.parseInt(replaceId) + 1;
            newId = "OD0" + id;
        }
        return newId;
    }
}
