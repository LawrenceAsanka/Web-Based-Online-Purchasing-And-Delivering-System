package lk.bit.web.api;

import lk.bit.web.business.custom.ConfirmationTokenBO;
import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/registers")
public class CustomerRegisterController {

    @Autowired
    private CustomerBO customerBO;
    @Autowired
    private ConfirmationTokenBO confirmationTokenBO;

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){
        return confirmationTokenBO.confirmToken(token);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/signup",consumes = MediaType.APPLICATION_JSON_VALUE)
    public String saveCustomer(@RequestBody CustomerDTO signUpDetails){
        if (signUpDetails.getFirstName().isEmpty() || signUpDetails.getEmail().isEmpty() ||
                signUpDetails.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return customerBO.saveCustomer(signUpDetails);
    }
}
