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
    @Column(name = "id", length = 20)
    private String shopId;

    @Column(name = "name", length = 50, nullable = false)
    private String shopName;

    @Column(length = 20, nullable = false)
    private String contact;

    @Column(name = "address1", length = 50, nullable = false)
    private String address1;

    @Column(name = "address2", length = 50, nullable = false)
    private String address2;

    @Column(name = "city", length = 50, nullable = false)
    private String city;

    @Column(name = "district", length = 50, nullable = false)
    private String district;

    @Column(name = "is_active", nullable = false, columnDefinition = "tinyint(1)")
    private int isActive = 1;

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "category_id", nullable = false)
    private ShopCategory shopCategory;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private CustomerUser customerUser;

    public Shop(String shopId, String shopName, String contact, String address1,
                String address2, String city, String district, ShopCategory shopCategory,
                CustomerUser customerUser) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.contact = contact;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.district = district;
        this.shopCategory = shopCategory;
        this.customerUser = customerUser;
    }
}
