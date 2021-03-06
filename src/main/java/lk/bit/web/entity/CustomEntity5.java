package lk.bit.web.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CustomEntity5 {

    String getOrderId();

    LocalDateTime getOrderDateTime();

    BigDecimal getNetTotal();

    String getCustomerId();

    String getPaymentMethod();

    String getShopName();

    String getAddress1();

    String getAddress2();

    String getCity();

    String getDistrict();

    String getContact();

    String getProductId();

    String getProductName();

    String getProductImage();

    BigDecimal getDiscount();

    int getQuantity();

    BigDecimal getTotal();

}
