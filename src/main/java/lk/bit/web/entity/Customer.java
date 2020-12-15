package lk.bit.web.entity;

import lk.bit.web.entity.SuperEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "customer")
public class Customer implements SuperEntity {

    @Id
    @Column(name = "customer_id",length = 20)
    private String customerId;

    @Column(name = "customer_fname",length = 50,nullable = false)
    private String customerFirstName;

    @Column(name = "customer_lname",length = 50,nullable = false)
    private String customerLastName;

    @Column(name = "customer_email",length = 50,nullable = false)
    private String customerEmail;

    @Column(length = 10,nullable = false)
    private String password;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(length = 20,nullable = false)
    private String status;


    public Customer(String ownerId){
       this.customerId = ownerId;
    }
}
