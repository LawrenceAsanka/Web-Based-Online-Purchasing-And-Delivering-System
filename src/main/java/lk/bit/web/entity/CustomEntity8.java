package lk.bit.web.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CustomEntity8 {

    String getOrderId();

    LocalDateTime getOrderDateTime();

    String getCustomerId();

    String getShopId();

    LocalDateTime getAssignedDateTime();

    BigDecimal getNetTotal();
}
