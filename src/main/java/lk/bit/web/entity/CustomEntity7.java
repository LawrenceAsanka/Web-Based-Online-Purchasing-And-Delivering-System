package lk.bit.web.entity;

import java.time.LocalDateTime;

public interface CustomEntity7 {

    String getOrderId();

    LocalDateTime getOrderDateTime();

    LocalDateTime getAssignedDateTime();

    String getCustomerId();

    String getAssignee();

    String getShopId();

    String getNetTotal();

    int getOrderStatus();
}
