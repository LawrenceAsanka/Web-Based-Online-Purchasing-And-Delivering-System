package lk.bit.web.api;

import lk.bit.web.business.custom.OrderInvoiceBO;
import lk.bit.web.business.custom.ReturnBO;
import lk.bit.web.dto.ReturnDTO;
import lk.bit.web.util.tm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
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
    @GetMapping("/{returnId}")
    private List<ReturnInvoiceTM> readAllReturnDetails(@PathVariable String returnId) {
        return returnBO.readAllReturnDetailsByReturnId(returnId);
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
    @GetMapping("/assignReturns")
    private List<AssignReturnTM> readAllAssignReturnsByStatusProcessing() {
        return returnBO.readAssignReturnDetailByStatusProcessing();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/complete")
    private List<AssignReturnTM> readAllAssignReturnsByStatusComplete() {
        return returnBO.readAssignReturnDetailByStatusComplete();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/assignee")
    private List<DeliveryReturnTM> readAllAssignReturnsDetailsByAssignee(@RequestParam String userName) {
        if (userName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return returnBO.readAllAssignReturnDetailByAssignee(userName);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/completeReturns")
    private List<CompleteReturnTM> readAllCompletedReturnsDetailsByAssignee(@RequestParam String userName) {
        if (userName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return returnBO.readAllCompleteReturnDetailsByAssignee(userName);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/customers")
    private List<ReturnDTO> readAllAssignReturnsDetailsByCustomer(@RequestParam String email) {
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return returnBO.readAllAssignReturnsDetailsByCustomer(email);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void saveReturn(@RequestBody ReturnDTO returnDTO) {
        if (!orderInvoiceBO.IsExistOrderByOrderId(returnDTO.getOrderId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        returnBO.saveReturnDetail(returnDTO);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/assigns")
    private void updateOrderStatusToDelivery(@RequestParam String returnIdArray,
                                             @RequestParam String assignedTo) throws IOException {
        if (returnIdArray == null || assignedTo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        returnBO.saveAssignReturnAndUpdateStatus(returnIdArray, assignedTo);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/completeReturn")
    private void saveCompleteReturnDetails(@RequestParam("id") String returnId){
        if (returnId == null || !returnBO.IsReturnExist(returnId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        returnBO.saveCompleteReturnDetails(returnId);
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
