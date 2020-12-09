package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.SupplierInvoiceBO;
import lk.bit.web.dto.SupplierInvoiceDTO;
import lk.bit.web.dto.SupplierInvoiceDetailDTO;
import lk.bit.web.entity.*;
import lk.bit.web.repository.ProductRepository;
import lk.bit.web.repository.SupplierInvoiceDetailRepository;
import lk.bit.web.repository.SupplierInvoiceRepository;
import lk.bit.web.repository.UserRepository;
import lk.bit.web.util.InvoiceDetailTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class SupplierInvoiceBOImpl implements SupplierInvoiceBO {

    @Autowired
    private SupplierInvoiceRepository supplierInvoiceRepository;
    @Autowired
    private SupplierInvoiceDetailRepository supplierInvoiceDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveSupplierInvoice(SupplierInvoiceDTO invoiceDetails) {

        User user = userRepository.findById(invoiceDetails.getUserId()).get();

       /* try {
            Date twelveHours = new SimpleDateFormat("dd/MM/yyyy hh.mm aa")
                    .parse(invoiceDetails.getDateTime().toString());
            supplierInvoiceRepository.save(new SupplierInvoice(
                    invoiceDetails.getInvoiceNumber(),twelveHours,user
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        supplierInvoiceRepository.save(new SupplierInvoice(
                invoiceDetails.getInvoiceNumber(),invoiceDetails.getDateTime(),user
        ));

        List<SupplierInvoiceDetailDTO> productDetails = invoiceDetails.getInvoiceDetail();
        for (SupplierInvoiceDetailDTO productDetail : productDetails) {

            supplierInvoiceDetailRepository.save(new SupplierInvoiceDetail(
                    invoiceDetails.getInvoiceNumber(), productDetail.getProductCode(), productDetail.getTotalQty(),
                    productDetail.getPricePerQty(),productDetail.getDiscount()
            ));

            Product product = productRepository.findById(productDetail.getProductCode()).get();
            System.out.println(product);
            product.setCurrentQuantity(product.getCurrentQuantity() + productDetail.getTotalQty());
            product.setQuantityBuyingPrice(productDetail.getPricePerQty());
            productRepository.save(product);
        }
    }

    @Override
    public List<InvoiceDetailTM> getInvoiceDetails() {
        List<CustomEntity3> invoiceDetails = supplierInvoiceRepository.getInvoiceDetails();
        List<InvoiceDetailTM> invoiceDetail = new ArrayList<>();
        for (CustomEntity3 details : invoiceDetails) {
            invoiceDetail.add(new InvoiceDetailTM(details.getInvoiceNumber(), details.getDateTime(),
                    details.getUserId(), details.getUserName(), details.getNetAmount()));
        }
        return invoiceDetail;
    }
}
