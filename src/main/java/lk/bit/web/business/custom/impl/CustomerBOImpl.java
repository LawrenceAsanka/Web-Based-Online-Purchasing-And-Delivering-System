package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.dto.SignUpDTO;
import lk.bit.web.entity.Customer;
import lk.bit.web.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomerBOImpl implements CustomerBO {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Environment env;

    @Override
    public void saveCustomer(SignUpDTO signUpDetails) {
        //create a customer
        customerRepository.save(new Customer(signUpDetails.getFirstName(), signUpDetails.getLastName(),
                signUpDetails.getEmail(), passwordEncoder.encode(signUpDetails.getPassword()), "", "ACTIVE"));

        // send an email to the created customer
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(env.getRequiredProperty("spring.mail.username"));
        mail.setTo(signUpDetails.getEmail());
        mail.setSubject("Welcome to the VG Distributors e shopping");
        mail.setText("Thanks for registering with us!.Use your email address as your user name when logging.");

        javaMailSender.send(mail);
    }

}
