package lk.bit.web.api;

import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    @Autowired
    private CustomerBO customerBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    private CustomerDTO readCustomerDetailByEmail(@RequestParam String email) {
        CustomerDTO customer = customerBO.findCustomerByEmail(email);

        if (email == null || customer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return customer;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    private CustomerDTO readCustomerDetailById(@PathVariable String id) {
        CustomerDTO customer = customerBO.findCustomerById(id);

        if (id == null || customer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return customer;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{customerId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private void updateCustomer(@PathVariable String customerId, @RequestPart(value = "userImage",required = false) MultipartFile userImage,
                                @RequestParam("data") String userData, @RequestParam("userEmail") String userEmail) {
        CustomerDTO customer = customerBO.findCustomerById(customerId);

        if (!customer.getEmail().equals(userEmail)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        customerBO.updateCustomer(userImage, userData, customerId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/resetPassword")
    private void updatePassword(@RequestParam("id") String customerId, @RequestParam String password) {
        CustomerDTO customer = customerBO.findCustomerById(customerId);
        System.out.println(password);
        if (customer == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        customerBO.updatePassword(customerId,password);
    }
}
