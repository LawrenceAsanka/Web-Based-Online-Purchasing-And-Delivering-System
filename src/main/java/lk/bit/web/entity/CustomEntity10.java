package lk.bit.web.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CustomEntity10 {

    String getOrderId();

    String getDeliveryId();

    LocalDateTime getAssignedDateTime();

    String getShopName();

    LocalDateTime getDeliveredDateTime();

    BigDecimal getTotalAmount();
}
