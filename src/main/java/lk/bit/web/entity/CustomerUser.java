package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "customer_user")
public class CustomerUser implements SuperEntity {

    @Id
    @Column(name = "id",length = 20)
    private String customerId;

    @Column(name = "customer_fname",length = 50,nullable = false)
    private String customerFirstName;

    @Column(name = "customer_lname",length = 50,nullable = false)
    private String customerLastName;

    @Column(name = "customer_email",length = 50,unique = true,nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String password;

    @Column(name = "profile_picture")
    private String profilePicture = "no-image";

    @Column(nullable = false,length = 15)
    private String contact;

    @Column(name = "credit_limit", nullable = false)
    private BigDecimal creditLimit = new BigDecimal("0.00");

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "email_verified",nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private int emailVerified;

    @Column(name = "account_status",nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private int accountStatus;

    public CustomerUser(String customerId,
                        String customerFirstName,
                        String customerLastName,
                        String customerEmail,
                        String password,
                        String contact,
                        Role role) {
        this.customerId = customerId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerEmail = customerEmail;
        this.password = password;
        this.contact = contact;
        this.role = role;
    }
}
