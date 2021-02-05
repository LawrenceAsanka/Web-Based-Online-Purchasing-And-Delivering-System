package lk.bit.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ConfirmationTokenDTO {

    private String token;
    private LocalDateTime createdDate;
    private LocalDateTime expiredDate;

}
