package lk.bit.web.api;

import lk.bit.web.business.custom.CreditorBO;
import lk.bit.web.dto.CreditDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/creditors")
public class CreditorController {

    @Autowired
    private CreditorBO creditorBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    private List<CreditDetailDTO> readAllCreditDetailsByDateAndStatus(){
        return creditorBO.readAllCreditorDetailsByDateAndStatus();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    private List<CreditDetailDTO> readAllCreditDetails(){
        return creditorBO.readAllCreditorDetails();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filter")
    private List<CreditDetailDTO> readAllCreditDetailsByFilter(@RequestParam("q") int filterStatus ){
        return creditorBO.readAllCreditDetailsByFilter(filterStatus);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/status")
    private int isSettleByCustomer(@RequestParam("email") String customerEmail){
        if (customerEmail == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return creditorBO.getCountOfNotSettleCreditByCustomer(customerEmail);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/assign")
    private void saveCreditAssign(@RequestParam("creditIdArray") String creditArray,
                                  @RequestParam("assignedTo") String assignTo){
        if (creditArray == null && assignTo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        creditorBO.saveCreditAssign(creditArray, assignTo);
    }
}
