package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.OrderInvoiceBO;
import lk.bit.web.dto.OrderInvoiceDTO;
import lk.bit.web.dto.OrderInvoiceDetailDTO;
import lk.bit.web.entity.*;
import lk.bit.web.repository.*;
import lk.bit.web.util.OrderInvoiceTM;
import lk.bit.web.util.email.EmailSender;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class OrderInvoiceBOImpl implements OrderInvoiceBO {
    @Autowired
    private OrderInvoiceDetailRepository orderInvoiceDetailRepository;
    @Autowired
    private OrderInvoiceRepository orderInvoiceRepository;
    @Autowired
    private CustomerUserRepository customerUserRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private ModelMapper mapper;

    @Override
    public void saveOrder(OrderInvoiceDTO orderInvoiceDTO) {

        CustomerUser customer = customerUserRepository.getCustomerByCustomerEmail(orderInvoiceDTO.getCustomerId());
        Optional<Shop> shop = shopRepository.findById(orderInvoiceDTO.getShopId());
        String orderId = getOrderId();

        if (customer != null && shop.isPresent()) {
            orderInvoiceRepository.save(new OrderInvoice(orderId, customer, shop.get(), new BigDecimal(orderInvoiceDTO.getNetTotal()),
                    LocalDateTime.now().plusHours(3)));

            List<OrderInvoiceDetailDTO> orderInvoiceDetail = orderInvoiceDTO.getOrderInvoiceDetail();

            for (OrderInvoiceDetailDTO orderInvoiceDetailDTO : orderInvoiceDetail) {
                orderInvoiceDetailRepository.save(new OrderInvoiceDetail(
                        orderId, orderInvoiceDetailDTO.getProductId(), orderInvoiceDetailDTO.getQuantity(),
                        orderInvoiceDetailDTO.getTotal(), orderInvoiceDetailDTO.getDiscount()
                ));

                emailSender.sendEmail(customer.getCustomerEmail(), buildOrderEmail(orderId, getDateAndTime(),
                        new BigDecimal(orderInvoiceDTO.getNetTotal()), shop.get()), "Order #" + orderId + " placed successfully");

                Optional<Product> product = productRepository.findById(orderInvoiceDetailDTO.getProductId());
                if (product.isPresent()) {
                    int currentQuantity = product.get().getCurrentQuantity();
                    product.get().setCurrentQuantity(currentQuantity - orderInvoiceDetailDTO.getQuantity());

                    productRepository.save(product.get());


                }
            }

        }
    }

    @Override
    public List<OrderInvoiceDTO> readOrderInvoiceDetailByStatus() {
        List<OrderInvoice> orderInvoiceList = orderInvoiceRepository.getOrderInvoiceByStatus();
        List<OrderInvoiceDTO> orderInvoiceDTOList= new ArrayList<>();

        for (OrderInvoice orderInvoice : orderInvoiceList) {
            OrderInvoiceDTO orderInvoiceDTO = new OrderInvoiceDTO();

            String createdDateTime = orderInvoice.getCreatedDateAndTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            String deadlineDateTime = orderInvoice.getDeadlineDateAndTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

            orderInvoiceDTO.setOrderId(orderInvoice.getOrderId());
            orderInvoiceDTO.setCustomerId(orderInvoice.getCustomerUser().getCustomerId());
            orderInvoiceDTO.setShopId(orderInvoice.getShop().getShopId());
            orderInvoiceDTO.setNetTotal(orderInvoice.getNetTotal().toString());
            orderInvoiceDTO.setCreatedDateTime(createdDateTime);
            orderInvoiceDTO.setDeadlineDateTime(deadlineDateTime);

            orderInvoiceDTOList.add(orderInvoiceDTO);
        }
        return orderInvoiceDTOList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderInvoiceTM> readOrderInvoiceDetailByOrderId(String orderId) {
        List<OrderInvoiceTM> orderInvoiceList = new ArrayList<>();
        List<CustomEntity5> orderInvoiceDetailList = orderInvoiceRepository.getOrderInvoice(orderId);

        for (CustomEntity5 orderInvoice : orderInvoiceDetailList) {

            OrderInvoiceTM orderInvoiceTM = new OrderInvoiceTM();
            String createdDateTime = orderInvoice.getOrderDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

            orderInvoiceTM.setOrderId(orderInvoice.getOrderId());
            orderInvoiceTM.setOrderDateTime(createdDateTime);
            orderInvoiceTM.setNetTotal(orderInvoice.getNetTotal().toString());
            orderInvoiceTM.setShopName(orderInvoice.getShopName());
            orderInvoiceTM.setAddress1(orderInvoice.getAddress1());
            orderInvoiceTM.setAddress2(orderInvoice.getAddress2());
            orderInvoiceTM.setCity(orderInvoice.getCity());
            orderInvoiceTM.setDistrict(orderInvoice.getDistrict());
            orderInvoiceTM.setProductId(orderInvoice.getProductId());
            orderInvoiceTM.setProductName(orderInvoice.getProductName());
            orderInvoiceTM.setProductImage(orderInvoice.getProductImage());
            orderInvoiceTM.setDiscount(orderInvoice.getDiscount().toString());
            orderInvoiceTM.setQuantity(orderInvoice.getQuantity());
            orderInvoiceTM.setTotal(orderInvoice.getTotal().toString());

            orderInvoiceList.add(orderInvoiceTM);
        }

        return orderInvoiceList;
    }

    @Override
    public List<OrderInvoiceDTO> readOrderInvoiceByStatusConfirm() {
        List<OrderInvoice> orderInvoiceList = orderInvoiceRepository.readOrderInvoiceByOrderStatusConfirm();
        List<OrderInvoiceDTO> orderInvoiceDTOList= new ArrayList<>();

        for (OrderInvoice orderInvoice : orderInvoiceList) {
            OrderInvoiceDTO orderInvoiceDTO = new OrderInvoiceDTO();

            String createdDateTime = orderInvoice.getCreatedDateAndTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            String deadlineDateTime = orderInvoice.getDeadlineDateAndTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

            orderInvoiceDTO.setOrderId(orderInvoice.getOrderId());
            orderInvoiceDTO.setCustomerId(orderInvoice.getCustomerUser().getCustomerId());
            orderInvoiceDTO.setShopId(orderInvoice.getShop().getShopId());
            orderInvoiceDTO.setNetTotal(orderInvoice.getNetTotal().toString());
            orderInvoiceDTO.setCreatedDateTime(createdDateTime);
            orderInvoiceDTO.setDeadlineDateTime(deadlineDateTime);

            orderInvoiceDTOList.add(orderInvoiceDTO);
        }
        return orderInvoiceDTOList;

    }

    @Override
    public List<OrderInvoiceDTO> readOrderInvoiceByStatusCancel() {
        List<OrderInvoice> orderInvoiceList = orderInvoiceRepository.readOrderInvoiceByOrderStatusCancel();
        List<OrderInvoiceDTO> orderInvoiceDTOList= new ArrayList<>();

        for (OrderInvoice orderInvoice : orderInvoiceList) {
            OrderInvoiceDTO orderInvoiceDTO = new OrderInvoiceDTO();

            String createdDateTime = orderInvoice.getCreatedDateAndTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            String deadlineDateTime = orderInvoice.getDeadlineDateAndTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

            orderInvoiceDTO.setOrderId(orderInvoice.getOrderId());
            orderInvoiceDTO.setCustomerId(orderInvoice.getCustomerUser().getCustomerId());
            orderInvoiceDTO.setShopId(orderInvoice.getShop().getShopId());
            orderInvoiceDTO.setNetTotal(orderInvoice.getNetTotal().toString());
            orderInvoiceDTO.setCreatedDateTime(createdDateTime);
            orderInvoiceDTO.setDeadlineDateTime(deadlineDateTime);

            orderInvoiceDTOList.add(orderInvoiceDTO);
        }
        return orderInvoiceDTOList;
    }

    @Override
    public List<OrderInvoiceDTO> readOrderInvoiceByCustomerId(String customerId) {
        List<OrderInvoice> orderInvoiceList = orderInvoiceRepository.getOrderInvoiceByCustomerId(customerId);
        List<OrderInvoiceDTO> orderInvoiceDTOList= new ArrayList<>();

        for (OrderInvoice orderInvoice : orderInvoiceList) {
            OrderInvoiceDTO orderInvoiceDTO = new OrderInvoiceDTO();

            String createdDateTime = orderInvoice.getCreatedDateAndTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            String deadlineDateTime = orderInvoice.getDeadlineDateAndTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

            orderInvoiceDTO.setOrderId(orderInvoice.getOrderId());
            orderInvoiceDTO.setCustomerId(orderInvoice.getCustomerUser().getCustomerId());
            orderInvoiceDTO.setShopId(orderInvoice.getShop().getShopId());
            orderInvoiceDTO.setNetTotal(orderInvoice.getNetTotal().toString());
            orderInvoiceDTO.setCreatedDateTime(createdDateTime);
            orderInvoiceDTO.setDeadlineDateTime(deadlineDateTime);
            orderInvoiceDTO.setStatus(orderInvoice.getStatus());

            orderInvoiceDTOList.add(orderInvoiceDTO);
        }
        return orderInvoiceDTOList;
    }

    @Override
    public void updateStatus(String orderId) {
        Optional<OrderInvoice> orderInvoice = orderInvoiceRepository.findById(orderId);

        if (orderInvoice.isPresent()) {
            orderInvoice.get().setStatus(1);

            orderInvoiceRepository.save(orderInvoice.get());
        }
    }

    @Override
    public boolean IExistOrderByOrderId(String id) {
        return orderInvoiceRepository.existsById(id);
    }

    @Override
    public int getTotalConfirmOrderCount() {
        return orderInvoiceRepository.getTotalConfirmOrderCount();
    }

    // check every one minutes
    @Scheduled(cron = "*/60 * * * * *")
    protected void autoConfirmOrder() {
        List<OrderInvoice> orderInvoiceList = orderInvoiceRepository.getOrderInvoiceByStatus();

        for (OrderInvoice orderInvoice : orderInvoiceList) {
            LocalDateTime createdDateAndTime = orderInvoice.getCreatedDateAndTime();
            long hourDifference = ChronoUnit.HOURS.between(createdDateAndTime, LocalDateTime.now());
            if (hourDifference >= 3) {
                orderInvoice.setStatus(2);
                orderInvoiceRepository.save(orderInvoice);

                //TODO send a mail
            }
        }
    }

    private String getOrderId() {
        String lastOrderId = orderInvoiceRepository.getOrderId();
        String newId = "";

        if (lastOrderId == null) {
            newId = "OD01";
        } else {
            String replaceId = lastOrderId.replaceAll("OD", "");
            int id = Integer.parseInt(replaceId) + 1;
            if (id < 10) {
                newId = "OD0" + id;
            } else {
                newId = "OD" + id;
            }
        }
        return newId;
    }

    private String getDateAndTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
    }

    private OrderInvoiceDTO getOrderInvoiceDTO(OrderInvoice orderInvoice){
        return mapper.map(orderInvoice, OrderInvoiceDTO.class);
    }

    private OrderInvoiceTM getOrderInvoiceTM(CustomEntity5 customEntity5){
        return mapper.map(customEntity5, OrderInvoiceTM.class);
    }

    private String buildOrderEmail(String orderId, String dateTime, BigDecimal netTotal, Shop shop) {

        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" style=\"width:100%;font-family:arial, 'helvetica neue', helvetica, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\">\n" +
                " <head> \n" +
                "  <meta charset=\"UTF-8\"> \n" +
                "  <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\"> \n" +
                "  <meta name=\"x-apple-disable-message-reformatting\"> \n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> \n" +
                "  <meta content=\"telephone=no\" name=\"format-detection\"> \n" +
                "  <title>New email template 2021-02-11</title> \n" +
                "  <!--[if (mso 16)]>\n" +
                "    <style type=\"text/css\">\n" +
                "    a {text-decoration: none;}\n" +
                "    </style>\n" +
                "    <![endif]--> \n" +
                "  <!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]--> \n" +
                "  <!--[if gte mso 9]>\n" +
                "<xml>\n" +
                "    <o:OfficeDocumentSettings>\n" +
                "    <o:AllowPNG></o:AllowPNG>\n" +
                "    <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "    </o:OfficeDocumentSettings>\n" +
                "</xml>\n" +
                "<![endif]--> \n" +
                "  <style type=\"text/css\">\n" +
                "#outlook a {\n" +
                "\tpadding:0;\n" +
                "}\n" +
                ".ExternalClass {\n" +
                "\twidth:100%;\n" +
                "}\n" +
                ".ExternalClass,\n" +
                ".ExternalClass p,\n" +
                ".ExternalClass span,\n" +
                ".ExternalClass font,\n" +
                ".ExternalClass td,\n" +
                ".ExternalClass div {\n" +
                "\tline-height:100%;\n" +
                "}\n" +
                ".es-button {\n" +
                "\tmso-style-priority:100!important;\n" +
                "\ttext-decoration:none!important;\n" +
                "}\n" +
                "a[x-apple-data-detectors] {\n" +
                "\tcolor:inherit!important;\n" +
                "\ttext-decoration:none!important;\n" +
                "\tfont-size:inherit!important;\n" +
                "\tfont-family:inherit!important;\n" +
                "\tfont-weight:inherit!important;\n" +
                "\tline-height:inherit!important;\n" +
                "}\n" +
                ".es-desk-hidden {\n" +
                "\tdisplay:none;\n" +
                "\tfloat:left;\n" +
                "\toverflow:hidden;\n" +
                "\twidth:0;\n" +
                "\tmax-height:0;\n" +
                "\tline-height:0;\n" +
                "\tmso-hide:all;\n" +
                "}\n" +
                "@media only screen and (max-width:600px) {p, ul li, ol li, a { font-size:16px!important; line-height:150%!important } h1 { font-size:30px!important; text-align:center; line-height:120%!important } h2 { font-size:26px!important; text-align:center; line-height:120%!important } h3 { font-size:20px!important; text-align:center; line-height:120%!important } h1 a { font-size:30px!important } h2 a { font-size:26px!important } h3 a { font-size:20px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:16px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:16px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } .es-btn-fw { border-width:10px 0px!important; text-align:center!important } .es-adaptive table, .es-btn-fw, .es-btn-fw-brdr, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden { width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } tr.es-desk-hidden { display:table-row!important } table.es-desk-hidden { display:table!important } td.es-desk-menu-hidden { display:table-cell!important } .es-menu td { width:1%!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } .es-menu td a { font-size:16px!important } a.es-button, button.es-button { font-size:20px!important; display:block!important; border-left-width:0px!important; border-right-width:0px!important } }\n" +
                "</style> \n" +
                " </head> \n" +
                " <body style=\"width:100%;font-family:arial, 'helvetica neue', helvetica, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\"> \n" +
                "  <div class=\"es-wrapper-color\" style=\"background-color:#EFEFEF\"> \n" +
                "   <!--[if gte mso 9]>\n" +
                "\t\t\t<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">\n" +
                "\t\t\t\t<v:fill type=\"tile\" color=\"#efefef\"></v:fill>\n" +
                "\t\t\t</v:background>\n" +
                "\t\t<![endif]--> \n" +
                "   <table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top\"> \n" +
                "     <tr style=\"border-collapse:collapse\"> \n" +
                "      <td valign=\"top\" style=\"padding:0;Margin:0\"> \n" +
                "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-header\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top\"> \n" +
                "         <tr style=\"border-collapse:collapse\"> \n" +
                "          <td align=\"center\" style=\"padding:0;Margin:0\"> \n" +
                "           <table class=\"es-header-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FEF5E4;width:600px\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#fef5e4\" align=\"center\"> \n" +
                "             <tr style=\"border-collapse:collapse\"> \n" +
                "              <td align=\"left\" style=\"padding:0;Margin:0\"> \n" +
                "               <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n" +
                "                 <tr style=\"border-collapse:collapse\"> \n" +
                "                  <td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;width:600px\"> \n" +
                "                   <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n" +
                "                     <tr style=\"border-collapse:collapse\"> \n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0;font-size:0px\"><a target=\"_blank\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;font-size:14px;text-decoration:underline;color:#999999\"><img class=\"adapt-img\" src=\"https://nyzuxl.stripocdn.email/content/guids/79c9fed7-e0fd-46bd-8ba4-d3210668c6c9/images/57411613015986966.png\" alt style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\" width=\"600\"></a></td> \n" +
                "                     </tr> \n" +
                "                   </table></td> \n" +
                "                 </tr> \n" +
                "               </table></td> \n" +
                "             </tr> \n" +
                "           </table></td> \n" +
                "         </tr> \n" +
                "       </table> \n" +
                "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"> \n" +
                "         <tr style=\"border-collapse:collapse\"> \n" +
                "          <td align=\"center\" style=\"padding:0;Margin:0\"> \n" +
                "           <table class=\"es-content-body\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\"> \n" +
                "             <tr style=\"border-collapse:collapse\"> \n" +
                "              <td align=\"left\" style=\"Margin:0;padding-top:10px;padding-bottom:10px;padding-left:20px;padding-right:20px\"> \n" +
                "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n" +
                "                 <tr style=\"border-collapse:collapse\"> \n" +
                "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:560px\"> \n" +
                "                   <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;border-radius:0px\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\"> \n" +
                "                     <tr style=\"border-collapse:collapse\"> \n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-top:25px;font-size:0px\"><img class=\"adapt-img\" src=\"https://nyzuxl.stripocdn.email/content/guids/79c9fed7-e0fd-46bd-8ba4-d3210668c6c9/images/30631613016796009.png\" alt style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\" width=\"147\"></td> \n" +
                "                     </tr> \n" +
                "                     <tr style=\"border-collapse:collapse\"> \n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-top:10px;padding-bottom:15px\"><h1 style=\"Margin:0;line-height:36px;mso-line-height-rule:exactly;font-family:'trebuchet ms', helvetica, sans-serif;font-size:30px;font-style:normal;font-weight:normal;color:#333333\"><br>Thank you for your purchase</h1></td> \n" +
                "                     </tr> \n" +
                "                     <tr style=\"border-collapse:collapse\"> \n" +
                "                      <td align=\"center\" style=\"Margin:0;padding-top:5px;padding-bottom:5px;padding-left:40px;padding-right:40px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333\"><b>Your order has been placed successfully. You can</b><strong>&nbsp;<span style=\"color:#FF8C00\">cancel </span></strong><b>&nbsp;the order within next</b><strong>&nbsp;<span style=\"color:#FF8C00\">three</span></strong><b>&nbsp;hours.</b><br><strong><span>If you have any questions, please contact us at</span><span style=\"color:#FFA500\"></span></strong><br><span style=\"color:#FFA500\"><strong>vgdisrtributors@gmail.com</strong></span><strong><span style=\"color:#FFA500\"></span></strong><br></p></td> \n" +
                "                     </tr> \n" +
                "                   </table></td> \n" +
                "                 </tr> \n" +
                "               </table></td> \n" +
                "             </tr> \n" +
                "           </table></td> \n" +
                "         </tr> \n" +
                "       </table> \n" +
                "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"> \n" +
                "         <tr style=\"border-collapse:collapse\"> \n" +
                "          <td align=\"center\" style=\"padding:0;Margin:0\"> \n" +
                "           <table class=\"es-content-body\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\"> \n" +
                "             <tr style=\"border-collapse:collapse\"> \n" +
                "              <td align=\"left\" style=\"Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;padding-bottom:30px\"> \n" +
                "               <!--[if mso]><table style=\"width:560px\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"width:280px\" valign=\"top\"><![endif]--> \n" +
                "               <table class=\"es-left\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;float:left\"> \n" +
                "                 <tr style=\"border-collapse:collapse\"> \n" +
                "                  <td class=\"es-m-p20b\" align=\"left\" style=\"padding:0;Margin:0;width:280px\"> \n" +
                "                   <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;background-color:#FEF9EF;border-color:#EFEFEF;border-width:1px 0px 1px 1px;border-style:solid\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#fef9ef\" role=\"presentation\"> \n" +
                "                     <tr style=\"border-collapse:collapse\"> \n" +
                "                      <td align=\"left\" style=\"Margin:0;padding-bottom:10px;padding-top:20px;padding-left:20px;padding-right:20px\"><h4 style=\"Margin:0;line-height:120%;mso-line-height-rule:exactly;font-family:'trebuchet ms', helvetica, sans-serif\">SUMMARY:</h4></td> \n" +
                "                     </tr> \n" +
                "                     <tr style=\"border-collapse:collapse\"> \n" +
                "                      <td align=\"left\" style=\"padding:0;Margin:0;padding-bottom:20px;padding-left:20px;padding-right:20px\"> \n" +
                "                       <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;width:100%\" class=\"cke_show_border\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\" align=\"left\" role=\"presentation\"> \n" +
                "                         <tr style=\"border-collapse:collapse\"> \n" +
                "                          <td style=\"padding:0;Margin:0\"><span style=\"font-size:14px;line-height:21px\">Order #:</span></td> \n" +
                "                          <td style=\"padding:0;Margin:0\"><span style=\"font-size:14px;line-height:21px\">"+orderId+"</span></td> \n" +
                "                         </tr> \n" +
                "                         <tr style=\"border-collapse:collapse\"> \n" +
                "                          <td style=\"padding:0;Margin:0\"><span style=\"font-size:14px;line-height:21px\">Order Date:</span></td> \n" +
                "                          <td style=\"padding:0;Margin:0\"><span style=\"font-size:14px;line-height:21px\">"+dateTime+"</span></td> \n" +
                "                         </tr> \n" +
                "                         <tr style=\"border-collapse:collapse\"> \n" +
                "                          <td style=\"padding:0;Margin:0\"><span style=\"font-size:14px;line-height:21px\">Order Total:</span></td> \n" +
                "                          <td style=\"padding:0;Margin:0\"><span style=\"font-size:14px;line-height:21px\">Rs."+String.format("%.2f", netTotal)+"</span></td> \n" +
                "                         </tr> \n" +
                "                       </table><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333\"><br></p></td> \n" +
                "                     </tr> \n" +
                "                   </table></td> \n" +
                "                 </tr> \n" +
                "               </table> \n" +
                "               <!--[if mso]></td><td style=\"width:0px\"></td><td style=\"width:280px\" valign=\"top\"><![endif]--> \n" +
                "               <table class=\"es-right\" cellspacing=\"0\" cellpadding=\"0\" align=\"right\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;float:right\"> \n" +
                "                 <tr style=\"border-collapse:collapse\"> \n" +
                "                  <td align=\"left\" style=\"padding:0;Margin:0;width:280px\"> \n" +
                "                   <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;background-color:#FEF9EF;border-width:1px;border-style:solid;border-color:#EFEFEF\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#fef9ef\" role=\"presentation\"> \n" +
                "                     <tr style=\"border-collapse:collapse\"> \n" +
                "                      <td align=\"left\" style=\"Margin:0;padding-bottom:10px;padding-top:20px;padding-left:20px;padding-right:20px\"><h4 style=\"Margin:0;line-height:120%;mso-line-height-rule:exactly;font-family:'trebuchet ms', helvetica, sans-serif\">SHIPPING ADDRESS:</h4></td> \n" +
                "                     </tr> \n" +
                "                     <tr style=\"border-collapse:collapse\"> \n" +
                "                      <td align=\"left\" style=\"padding:0;Margin:0;padding-bottom:20px;padding-left:20px;padding-right:20px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333\">"+shop.getShopName()+"</p><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333\">"+shop.getAddress1()+"</p><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333\">"+shop.getAddress2()+""+shop.getContact()+"</p></td> \n" +
                "                     </tr> \n" +
                "                   </table></td> \n" +
                "                 </tr> \n" +
                "               </table> \n" +
                "               <!--[if mso]></td></tr></table><![endif]--></td> \n" +
                "             </tr> \n" +
                "           </table></td> \n" +
                "         </tr> \n" +
                "       </table> \n" +
                "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-footer\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top\"> \n" +
                "         <tr style=\"border-collapse:collapse\"> \n" +
                "          <td align=\"center\" style=\"padding:0;Margin:0\"> \n" +
                "           <table class=\"es-footer-body\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FEF5E4;width:600px\"> \n" +
                "             <tr style=\"border-collapse:collapse\"> \n" +
                "              <td align=\"left\" style=\"padding:20px;Margin:0\"> \n" +
                "               <table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n" +
                "                 <tr style=\"border-collapse:collapse\"> \n" +
                "                  <td align=\"left\" style=\"padding:0;Margin:0;width:560px\"> \n" +
                "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n" +
                "                     <tr style=\"border-collapse:collapse\"> \n" +
                "                      <td align=\"center\" style=\"padding:0;Margin:0;font-size:0\"> \n" +
                "                       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-table-not-adapt es-social\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n" +
                "                         <tr style=\"border-collapse:collapse\"> \n" +
                "                          <td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;padding-right:10px\"><img src=\"https://nyzuxl.stripocdn.email/content/assets/img/social-icons/circle-colored/twitter-circle-colored.png\" alt=\"Tw\" title=\"Twitter\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td> \n" +
                "                          <td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;padding-right:10px\"><img src=\"https://nyzuxl.stripocdn.email/content/assets/img/social-icons/circle-colored/facebook-circle-colored.png\" alt=\"Fb\" title=\"Facebook\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td> \n" +
                "                          <td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0\"><img src=\"https://stripo.email/static/assets/img/messenger-icons/circle-colored/whatsapp-circle-colored.png\" alt=\"Whatsapp\" title=\"Whatsapp\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td> \n" +
                "                         </tr> \n" +
                "                       </table></td> \n" +
                "                     </tr> \n" +
                "                   </table></td> \n" +
                "                 </tr> \n" +
                "               </table></td> \n" +
                "             </tr> \n" +
                "           </table></td> \n" +
                "         </tr> \n" +
                "       </table></td> \n" +
                "     </tr> \n" +
                "   </table> \n" +
                "  </div>  \n" +
                " </body>\n" +
                "</html>";
    }

}
