package lk.bit.web.api;

import lk.bit.web.business.custom.OrderInvoiceBO;
import lk.bit.web.util.tm.SalesGraphTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/dashboards")
public class DashBoardController {
    @Autowired
    private OrderInvoiceBO orderInvoiceBO;

    @GetMapping("/todayOrderCount")
    private int readAllTodayOrderCount() {
        return orderInvoiceBO.readAllTodayOrderCount();
    }

    @GetMapping("/todayDeliverCount")
    private int readAllTodayDeliverCount() {
        return orderInvoiceBO.readAllTodayDeliveryCount();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/netTotals")
    private List<SalesGraphTM> getNetTotalByMonth() {
        return orderInvoiceBO.readNetTotalByMonth();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/newOrdersCount")
    private int readNewOrdersCount() {
        return orderInvoiceBO.getTotalNewOrderCount();
    }
}
