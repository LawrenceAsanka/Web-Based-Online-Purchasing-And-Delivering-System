package lk.bit.web.api;

import lk.bit.web.business.custom.CreditorBO;
import lk.bit.web.dto.CreditDetailDTO;
import lk.bit.web.util.tm.CreditCollectionTM;
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
    @GetMapping("/assignee")
    private List<CreditCollectionTM> readAllCreditDetailsByAssignee(@RequestParam("userName") String assignee){
        if (assignee == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return creditorBO.readAllCreditDetailsByAssignee(assignee);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/assign")
    private void saveCreditAssign(@RequestParam("creditIdArray") String creditArray,
                                  @RequestParam("assignedTo") String assignTo){
        if (creditArray == null && assignTo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        creditorBO.saveCreditAssign(creditArray, assignTo);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/extendDate")
    private void saveCreditAssign(@RequestParam("creditId") String creditId,
                                  @RequestParam("extendDate") String extendDate,
                                  @RequestParam("amount") String newAmount){
        if (creditId == null && extendDate == null && newAmount ==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        creditorBO.updateCreditAmountAndDate(creditId, extendDate, newAmount);
    }
}
