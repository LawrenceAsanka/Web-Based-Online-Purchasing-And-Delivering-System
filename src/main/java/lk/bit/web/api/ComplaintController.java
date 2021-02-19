package lk.bit.web.api;

import lk.bit.web.business.custom.ComplaintBO;
import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.dto.ComplainDTO;
import lk.bit.web.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/complaints")
public class ComplaintController {
    @Autowired
    private ComplaintBO complaintBO;
    @Autowired
    private CustomerBO customerBO;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    private void save(@RequestBody ComplainDTO complainDTO){
        if (complaintBO.IsComplaintExist(complainDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        CustomerDTO customerDTO = customerBO.findCustomerByEmail(complainDTO.getCreatedBy());
        complainDTO.setCreatedBy(customerDTO.getId());

        complaintBO.save(complainDTO);
    }
}
