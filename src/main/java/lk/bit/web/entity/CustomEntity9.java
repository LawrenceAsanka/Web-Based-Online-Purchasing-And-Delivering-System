package lk.bit.web.entity;

import java.time.LocalDateTime;

public interface CustomEntity9 {

    String getOrderId();

    LocalDateTime getOrderedDateTime();

    String getOrderBy();

    String getDeliverBy();

    LocalDateTime getDeliveredDateTime();

    String getPaymentMethod();

    String getStatus();
}
