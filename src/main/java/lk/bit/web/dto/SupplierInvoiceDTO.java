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

    private String userId;
    private String invoiceNumber;
    private Date dateTime;
    private List<SupplierInvoiceDetailDTO> invoiceDetail;
}
