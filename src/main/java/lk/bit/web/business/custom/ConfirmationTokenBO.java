package lk.bit.web.business.custom;

import lk.bit.web.dto.ConfirmationTokenDTO;
import lk.bit.web.entity.CustomerUser;

public interface ConfirmationTokenBO {

    String saveConfirmationToken(ConfirmationTokenDTO confirmationTokenDTO, CustomerUser customerUser);

    String confirmToken(String token);
}
