package lk.bit.web.api;

import lk.bit.web.business.custom.OrderInvoiceBO;
import lk.bit.web.business.custom.ReturnBO;
import lk.bit.web.dto.ReturnDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/returns")
public class ReturnController {
    @Autowired
    private OrderInvoiceBO orderInvoiceBO;
    @Autowired
    private ReturnBO returnBO;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void saveReturn(@RequestBody ReturnDTO returnDTO) {
        if (!orderInvoiceBO.IsExistOrderByOrderId(returnDTO.getOrderId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        returnBO.saveReturnDetail(returnDTO);
    }
}
