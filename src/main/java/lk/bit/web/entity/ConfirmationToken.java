package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "confirmation_token")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,length = 100)
    private String token;

    @Column(name = "created_Date",nullable = false)
    private LocalDateTime createdDate;

    @Column(name= "expired_date",nullable = false)
    private LocalDateTime expiredDate;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false,
    name = "customer_user_id",referencedColumnName = "id")
    private CustomerUser customerUserId;

    public ConfirmationToken(String token,
                             LocalDateTime createdDate,
                             LocalDateTime expiredDate,
                             CustomerUser customerUser) {
        this.token = token;
        this.createdDate = createdDate;
        this.expiredDate = expiredDate;
        this.customerUserId = customerUser;
    }

}
