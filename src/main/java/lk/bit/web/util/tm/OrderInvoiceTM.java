package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderInvoiceTM {

    private String orderId;
    private String orderDateTime;
    private String netTotal;
    private String shopName;
    private String address1;
    private String address2;
    private String city;
    private String district;
    private String productId;
    private String productName;
    private String productImage;
    private String discount;
    private int quantity;
    private String total;
}
