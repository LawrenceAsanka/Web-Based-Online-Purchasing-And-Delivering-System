package lk.bit.web.business.custom.impl;

import com.google.gson.Gson;
import lk.bit.web.business.custom.ConfirmationTokenBO;
import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.dto.ConfirmationTokenDTO;
import lk.bit.web.dto.CustomerDTO;
import lk.bit.web.entity.CustomerUser;
import lk.bit.web.entity.Role;
import lk.bit.web.repository.CustomerUserRepository;
import lk.bit.web.util.email.EmailSender;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Transactional
public class CustomerBOImpl implements CustomerBO {

    @Autowired
    private CustomerUserRepository customerUserRepository;
    @Autowired
    private ConfirmationTokenBO confirmationTokenBO;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    @Lazy
    private ModelMapper mapper;
    @Autowired
    private Environment env;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> roles = null;
        CustomerUser customerUser = customerUserRepository.getCustomerByCustomerEmail(email);
        if (customerUser != null) {
            roles = Collections.singletonList(new SimpleGrantedAuthority(customerUser.getRole().name()));
            return new User(customerUser.getCustomerEmail(), customerUser.getPassword(), roles);
        }
        throw new UsernameNotFoundException(String.format("user with email %s not found", email));
    }

    @Override
    public String saveCustomer(CustomerDTO signUpDetails) {

        CustomerUser customer = customerUserRepository.getCustomerByCustomerEmail(signUpDetails.getEmail());
        String customerID = getCustomerId();

        if (customer != null) {
            throw new IllegalStateException(String.format("email %s was already taken", signUpDetails.getEmail()));
        }
        //create a customer
        customerUserRepository.save(new CustomerUser(customerID, signUpDetails.getFirstName(),
                signUpDetails.getLastName(), signUpDetails.getEmail(),
                passwordEncoder.encode(signUpDetails.getPassword()), signUpDetails.getContact(), Role.ROLE_CUSTOMER));

        // process of creating confirmation email
        String token = UUID.randomUUID().toString();
        String savedToken = confirmationTokenBO.saveConfirmationToken(new ConfirmationTokenDTO(
                        token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15)),
                new CustomerUser(customerID, signUpDetails.getFirstName(),
                        signUpDetails.getLastName(), signUpDetails.getEmail(),
                        passwordEncoder.encode(signUpDetails.getPassword()), signUpDetails.getContact(), Role.ROLE_CUSTOMER));

        // confirmation link
        String link = "http://localhost:63342/BIT-frontend/template/customer/index.html?verified=true";
        emailSender.sendEmail(signUpDetails.getEmail(), buildEmail(signUpDetails.getFirstName(), link), "Confirm Your Email");

        return savedToken;
    }

    @Override
    public void checkIsEmailAndStatusEnable(String email) {
        CustomerUser customer = customerUserRepository.getCustomerByCustomerEmail(email);

        if (customer.getAccountStatus() == 1 && customer.getEmailVerified() == 1) {
            return;
        }
        throw new DisabledException("User email not verified");
    }

    @Override
    public CustomerDTO findCustomerByEmail(String email) {
        CustomerUser customer = customerUserRepository.getCustomerByCustomerEmail(email);

        return mapper.map(customer, CustomerDTO.class);
    }

    @Override
    public CustomerDTO findCustomerById(String id) {
        Optional<CustomerUser> optionalCustomerUser = customerUserRepository.findById(id);
        CustomerDTO customerDTO = null;

        if (optionalCustomerUser.isPresent()) {
            customerDTO = mapper.map(optionalCustomerUser.get(), CustomerDTO.class);
        }
        return customerDTO;
    }

    @Override
    public void updateCustomer(MultipartFile multipartFile, String userData, String customerId) {
        String uploadDir;
        File file;
        Gson gson = new Gson();
        CustomerDTO customerDTO = gson.fromJson(userData, CustomerDTO.class);
        Optional<CustomerUser> optionalCustomerUser = customerUserRepository.findById(customerId);
        if (multipartFile != null) {
            if (optionalCustomerUser.isPresent()) {
                optionalCustomerUser.get().setCustomerFirstName(customerDTO.getFirstName());
                optionalCustomerUser.get().setCustomerLastName(customerDTO.getLastName());
                optionalCustomerUser.get().setCustomerEmail(customerDTO.getEmail());
                optionalCustomerUser.get().setContact(customerDTO.getContact());

                // check whether folder is exist or not
                uploadDir = env.getProperty("static.path") + "customerImage/" + optionalCustomerUser.get().getCustomerId();
                file = new File(uploadDir);
                if (!file.exists()) {
                    file.mkdirs();
                }

                try {
                    multipartFile.transferTo(new File(file.getAbsolutePath() + "/" + multipartFile.getOriginalFilename()));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                optionalCustomerUser.get().setProfilePicture(multipartFile.getOriginalFilename());


                customerUserRepository.save(optionalCustomerUser.get());
            }
        } else {
            optionalCustomerUser.get().setCustomerFirstName(customerDTO.getFirstName());
            optionalCustomerUser.get().setCustomerLastName(customerDTO.getLastName());
            optionalCustomerUser.get().setCustomerEmail(customerDTO.getEmail());
            optionalCustomerUser.get().setContact(customerDTO.getContact());
            customerUserRepository.save(optionalCustomerUser.get());
        }

    }

    @Override
    public void updatePassword(String customerId, String password) {
        Optional<CustomerUser> optionalCustomerUser = customerUserRepository.findById(customerId);

        optionalCustomerUser.ifPresent(customerUser -> customerUser.setPassword(passwordEncoder.encode(password)));
    }

    @Override
    public List<CustomerDTO> readAllCustomerDetails() {
        List<CustomerUser> customerUsers = customerUserRepository.findAll();
        List<CustomerDTO> customerUserDTO = new ArrayList<>();

        for (CustomerUser customer : customerUsers) {
            CustomerDTO customerDTO = new CustomerDTO();

            customerDTO.setId(customer.getCustomerId());
            customerDTO.setFirstName(customer.getCustomerFirstName());
            customerDTO.setEmail(customer.getCustomerEmail());
            customerDTO.setContact(customer.getContact());
            customerDTO.setEmailVerified(customer.getEmailVerified());
            customerDTO.setAccountStatus(customer.getAccountStatus());

            customerUserDTO.add(customerDTO);
        }

        return customerUserDTO;
    }

    @Override
    public void updateCustomerStatus(String customerId, int status) {
        Optional<CustomerUser> optionalCustomerUser = customerUserRepository.findById(customerId);

        if (optionalCustomerUser.isPresent()) {

            if (status == 1) {
                optionalCustomerUser.get().setAccountStatus(0);
            } else {
                optionalCustomerUser.get().setAccountStatus(1);
            }
            customerUserRepository.save(optionalCustomerUser.get());
        }
    }

    @Override
    public int getTotalCountOfRegistered() {
        return customerUserRepository.getTotalCountOfRegisteredCustomers();
    }

    @Override
    public int getTotalCountOfActive() {
        return customerUserRepository.getTotalCountOfActiveCustomers();
    }

    @Override
    public int getTotalCountOfDeactivate() {
        return customerUserRepository.getTotalCountOfDeactivateCustomers();
    }

    @Override
    public int getTotalCountOfVerified() {
        return customerUserRepository.getTotalCountOfVerifiedCustomers();
    }

    @Override
    public int getTotalCountOfUnVerified() {
        return customerUserRepository.getTotalCountOfUnVerifiedCustomers();
    }

    @Override
    public List<CustomerDTO> getCustomerDetailsByStatus(int status) {
        List<CustomerDTO> customerDTOList = new ArrayList<>();

        if (status == 0) {
            List<CustomerUser> customerUserActive = customerUserRepository.getCustomerDetailsByStatusActive();

            for (CustomerUser customer : customerUserActive) {
                CustomerDTO customerDTO = getCustomerDTO(customer);
                customerDTOList.add(customerDTO);
            }
        } else if (status == 1) {
            List<CustomerUser> customerUserDeactivate = customerUserRepository.getCustomerDetailsByStatusDeactivate();

            for (CustomerUser customerUser : customerUserDeactivate) {
                CustomerDTO customerDTO = getCustomerDTO(customerUser);
                customerDTOList.add(customerDTO);
            }
        } else if (status == 2) {
            List<CustomerUser> customerUserVerified = customerUserRepository.getCustomerDetailsByStatusVerified();

            for (CustomerUser customerUser : customerUserVerified) {
                CustomerDTO customerDTO = getCustomerDTO(customerUser);
                customerDTOList.add(customerDTO);
            }
        } else if (status == 3) {
            List<CustomerUser> customerUserNotVerified = customerUserRepository.getCustomerDetailsByStatusUnVerified();

            for (CustomerUser customerUser : customerUserNotVerified) {
                CustomerDTO customerDTO = getCustomerDTO(customerUser);
                customerDTOList.add(customerDTO);
            }
        } else {
          customerDTOList = readAllCustomerDetails();
        }

        return customerDTOList;
    }

    private String getCustomerId(){
        String lastCustomerId = customerUserRepository.getCustomerId();
        String newCustomerId = "";

        if (lastCustomerId == null) {
            newCustomerId = "CUS01";
        } else {
            String id = lastCustomerId.replaceAll("CUS", "");
            int newID = Integer.parseInt(id) + 1;
            if (newID < 10) {
                newCustomerId = "CUS0" + newID;
            } else {
                newCustomerId = "CUS" + newID;
            }
        }
        return newCustomerId;
    }

    private CustomerDTO getCustomerDTO(CustomerUser customer){
        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setId(customer.getCustomerId());
        customerDTO.setFirstName(customer.getCustomerFirstName());
        customerDTO.setEmail(customer.getCustomerEmail());
        customerDTO.setContact(customer.getContact());
        customerDTO.setEmailVerified(customer.getEmailVerified());
        customerDTO.setAccountStatus(customer.getAccountStatus());

        return customerDTO;
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p>" +
                "        <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p>" +
                "      <button style=\"background-color: #3176C9;\n" +
                "        border: none;\n" +
                "        border-radius: 8px;\n" +
                "        color: white;\n" +
                "        padding: 15px 32px;\n" +
                "        text-align: center;\n" +
                "        text-decoration: none;\n" +
                "        display: inline-block;\n" +
                "        font-size: 16px;\n" +
                "         cursor: pointer;\n" +
                "        margin: 4px 2px;\n" +
                "        -webkit-transition-duration: 0.4s;\n" +
                "        transition-duration: 0.4s;\"><a href=\"" + link + "\" style=\"text-decoration: none;color: white;\">Activate Now</button></p><br><br> Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
