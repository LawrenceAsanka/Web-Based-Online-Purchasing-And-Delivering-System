package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "district")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dis_id",length = 10)
    private int districtId;

    @Column(name = "dis_name",length = 50,nullable = false)
    private String districtName;

    @Column(length = 20,nullable = false)
    private String status;
}
