package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ConfirmationTokenBO;
import lk.bit.web.dto.ConfirmationTokenDTO;
import lk.bit.web.entity.ConfirmationToken;
import lk.bit.web.entity.CustomerUser;
import lk.bit.web.repository.ConfirmationTokenRepository;
import lk.bit.web.repository.CustomerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Transactional
public class ConfirmationTokenBOImpl implements ConfirmationTokenBO {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private CustomerUserRepository customerUserRepository;

    @Override
    public String saveConfirmationToken(ConfirmationTokenDTO confirmationTokenDTO, CustomerUser customerUser) {
        ConfirmationToken token = confirmationTokenRepository.save(new ConfirmationToken(confirmationTokenDTO.getToken(),
                confirmationTokenDTO.getCreatedDate(), confirmationTokenDTO.getExpiredDate(), customerUser));

        return token.getToken();
    }

    @Override
    public String confirmToken(String token) {
        boolean tokenPresent = confirmationTokenRepository.findByToken(token).isPresent();

        if (!tokenPresent) {
            throw new IllegalStateException("Token not found");
        }

        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token).get();

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredDate = confirmationToken.getExpiredDate();

        if (expiredDate.isBefore(LocalDateTime.now())) {
            throw  new IllegalStateException("token expired");
        }

        CustomerUser customerUser = confirmationToken.getCustomerUserId();
        Optional<CustomerUser> id = customerUserRepository.findById(customerUser.getCustomerId());
        if (id.isPresent()) {
            customerUser.setEmailVerified(1);
            customerUser.setAccountStatus(1);
            customerUserRepository.save(customerUser);
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
        return "confirmed";
    }


}
