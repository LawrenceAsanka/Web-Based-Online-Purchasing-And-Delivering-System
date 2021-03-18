package lk.bit.web.api;

import lk.bit.web.business.custom.OrderInvoiceBO;
import lk.bit.web.business.custom.ReturnBO;
import lk.bit.web.dto.ReturnDTO;
import lk.bit.web.util.tm.ReturnInvoiceTM;
import lk.bit.web.util.tm.ReturnTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/returns")
public class ReturnController {
    @Autowired
    private OrderInvoiceBO orderInvoiceBO;
    @Autowired
    private ReturnBO returnBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    private List<ReturnTM> readAllByStatus() {
        return returnBO.readAllByStatus();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cancel")
    private List<ReturnTM> readAllByStatusCancel() {
        return returnBO.readAllByStatusCancel();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/confirm")
    private List<ReturnTM> readAllByStatusConfirm() {
        return returnBO.readAllByStatusConfirm();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{returnId}")
    private List<ReturnInvoiceTM> readAllReturnDetails(@PathVariable String returnId) {
        return returnBO.readAllReturnDetailsByReturnId(returnId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void saveReturn(@RequestBody ReturnDTO returnDTO) {
        if (!orderInvoiceBO.IsExistOrderByOrderId(returnDTO.getOrderId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        returnBO.saveReturnDetail(returnDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{returnId}/cancel")
    private void updateStatusCancel(@PathVariable("returnId") String returnId) {
        System.out.println(returnId);
        if (!returnBO.IsReturnExist(returnId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        returnBO.updateStatusToCancel(returnId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{returnId}/confirm")
    private void updateStatusConfirm(@PathVariable String returnId) {
        if (!returnBO.IsReturnExist(returnId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        returnBO.updateStatusToConfirm(returnId);
    }
}