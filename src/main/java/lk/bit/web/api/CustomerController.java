package lk.bit.web.api;

import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    private List<CustomerDTO> readAllCustomerDetail() {

        return customerBO.readAllCustomerDetails();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/registered")
    private int getTotalCountOfRegistered() {

        return customerBO.getTotalCountOfRegistered();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/active")
    private int getTotalCountOfActive() {

        return customerBO.getTotalCountOfActive();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/deactivate")
    private int getTotalCountOfDeactivate() {

        return customerBO.getTotalCountOfDeactivate();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/verified")
    private int getTotalCountOfVerified() {

        return customerBO.getTotalCountOfVerified();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/unVerified")
    private int getTotalCountOfUnVerified() {

        return customerBO.getTotalCountOfUnVerified();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/status")
    private List<CustomerDTO> readCustomerDetailsByStatus(@RequestParam int status) {
        if (status < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return customerBO.getCustomerDetailsByStatus(status);
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping
    private void updateAccountStatus(@RequestParam("id") String customerId, @RequestParam int status) {
        CustomerDTO customer = customerBO.findCustomerById(customerId);
        if (customer == null || status < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        customerBO.updateCustomerStatus(customerId,status);
    }
}
