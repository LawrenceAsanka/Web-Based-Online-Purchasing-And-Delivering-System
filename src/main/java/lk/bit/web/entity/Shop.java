package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "shop")
public class Shop implements SuperEntity {

    @Id
    @Column(name= "shop_id",length = 20)
    private String shopId;
    @Column(name = "shop_name",length = 50,nullable = false)
    private String shopName;
    @Column(name = "shop_category",length = 15,nullable = false)
    private String shopCategory;
    @Column(length = 20,nullable = false)
    private String contact;
    private String address;
    @ManyToOne
    @JoinColumn(name = "owner_id",referencedColumnName = "customer_id",nullable = false)
    private Customer customer;

    public Shop(String shopId,String shopName, String shopCategory, String contact, String address,String ownerId) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopCategory = shopCategory;
        this.contact = contact;
        this.address = address;
        this.customer = new Customer(ownerId);
    }
}
