package lk.bit.web.api;

import lk.bit.web.business.custom.CreditorBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/creditors")
public class CreditorController {

    @Autowired
    private CreditorBO creditorBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/status")
    private int isSettleByCustomer(@RequestParam("email") String customerEmail){
        if (customerEmail == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return creditorBO.getCountOfNotSettleCreditByCustomer(customerEmail);
    }
}
