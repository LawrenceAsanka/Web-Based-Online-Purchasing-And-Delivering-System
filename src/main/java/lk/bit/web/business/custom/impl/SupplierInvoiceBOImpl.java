package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.SupplierInvoiceBO;
import lk.bit.web.dto.SupplierInvoiceDTO;
import lk.bit.web.dto.SupplierInvoiceDetailDTO;
import lk.bit.web.entity.*;
import lk.bit.web.repository.ProductRepository;
import lk.bit.web.repository.SupplierInvoiceDetailRepository;
import lk.bit.web.repository.SupplierInvoiceRepository;
import lk.bit.web.repository.SystemUserRepository;
import lk.bit.web.util.tm.InvoiceDetailTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private SystemUserRepository systemUserRepository;

    @Override
    public void saveSupplierInvoice(SupplierInvoiceDTO invoiceDetails) {

        SystemUser systemUser = systemUserRepository.findSystemUser(invoiceDetails.getUserName());

        supplierInvoiceRepository.save(new SupplierInvoice(
                invoiceDetails.getInvoiceNumber(), invoiceDetails.getDateTime(), systemUser
        ));

        List<SupplierInvoiceDetailDTO> productDetails = invoiceDetails.getInvoiceDetail();
        for (SupplierInvoiceDetailDTO productDetail : productDetails) {

            supplierInvoiceDetailRepository.save(new SupplierInvoiceDetail(
                    invoiceDetails.getInvoiceNumber(), productDetail.getProductCode(), productDetail.getTotalQty(),
                    productDetail.getPricePerQty(), productDetail.getDiscount()
            ));

            Product product = productRepository.findById(productDetail.getProductCode()).get();

            product.setCurrentQuantity(product.getCurrentQuantity() + productDetail.getTotalQty());
            product.setQuantityBuyingPrice(productDetail.getPricePerQty());
            productRepository.save(product);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDetailTM> getAllInvoiceDetails() {
        List<CustomEntity3> invoiceDetails = supplierInvoiceRepository.getAllInvoiceDetails();
        List<InvoiceDetailTM> invoiceDetail = new ArrayList<>();
        for (CustomEntity3 details : invoiceDetails) {
            String netAmount = String.format("%.2f", new BigDecimal(details.getNetAmount()));

            invoiceDetail.add(new InvoiceDetailTM(details.getInvoiceNumber(), details.getDateTime(),
                    details.getUserId(), details.getUserName(), netAmount));
        }
        return invoiceDetail;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existInvoice(String invoiceNumber) {
        return supplierInvoiceRepository.existsById(invoiceNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierInvoiceDetailDTO> getInvoiceDetail(String invoiceNumber) {
        List<SupplierInvoiceDetailDTO> invoice = new ArrayList<>();
        List<SupplierInvoiceDetail> invoiceDetails = supplierInvoiceDetailRepository.getInvoiceDetail(invoiceNumber);
        invoiceDetails.forEach(id -> invoice.add(new SupplierInvoiceDetailDTO(
                id.getProduct().getProductId(), id.getQty(), id.getQtyPrice(), id.getDiscount()
        )));
        return invoice;
    }

}
