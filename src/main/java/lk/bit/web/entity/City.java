package lk.bit.web.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "city")
public class City implements SuperEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dis_id")
    private int DistrictId;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "dis_id")
    private District district;

    @Column(length = 20, nullable = false)
    private String city;

    @Column(length = 10, nullable = false)
    private String status;
}
