package lk.bit.web.api;

import lk.bit.web.business.custom.OrderInvoiceBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
