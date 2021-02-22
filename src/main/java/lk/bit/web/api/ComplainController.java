package lk.bit.web.api;

import lk.bit.web.business.custom.ComplainBO;
import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.dto.ComplainDTO;
import lk.bit.web.dto.CustomerDTO;
import lk.bit.web.util.SolutionTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/complains")
public class ComplainController {
    @Autowired
    private ComplainBO complainBO;
    @Autowired
    private CustomerBO customerBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    private List<ComplainDTO> getComplaintDetailsByAdmin(){
        return complainBO.getComplaintDetailsByAdmin();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    private List<SolutionTM> getComplaintDetailsAndSolutionDetails(){
        return complainBO.getAllComplainAndComplainSolutionDetails();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/complain/{id}")
    private List<SolutionTM> getComplaintDetailsAndSolutionDetailsById(@PathVariable("id") String complainId){
        return complainBO.getAllComplainAndComplainSolutionDetailsById(complainId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{status}")
    private List<ComplainDTO> getComplaintDetailsByStatus(@PathVariable int status){
//        System.out.println(status);
        return complainBO.getComplaintDetailsByStatus(status);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/customer")
    private List<ComplainDTO> getComplaintDetailsByCustomerId(@RequestParam("id") String customerId){

        if (customerBO.findCustomerById(customerId) == null  || customerId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return complainBO.getComplaintDetailsByCustomerId(customerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/count")
    private int getTotalComplainCount(){
        return complainBO.getTotalComplaintCount();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    private void save(@RequestBody ComplainDTO complainDTO){
        if (complainBO.IsComplaintExist(complainDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        CustomerDTO customerDTO = customerBO.findCustomerByEmail(complainDTO.getCreatedBy());
        complainDTO.setCreatedBy(customerDTO.getId());

        complainBO.save(complainDTO);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}")
    private void updateDeletedByCustomer(@PathVariable String id){
        System.out.println("id -"+id);
        if (!complainBO.IsComplaintExist(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        complainBO.updateIsDeletedByCustomer(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    private void updateIsDeleted(@PathVariable("id") String complainId){
        if (!complainBO.IsComplaintExist(complainId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        complainBO.updateIsDeleted(complainId);
    }
}
