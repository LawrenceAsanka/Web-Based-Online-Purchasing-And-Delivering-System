package lk.bit.web.api;

import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.dto.SignUpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerBO customerBO;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void saveCustomer(@RequestBody SignUpDTO signUpDetails){
        if (signUpDetails.getFirstName().isEmpty() || signUpDetails.getEmail().isEmpty() ||
        signUpDetails.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        customerBO.saveCustomer(signUpDetails);
    }
}
