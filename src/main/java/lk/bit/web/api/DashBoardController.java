package lk.bit.web.api;

import lk.bit.web.business.custom.CreditorBO;
import lk.bit.web.business.custom.OrderInvoiceBO;
import lk.bit.web.business.custom.ReturnBO;
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
    @Autowired
    private ReturnBO returnBO;
    @Autowired
    private CreditorBO creditorBO;

    @GetMapping("/totalOrdersCount")
    private int readAllTotalOrdersCount() {
        return orderInvoiceBO.readAllTodayOrderCount();
    }

    @GetMapping("/completeOrdersCount")
    private int readAllTotalCompleteOrdersCount() {
        return orderInvoiceBO.readAllTodayDeliveryCount();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/totalReturnsCount")
    private int readAllTotalReturnsCount() {
        return returnBO.readAllTodayReturnCount();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/completeReturnsCount")
    private int readAllTotalCompleteReturnsCount() {
        return returnBO.readATotalCompleteReturnsCount();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/totalCreditsCount")
    private int readAllTotalCreditsCount() {
        return creditorBO.readTodayCreditsCount();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/completeCreditsCount")
    private int readAllCompleteCreditsCount() {
        return creditorBO.readTodayCompleteCreditsCount();
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/netTotalAmount")
    private String readTotalNetAmountByDate() {
        return orderInvoiceBO.readTotalNetAmountByDate().toString();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/codTotalAmount")
    private String readTotalCODAmountByDate() {
        return orderInvoiceBO.readTotalCODAmountByDate().toString();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/creditTotalAmount")
    private String readTotalCreditAmountByDate() {
        return orderInvoiceBO.readTotalCreditAmountByDate().toString();
    }

}
