package lk.bit.web.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierInvoiceDTO {

    private String userName;
    private String invoiceNumber;
    private String dateTime;
    private List<SupplierInvoiceDetailDTO> invoiceDetail;
}
