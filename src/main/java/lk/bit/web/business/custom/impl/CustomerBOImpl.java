package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ConfirmationTokenBO;
import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.dto.ConfirmationTokenDTO;
import lk.bit.web.dto.SignUpDTO;
import lk.bit.web.entity.CustomerUser;
import lk.bit.web.entity.Role;
import lk.bit.web.repository.CustomerUserRepository;
import lk.bit.web.util.email.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
    private Environment env;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

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
    public String saveCustomer(SignUpDTO signUpDetails) {

        CustomerUser customer = customerUserRepository.getCustomerByCustomerEmail(signUpDetails.getEmail());
        if (customer != null) {
            throw new IllegalStateException(String.format("email %s was already taken", signUpDetails.getEmail()));
        }
        //create a customer
        customerUserRepository.save(new CustomerUser("CUS1", signUpDetails.getFirstName(),
                signUpDetails.getLastName(), signUpDetails.getEmail(),
                passwordEncoder.encode(signUpDetails.getPassword()), "", Role.ROLE_CUSTOMER));

        String token = UUID.randomUUID().toString();
        String savedToken = confirmationTokenBO.saveConfirmationToken(new ConfirmationTokenDTO(
                        token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15)),
                new CustomerUser("CUS1", signUpDetails.getFirstName(),
                        signUpDetails.getLastName(), signUpDetails.getEmail(),
                        passwordEncoder.encode(signUpDetails.getPassword()), "", Role.ROLE_CUSTOMER));

        // confirmation link
        String link = "http://localhost:8080/bit/api/v1/registers/confirm?token=" + savedToken;
        emailSender.sendEmail(signUpDetails.getEmail(), buildEmail(signUpDetails.getFirstName(), link));
        return savedToken;
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
