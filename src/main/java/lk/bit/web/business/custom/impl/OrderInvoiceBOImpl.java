package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.OrderInvoiceBO;
import lk.bit.web.dto.DeliveryOrderDTO;
import lk.bit.web.dto.OrderInvoiceDTO;
import lk.bit.web.dto.OrderInvoiceDetailDTO;
import lk.bit.web.entity.*;
import lk.bit.web.repository.*;
import lk.bit.web.util.tm.*;
import lk.bit.web.util.email.EmailSender;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    private SystemUserRepository systemUserRepository;
    @Autowired
    private AssignOrderInvoiceDetailRepository assignOrderInvoiceDetailRepository;
    @Autowired
    private CompleteDeliveryRepository completeDeliveryRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private ModelMapper mapper;

    @Override
    public void saveOrder(OrderInvoiceDTO orderInvoiceDTO) {

        CustomerUser customer = customerUserRepository.getCustomerByCustomerEmail(orderInvoiceDTO.getCustomerId());
        Optional<Shop> shop = shopRepository.findById(orderInvoiceDTO.getShopId());
        String orderId = getOrderId();
        String paymentMethod = null;

        if (customer != null && shop.isPresent()) {

            // payment method check
            if (orderInvoiceDTO.getPaymentMethod().equals("1")) {
                paymentMethod = PaymentMethod.COD.name();
            } else if (orderInvoiceDTO.getPaymentMethod().equals("2")) {
                paymentMethod = PaymentMethod.CREDIT.name();
            }

            orderInvoiceRepository.save(new OrderInvoice(orderId, customer, shop.get(), new BigDecimal(orderInvoiceDTO.getNetTotal()),
                    paymentMethod, LocalDateTime.now().plusHours(3)));

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
            orderInvoiceDTO.setShopId(orderInvoice.getShop().getShopName());
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
            orderInvoiceDTO.setStatus(orderInvoice.getStatus());

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
            orderInvoiceDTO.setStatus(orderInvoice.getStatus());

            orderInvoiceDTOList.add(orderInvoiceDTO);
        }
        return orderInvoiceDTOList;
    }

    @Override
    public List<CompleteDeliveryDetailTM> readOrderInvoiceByStatusComplete() {
        List<CustomEntity9> allCompletedDeliveryDetails = completeDeliveryRepository.getAllCompletedDeliveryDetails();
        List<CompleteDeliveryDetailTM> completeOrderDetails= new ArrayList<>();

        for (CustomEntity9 orderInvoice : allCompletedDeliveryDetails) {
            CompleteDeliveryDetailTM completeOrderDetail = new CompleteDeliveryDetailTM();

            String orderedDateTime = orderInvoice.getOrderedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            String deliveredDateTime = orderInvoice.getDeliveredDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));

            completeOrderDetail.setOrderId(orderInvoice.getOrderId());
            completeOrderDetail.setOrderedDateTime(orderedDateTime);
            completeOrderDetail.setOrderBy(orderInvoice.getOrderBy());

            SystemUser systemUser = systemUserRepository.findById(orderInvoice.getDeliverBy()).get();
            completeOrderDetail.setDeliverBy(systemUser.getFirstName()+" "+systemUser.getLastName());

            completeOrderDetail.setDeliveredDateTime(deliveredDateTime);
            completeOrderDetail.setPaymentMethod(orderInvoice.getPaymentMethod());
            completeOrderDetail.setStatus(orderInvoice.getStatus());

            completeOrderDetails.add(completeOrderDetail);
        }
        return completeOrderDetails;
    }

    @Override
    public List<OrderInvoiceDTO> readOrderInvoiceByStatusProcess() {
        List<OrderInvoice> orderInvoiceList = orderInvoiceRepository.readOrderInvoiceByOrderStatusProcess();
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
    public List<AssignOrderInvoiceTM> readOrderInvoiceByStatusDelivery() {
        List<CustomEntity7> orderAssigneeDetails = assignOrderInvoiceDetailRepository.getOrderAssigneeDetails();
        List<AssignOrderInvoiceTM> assignOrderInvoiceList= new ArrayList<>();

        for (CustomEntity7 orderInvoice : orderAssigneeDetails) {
            AssignOrderInvoiceTM orders = new AssignOrderInvoiceTM();

            String createdDateTime = orderInvoice.getOrderDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            String assignedDateTime = orderInvoice.getAssignedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            SystemUser systemUser = systemUserRepository.findById(orderInvoice.getAssignee()).get();

            orders.setOrderId(orderInvoice.getOrderId());
            orders.setOrderCreatedDateTime(createdDateTime);
            orders.setAssignedDateTime(assignedDateTime);
            orders.setCustomerId(orderInvoice.getCustomerId());
            orders.setShopId(orderInvoice.getShopId());
            orders.setAssignee(systemUser.getFirstName()+" "+systemUser.getLastName());
            orders.setNetTotal(orderInvoice.getNetTotal());
            orders.setOrderStatus(orderInvoice.getOrderStatus());

            assignOrderInvoiceList.add(orders);
        }
        return assignOrderInvoiceList;
    }

    @Override
    public List<AssignOrderInvoiceDetailTM> readOrderInvoiceByAssignee(String assigneeId) {
        SystemUser assignee = systemUserRepository.findSystemUser(assigneeId);
        List<CustomEntity8> orderAssigneeDetails = assignOrderInvoiceDetailRepository.getOrderAssigneeDetailsByAssignee(assignee.getId());
        List<AssignOrderInvoiceDetailTM> assignOrderInvoiceList= new ArrayList<>();

        for (CustomEntity8 orderInvoice : orderAssigneeDetails) {
            AssignOrderInvoiceDetailTM orders = new AssignOrderInvoiceDetailTM();

            String createdDateTime = orderInvoice.getOrderDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            String assignedDateTime = orderInvoice.getAssignedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            CustomerUser customerUser = customerUserRepository.findById(orderInvoice.getCustomerId()).get();
            Shop shop = shopRepository.findById(orderInvoice.getShopId()).get();

            orders.setOrderId(orderInvoice.getOrderId());
            orders.setOrderCreatedDateTime(createdDateTime);
            orders.setAssignedDateTime(assignedDateTime);
            orders.setCustomerName(customerUser.getCustomerFirstName()+" "+customerUser.getCustomerLastName());
            orders.setShopName(shop.getShopName());
            orders.setNetTotal(orderInvoice.getNetTotal().toString());

            assignOrderInvoiceList.add(orders);
        }
        return assignOrderInvoiceList;
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
    public void updateStatusToProcess(String orderIdArray) {

        String[] orderIds = orderIdArray.split(",");
        for (String orderId : orderIds) {

            Optional<OrderInvoice> optionalOrderInvoice = orderInvoiceRepository.findById(orderId);

            if (optionalOrderInvoice.isPresent()) {
                optionalOrderInvoice.get().setStatus(3);

                orderInvoiceRepository.save(optionalOrderInvoice.get());
            }
        }
    }

    @Override
    public void saveCompleteDeliveryDetails(String orderId) {
        Optional<OrderInvoice> optionalOrderInvoice = orderInvoiceRepository.findById(orderId);
        AssignOrderInvoiceDetail assignInvoiceDetail = assignOrderInvoiceDetailRepository.getAssignInvoiceIdByOrderId(orderId);

        if (optionalOrderInvoice.isPresent()) {
            optionalOrderInvoice.get().setStatus(5);

            completeDeliveryRepository.save(new CompleteDelivery(getCompleteDeliveryId(),assignInvoiceDetail));
            orderInvoiceRepository.save(optionalOrderInvoice.get());

            //send delivery complete email
            DeliveryOrderDTO detail = getDeliveryOrderDetail(orderId);

            CustomerUser customerUser = customerUserRepository.findById(detail.getCustomerId()).get();
            String[] address = detail.getDeliveryAddress().split("\\|");

            String bodyMessage = buildDeliveryCompleteEmail(detail.getCustomerFullName(), orderId, detail.getShopName(), address[0], address[1],
                    address[2], address[3], detail.getDeliveryContact(), detail.getPaymentMethod(), detail.getTotalAmount());

            emailSender.sendEmail(customerUser.getCustomerEmail(), bodyMessage, "Your order #"+orderId+" has been delivered");
        }
    }

    @Override
    public void updateStatusToDelivery(String orderIdArray, String assigneeId) throws IOException {
        String orderIdList = "";
        String message1 = "Please+deliver+below+orders.From+VG+Distributors+";
        Optional<SystemUser> optionalSystemUser = systemUserRepository.findById(assigneeId);

        String[] orderIds = orderIdArray.split(",");
        for (String orderId : orderIds) {

            Optional<OrderInvoice> optionalOrderInvoice = orderInvoiceRepository.findById(orderId);

            if (optionalOrderInvoice.isPresent() && optionalSystemUser.isPresent()) {

                    assignOrderInvoiceDetailRepository.save(new AssignOrderInvoiceDetail(optionalOrderInvoice.get(),
                            optionalSystemUser.get()));

                    optionalOrderInvoice.get().setStatus(4);

                orderInvoiceRepository.save(optionalOrderInvoice.get());

            }
            orderIdList += "#"+orderId+"+";
        }

        String finalMessage = message1+orderIdList;
//        System.out.println(finalMessage);
      /*  TextMsgSender textMsgSender = new TextMsgSender();
        textMsgSender.sendTextMsg(optionalSystemUser.get().getContact(), finalMessage );*/
    }

    @Override
    public boolean IsExistOrderByOrderId(String id) {
        return orderInvoiceRepository.existsById(id);
    }

    @Override
    public int getTotalConfirmOrderCount() {
        return orderInvoiceRepository.getTotalConfirmOrderCount();
    }

    @Override
    public String getOrderIdFromAssignOrder(String assignee, String orderId) {
        SystemUser systemUser = systemUserRepository.findSystemUser(assignee);
        return assignOrderInvoiceDetailRepository.getOrderIdFromAssignOrder(systemUser.getId(), orderId);
    }

    @Override
    public DeliveryOrderDTO getDeliveryOrderDetail(String orderId) {
        List<CustomEntity5> orderInvoice = orderInvoiceRepository.getOrderInvoice(orderId);
        List<DeliveryOrderDTO> deliveryOrderDetails = new ArrayList<>();

        for (CustomEntity5 order : orderInvoice) {
            DeliveryOrderDTO deliveryOrderDTO = new DeliveryOrderDTO();

            deliveryOrderDTO.setCustomerId(order.getCustomerId());

            CustomerUser customerUser = customerUserRepository.findById(order.getCustomerId()).get();
            deliveryOrderDTO.setCustomerFullName(customerUser.getCustomerFirstName()+" "+customerUser.getCustomerLastName());

            deliveryOrderDTO.setCustomerContact(customerUser.getContact());
            deliveryOrderDTO.setOrderId(orderId);
            deliveryOrderDTO.setShopName(order.getShopName());
            deliveryOrderDTO.setDeliveryAddress(order.getAddress1()+"|"+order.getAddress2()+"|"+order.getCity()+"|"+order.getDistrict());
            deliveryOrderDTO.setDeliveryContact(order.getContact());
            deliveryOrderDTO.setPaymentMethod(order.getPaymentMethod());
            deliveryOrderDTO.setTotalAmount(order.getNetTotal().toString());

            deliveryOrderDetails.add(deliveryOrderDTO);
        }
        return deliveryOrderDetails.get(0);
    }

    @Override
    public List<CompleteDeliveryDetailTM2> getCompletedOrderDetailsByAssignee(String assignee) {
        SystemUser systemUser = systemUserRepository.findSystemUser(assignee);
        List<CustomEntity10> completedOrderDetails = completeDeliveryRepository.getAllCompletedDeliveryDetailsByAssignee(systemUser.getId());
        List<CompleteDeliveryDetailTM2> completeDeliveryDetailTM = new ArrayList<>();

        for (CustomEntity10 orderDetail : completedOrderDetails) {
            CompleteDeliveryDetailTM2 completeDeliveryDetailTM2 = new CompleteDeliveryDetailTM2();

            completeDeliveryDetailTM2.setOrderId(orderDetail.getOrderId());
            completeDeliveryDetailTM2.setDeliveryId(orderDetail.getDeliveryId());

            String assignedDateTime = orderDetail.getAssignedDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            String deliveredDateTime = orderDetail.getDeliveredDateTime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"));
            completeDeliveryDetailTM2.setDeliveredDateTime(deliveredDateTime);
            completeDeliveryDetailTM2.setAssignedDateTime(assignedDateTime);

            completeDeliveryDetailTM2.setShop(orderDetail.getShopName());
            completeDeliveryDetailTM2.setNetTotal(orderDetail.getTotalAmount().toString());

            completeDeliveryDetailTM.add(completeDeliveryDetailTM2);
        }

        return completeDeliveryDetailTM;
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

                DeliveryOrderDTO detail = getDeliveryOrderDetail(orderInvoice.getOrderId());
                CustomerUser customerUser = customerUserRepository.findById(detail.getCustomerId()).get();
                String[] address = detail.getDeliveryAddress().split("\\|");

                //Send Confirmation Email
                String bodyMessage = buildConfirmEmail(detail.getCustomerFullName(), orderInvoice.getOrderId(),
                        "", detail.getShopName(), address[0], address[1], address[2], address[3], detail.getDeliveryContact());

                emailSender.sendEmail(customerUser.getCustomerEmail(), bodyMessage, "Your order #"+orderInvoice.getOrderId()+" has been confirmed");
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

    private String getCompleteDeliveryId() {
        String lastCompleteDeliveryId = completeDeliveryRepository.getCompleteDeliveryId();
        String newId = "";

        if (lastCompleteDeliveryId == null) {
            newId = "D01";
        } else {
            String replaceId = lastCompleteDeliveryId.replaceAll("D", "");
            int id = Integer.parseInt(replaceId) + 1;
            if (id < 10) {
                newId = "D0" + id;
            } else {
                newId = "D" + id;
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

    private String buildDeliveryCompleteEmail(String customerName, String orderId, String shopName, String address1,
                                              String address2, String city, String district, String contact,
                                              String paymentMethod, String totalAmount){

        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\"><head><meta charset=\"UTF-8\">" +
                "<meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">" +
                "<meta name=\"x-apple-disable-message-reformatting\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">" +
                "<meta content=\"telephone=no\" name=\"format-detection\">" +
                "<title>Delivery Email</title> <!--[if (mso 16)]><style type=\"text/css\">     a {text-decoration: none;}     </style><![endif]--> <!--[if gte mso 9]>" +
                "<style>sup { font-size: 100% !important; }</style><![endif]--> <!--[if gte mso 9]> <![endif]-->" +
                "<style type=\"text/css\">#outlook a {\tpadding:0;}.es-button {\tmso-style-priority:100!important;\ttext-decoration:none!important;}a[x-apple-data-detectors] {\tcolor:inherit!important;\ttext-decoration:none!important;\tfont-size:inherit!important;\tfont-family:inherit!important;\tfont-weight:inherit!important;\tline-height:inherit!important;}.es-desk-hidden {\tdisplay:none;\tfloat:left;\toverflow:hidden;\twidth:0;\tmax-height:0;\tline-height:0;\tmso-hide:all;}@media only screen and (max-width:600px) {p, ul li, ol li, a { line-height:150%!important } h1 { font-size:30px!important; text-align:center; line-height:120% } h2 { font-size:26px!important; text-align:center; line-height:120% } h3 { font-size:20px!important; text-align:center; line-height:120% } .es-header-body h1 a, .es-content-body h1 a, .es-footer-body h1 a { font-size:30px!important } .es-header-body h2 a, .es-content-body h2 a, .es-footer-body h2 a { font-size:26px!important } .es-header-body h3 a, .es-content-body h3 a, .es-footer-body h3 a { font-size:20px!important } .es-menu td a { font-size:16px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:16px!important } .es-content-body p, .es-content-body ul li, .es-content-body ol li, .es-content-body a { font-size:16px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:16px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button, button.es-button { font-size:20px!important; display:block!important; border-width:10px 0px 10px 0px!important } .es-adaptive table, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden { width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } tr.es-desk-hidden { display:table-row!important } table.es-desk-hidden { display:table!important } td.es-desk-menu-hidden { display:table-cell!important } .es-menu td { width:1%!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } }</style></head>\n" +
                "<body style=\"width:100%;font-family:arial, 'helvetica neue', helvetica, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\"><div class=\"es-wrapper-color\" style=\"background-color:#F6F6F6\"> <!--[if gte mso 9]><v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\"> <v:fill type=\"tile\" color=\"#efefef\"></v:fill> </v:background><![endif]--><table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top\"><tr><td valign=\"top\" style=\"padding:0;Margin:0\"><table cellpadding=\"0\" cellspacing=\"0\" class=\"es-header\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top\"><tr><td align=\"center\" style=\"padding:0;Margin:0\"><table class=\"es-header-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FEF5E4;width:600px\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#fef5e4\" align=\"center\"><tr><td align=\"left\" style=\"padding:0;Margin:0\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;width:600px\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td align=\"center\" style=\"padding:0;Margin:0;font-size:0px\"><img class=\"adapt-img\" src=\"https://nseesc.stripocdn.email/content/guids/79c9fed7-e0fd-46bd-8ba4-d3210668c6c9/images/57411613015986966.png\" alt style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\" width=\"600\"></td>\n" +
                "</tr></table></td></tr></table></td></tr></table></td>\n" +
                "</tr></table><table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"><tr><td align=\"center\" style=\"padding:0;Margin:0\"><table class=\"es-content-body\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\"><tr><td align=\"left\" style=\"Margin:0;padding-top:10px;padding-bottom:10px;padding-left:20px;padding-right:20px\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:560px\"><table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;border-radius:0px\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\"><tr><td align=\"center\" style=\"padding:0;Margin:0;padding-top:15px;padding-bottom:15px\"><h1 style=\"Margin:0;line-height:12px;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;font-size:10px;font-style:normal;font-weight:normal;color:#DAA520\"><br></h1>\n" +
                "<h1 style=\"Margin:0;line-height:36px;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;font-size:30px;font-style:normal;font-weight:normal;color:#DAA520\"><strong>Your order has been delivered!</strong></h1></td></tr><tr><td align=\"left\" style=\"Margin:0;padding-top:5px;padding-bottom:5px;padding-left:15px;padding-right:40px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333;font-size:14px\">Hi <strong>"+customerName+"</strong>,<br><br></p>\n" +
                "<p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333;font-size:14px\">We are pleased to inform that your order&nbsp;<b>#"+orderId+"</b>&nbsp;has been delivered.<span style=\"font-size:8px\"><br></span><span style=\"font-size:9px\"></span><br>If you have any questions, please contact us at <a target=\"_blank\" href=\"mailto:vgdistributors@gmail.com\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#2CB543;font-size:14px\">vgdisrtributors@gmail.com</a><br><br></p></td></tr></table></td></tr></table></td></tr></table></td>\n" +
                "</tr></table><table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"><tr><td align=\"center\" style=\"padding:0;Margin:0\"><table class=\"es-content-body\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\"><tr><td align=\"left\" style=\"padding:0;Margin:0\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td align=\"left\" style=\"padding:0;Margin:0;width:600px\"><table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;background-color:#FEF9EF;border-width:1px;border-style:solid;border-color:#EFEFEF\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#fef9ef\" role=\"presentation\"><tr><td align=\"left\" style=\"padding:0;Margin:0;padding-bottom:10px;padding-top:20px;padding-right:20px\"><h4 style=\"Margin:0;line-height:120%;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;DELIVERY DETAILS:</h4>\n" +
                "</td></tr><tr><td align=\"left\" style=\"padding:0;Margin:0;padding-bottom:20px;padding-right:20px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333;font-size:14px\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <strong>Shop Name</strong> :&nbsp; "+shopName+"<br>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <strong>Address</strong>&nbsp; &nbsp; &nbsp; &nbsp;: "+address1+"</p>\n" +
                "<p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333;font-size:14px\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+address2+"<br>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+city+"<br>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+district+"<br>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <strong>Contact</strong>&nbsp; &nbsp; &nbsp; &nbsp; :&nbsp;"+contact+"</p></td></tr></table></td>\n" +
                "</tr><tr><td align=\"left\" style=\"padding:0;Margin:0;width:600px\"><table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;background-color:#FEF9EF;border-width:1px;border-style:solid;border-color:#EFEFEF\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#fef9ef\" role=\"presentation\"><tr><td align=\"left\" style=\"padding:0;Margin:0;padding-bottom:10px;padding-top:20px;padding-right:20px\"><h4 style=\"Margin:0;line-height:120%;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;PAYMENT DETAILS:</h4></td>\n" +
                "</tr><tr><td align=\"left\" style=\"padding:0;Margin:0;padding-bottom:20px;padding-right:20px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333;font-size:14px\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <strong>Paid Via&nbsp; &nbsp; &nbsp;&nbsp;</strong> :&nbsp; "+paymentMethod+"<br>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <b>Paid</b>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; : Rs."+totalAmount+"</p></td></tr><tr><td align=\"center\" style=\"padding:20px;Margin:0;font-size:0\"><table border=\"0\" width=\"100%\" height=\"100%\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td style=\"padding:0;Margin:0;border-bottom:1px solid #CCCCCC;background:none;height:1px;width:100%;margin:0px\"></td></tr></table></td></tr></table></td>\n" +
                "</tr></table></td></tr></table></td>\n" +
                "</tr></table><table cellpadding=\"0\" cellspacing=\"0\" class=\"es-footer\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top\"><tr><td align=\"center\" style=\"padding:0;Margin:0\"><table class=\"es-footer-body\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\"><tr><td align=\"left\" style=\"Margin:0;padding-top:20px;padding-bottom:20px;padding-left:20px;padding-right:20px\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td align=\"left\" style=\"padding:0;Margin:0;width:560px\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td class=\"es-m-txt-c\" align=\"center\" style=\"padding:0;Margin:0;font-size:0px\"><table class=\"es-table-not-adapt es-social\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px\"><img title=\"Facebook\" src=\"https://nseesc.stripocdn.email/content/assets/img/social-icons/circle-black-bordered/facebook-circle-black-bordered.png\" alt=\"Fb\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td>\n" +
                "<td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px\"><img title=\"Twitter\" src=\"https://nseesc.stripocdn.email/content/assets/img/social-icons/circle-black-bordered/twitter-circle-black-bordered.png\" alt=\"Tw\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0\"><img title=\"Whatsapp\" src=\"https://nseesc.stripocdn.email/content/assets/img/messenger-icons/circle-black-bordered/whatsapp-circle-black-bordered.png\" alt=\"Whatsapp\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td></tr></table></td></tr></table></td></tr></table></td></tr></table></td></tr></table></td></tr></table></div></body></html>\n";
    }

    private String buildConfirmEmail(String customerName, String orderId, String orderDateTime,String shopName, String address1,
                                     String address2, String city, String district, String contact){

        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\"><head><meta charset=\"UTF-8\"><meta content=\"width=device-width, initial-scale=1\" name=\"viewport\"><meta name=\"x-apple-disable-message-reformatting\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta content=\"telephone=no\" name=\"format-detection\"><title>Confirm email</title> <!--[if (mso 16)]><style type=\"text/css\">     a {text-decoration: none;}     </style><![endif]--> <!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]--> <!--[if gte mso 9]> <![endif]--><style type=\"text/css\">#outlook a {\tpadding:0;}.es-button {\tmso-style-priority:100!important;\ttext-decoration:none!important;}a[x-apple-data-detectors] {\tcolor:inherit!important;\ttext-decoration:none!important;\tfont-size:inherit!important;\tfont-family:inherit!important;\tfont-weight:inherit!important;\tline-height:inherit!important;}.es-desk-hidden {\tdisplay:none;\tfloat:left;\toverflow:hidden;\twidth:0;\tmax-height:0;\tline-height:0;\tmso-hide:all;}.es-button-border:hover a.es-button, .es-button-border:hover button.es-button {\tbackground:#56d66b!important;\tborder-color:#56d66b!important;}.es-button-border:hover {\tbackground:#56d66b!important;\tborder-color:#42d159 #42d159 #42d159 #42d159!important;}td .es-button-border:hover a.es-button-1 {\tbackground:#daa525!important;\tborder-color:#daa525!important;}td .es-button-border-2:hover {\tbackground:#daa525!important;\tborder-color:#daa520 #daa520 #daa520 #daa520!important;\tborder-style:solid solid solid solid!important;}@media only screen and (max-width:600px) {p, ul li, ol li, a { line-height:150%!important } h1 { font-size:30px!important; text-align:center; line-height:120% } h2 { font-size:26px!important; text-align:center; line-height:120% } h3 { font-size:20px!important; text-align:center; line-height:120% } .es-header-body h1 a, .es-content-body h1 a, .es-footer-body h1 a { font-size:30px!important } .es-header-body h2 a, .es-content-body h2 a, .es-footer-body h2 a { font-size:26px!important } .es-header-body h3 a, .es-content-body h3 a, .es-footer-body h3 a { font-size:20px!important } .es-menu td a { font-size:16px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:16px!important } .es-content-body p, .es-content-body ul li, .es-content-body ol li, .es-content-body a { font-size:16px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:16px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button, button.es-button { font-size:20px!important; display:block!important; border-left-width:0px!important; border-right-width:0px!important } .es-adaptive table, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden { width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } tr.es-desk-hidden { display:table-row!important } table.es-desk-hidden { display:table!important } td.es-desk-menu-hidden { display:table-cell!important } .es-menu td { width:1%!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } }</style></head>\n" +
                "<body style=\"width:100%;font-family:arial, 'helvetica neue', helvetica, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\"><div class=\"es-wrapper-color\" style=\"background-color:#F6F6F6\"> <!--[if gte mso 9]><v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\"> <v:fill type=\"tile\" color=\"#f6f6f6\"></v:fill> </v:background><![endif]--><table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top\"><tr><td valign=\"top\" style=\"padding:0;Margin:0\"><table cellpadding=\"0\" cellspacing=\"0\" class=\"es-header\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top\"><tr><td align=\"center\" style=\"padding:0;Margin:0\"><table class=\"es-header-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FEF5E4;width:600px\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#fef5e4\" align=\"center\"><tr><td align=\"left\" style=\"padding:0;Margin:0\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td align=\"center\" valign=\"top\" style=\"padding:0;Margin:0;width:600px\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td align=\"center\" style=\"padding:0;Margin:0;font-size:0px\"><img class=\"adapt-img\" src=\"https://nseesc.stripocdn.email/content/guids/79c9fed7-e0fd-46bd-8ba4-d3210668c6c9/images/57411613015986966.png\" alt style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\" width=\"600\"></td>\n" +
                "</tr></table></td></tr></table></td></tr></table></td>\n" +
                "</tr></table><table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"><tr><td align=\"center\" style=\"padding:0;Margin:0\"><table class=\"es-content-body\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\"><tr><td align=\"left\" style=\"Margin:0;padding-top:15px;padding-bottom:20px;padding-left:20px;padding-right:20px\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:560px\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td class=\"es-infoblock\" align=\"center\" style=\"padding:0;Margin:0;line-height:14px;font-size:12px;color:#CCCCCC\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:36px;color:#DAA520;font-size:30px\"><strong>Your order&nbsp;</strong><b>has been confirmed</b><strong>!</strong></p>\n" +
                "</td></tr></table></td></tr></table></td></tr></table></td>\n" +
                "</tr></table><table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"><tr><td align=\"center\" style=\"padding:0;Margin:0\"><table class=\"es-content-body\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\"><tr><td align=\"left\" style=\"Margin:0;padding-top:15px;padding-bottom:20px;padding-left:20px;padding-right:20px\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:560px\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td class=\"es-infoblock\" align=\"left\" style=\"padding:0;Margin:0;line-height:14px;font-size:12px;color:#CCCCCC\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:14px;color:#CCCCCC;font-size:12px\"><a target=\"_blank\" href=\"\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#CCCCCC;font-size:12px\"></a><span style=\"color:#000000;font-size:14px\">Hi</span>&nbsp;&nbsp;<span style=\"font-size:14px;color:#000000\"><strong>"+customerName+"</strong>,<a target=\"_blank\" href=\"\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#CCCCCC;font-size:12px\"></a></span></p>\n" +
                "</td></tr><tr><td align=\"center\" style=\"padding:0;Margin:0;font-size:0px\"><img class=\"adapt-img\" src=\"https://nseesc.stripocdn.email/content/guids/CABINET_17e45dd513cb82f2e69c6779a9d7b46d/images/89121614940658400.png\" alt style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\" width=\"560\"></td></tr></table></td></tr></table></td></tr></table></td>\n" +
                "</tr></table><table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"><tr><td align=\"center\" style=\"padding:0;Margin:0\"><table class=\"es-content-body\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\"><tr><td align=\"left\" style=\"Margin:0;padding-top:10px;padding-bottom:10px;padding-left:20px;padding-right:20px\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:560px\"><table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;border-radius:0px\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\"><tr><td align=\"left\" style=\"Margin:0;padding-top:5px;padding-bottom:5px;padding-left:15px;padding-right:40px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333;font-size:14px\">We are pleased to inform that your order&nbsp;<b>#"+orderId+"</b>&nbsp;has been confirmed.<br><br>If you have any questions, please contact us at <a target=\"_blank\" href=\"mailto:vgdistributors@gmail.com\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:underline;color:#2CB543;font-size:14px\">vgdisrtributors@gmail.com</a><br><br></p>\n" +
                "</td></tr><tr><td align=\"center\" style=\"padding:0;Margin:0\"><span class=\"es-button-border es-button-border-2\" style=\"border-style:solid;border-color:#DAA520;background:#DAA520;border-width:0px 0px 2px 0px;display:inline-block;border-radius:0px;width:auto;border-bottom-width:0px\"><a href=\"http://localhost:63342/BIT-frontend/template/customer/index.html\" class=\"es-button es-button-1\" target=\"_blank\" style=\"mso-style-priority:100 !important;text-decoration:none;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;color:#FFFFFF;font-size:18px;border-style:solid;border-color:#DAA520;border-width:10px 20px 10px 20px;display:inline-block;background:#DAA520;border-radius:0px;font-family:arial, 'helvetica neue', helvetica, sans-serif;font-weight:normal;font-style:normal;line-height:22px;width:auto;text-align:center\">MANAGE YOUR ORDER</a></span></td></tr></table></td></tr></table></td></tr></table></td>\n" +
                "</tr></table><table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"><tr><td align=\"center\" style=\"padding:0;Margin:0\"><table class=\"es-content-body\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\"><tr><td align=\"left\" style=\"padding:0;Margin:0\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td align=\"left\" style=\"padding:0;Margin:0;width:600px\"><table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;background-color:#FEF9EF;border-width:1px;border-style:solid;border-color:#EFEFEF\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#fef9ef\" role=\"presentation\"><tr><td align=\"left\" style=\"padding:0;Margin:0;padding-top:15px;padding-bottom:15px\"><h4 style=\"Margin:0;line-height:120%;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;ORDER DETAILS:</h4>\n" +
                "</td>\n" +
                "</tr><tr><td align=\"left\" style=\"padding:0;Margin:0;padding-bottom:10px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333;font-size:14px\"><strong>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Order Date & Time</strong>&nbsp;:&nbsp; "+orderDateTime+"&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;<br><strong>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Shop Name</strong>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;:&nbsp; "+shopName+"&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;<br>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <strong>Address</strong>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;:&nbsp;"+address1+"&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</p>\n" +
                "<p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#333333;font-size:14px\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+address2+"&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;<br>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+city+"&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;<br>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+district+"&nbsp;<br>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <strong>Contact</strong>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; :&nbsp;"+contact+"</p>\n" +
                "</td></tr></table></td></tr></table></td></tr></table></td>\n" +
                "</tr></table><table cellpadding=\"0\" cellspacing=\"0\" class=\"es-footer\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:transparent;background-repeat:repeat;background-position:center top\"><tr><td align=\"center\" style=\"padding:0;Margin:0\"><table class=\"es-footer-body\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\"><tr><td align=\"left\" style=\"Margin:0;padding-top:20px;padding-bottom:20px;padding-left:20px;padding-right:20px\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td align=\"left\" style=\"padding:0;Margin:0;width:560px\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td class=\"es-m-txt-c\" align=\"center\" style=\"padding:0;Margin:0;font-size:0px\"><table class=\"es-table-not-adapt es-social\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"><tr><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px\"><img title=\"Facebook\" src=\"https://nseesc.stripocdn.email/content/assets/img/social-icons/circle-black-bordered/facebook-circle-black-bordered.png\" alt=\"Fb\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td>\n" +
                "<td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;padding-right:10px\"><img title=\"Twitter\" src=\"https://nseesc.stripocdn.email/content/assets/img/social-icons/circle-black-bordered/twitter-circle-black-bordered.png\" alt=\"Tw\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td><td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0\"><img title=\"Whatsapp\" src=\"https://nseesc.stripocdn.email/content/assets/img/messenger-icons/circle-black-bordered/whatsapp-circle-black-bordered.png\" alt=\"Whatsapp\" width=\"32\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\"></td></tr></table></td></tr></table></td></tr></table></td></tr></table></td></tr></table></td></tr></table></div></body></html>\n";
    }
}
