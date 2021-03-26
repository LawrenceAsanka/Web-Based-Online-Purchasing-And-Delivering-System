package lk.bit.web.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CustomEntity14 {

    String getCreditId();

    String getOrderId();

    LocalDateTime getCreditDate();

    String getCreditor();

    String getAssignee();

    LocalDateTime getSettleDay();

    BigDecimal getCreditAmount();

}
