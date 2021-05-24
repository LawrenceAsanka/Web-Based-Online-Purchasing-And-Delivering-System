package lk.bit.web.api;

import lk.bit.web.business.custom.CreditorBO;
import lk.bit.web.dto.CreditDetailDTO;
import lk.bit.web.util.tm.CompleteCreditCollectionTM;
import lk.bit.web.util.tm.CreditCollectionTM;
import lk.bit.web.util.tm.ReturnTM;
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

    //Today Collections
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/complete")
    private List<CompleteCreditCollectionTM> readAllCompleteCreditCollectionDetails(){

        return creditorBO.readAllCompleteCreditCollectionDetails();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/completeCredits")
    private List<CompleteCreditCollectionTM> readAllCompleteCreditCollectionDetailsByAssignee(@RequestParam("userName") String assignee){

        return creditorBO.readAllCompleteCreditCollectionDetailsByAssignee(assignee);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filterByDates")
    private List<CreditDetailDTO> readCreditorDetailsByDateRange(@RequestParam("q") int dateType, @RequestParam("startDate") String startDate,
                                                        @RequestParam("endDate") String endDate){
        if (startDate == null && endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return creditorBO.readCreditorDetailsByDateRange(dateType, startDate, endDate);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filterBy")
    private List<CompleteCreditCollectionTM> readCompleteCreditorDetailsByDateRange(@RequestParam("q") int dateType, @RequestParam("startDate") String startDate,
                                                                 @RequestParam("endDate") String endDate){
                System.out.println(dateType);
        if (startDate == null && endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return creditorBO.readCompleteCreditorDetailsByDateRange(dateType, startDate, endDate);
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

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/complete")
    private void updateIsSettleStatus(@RequestParam("id") String creditId){
        if (creditId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        creditorBO.updateIsSettleStatus(creditId);
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
