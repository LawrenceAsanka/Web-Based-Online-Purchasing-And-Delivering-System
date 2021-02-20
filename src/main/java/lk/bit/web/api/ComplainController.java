package lk.bit.web.api;

import lk.bit.web.business.custom.ComplainBO;
import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.dto.ComplainDTO;
import lk.bit.web.dto.CustomerDTO;
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
    @GetMapping("/{status}")
    private List<ComplainDTO> getComplaintDetailsByStatus(@PathVariable int status){
        System.out.println(status);
        return complainBO.getComplaintDetailsByStatus(status);
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
}
