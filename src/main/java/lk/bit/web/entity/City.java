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
    @Column(name = "city_id")
    private int cityId;

    @Column(name = "city_name",length = 20, nullable = false)
    private String cityName;

    @Column(length = 10, nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "dis_id",referencedColumnName = "dis_id",nullable = false)
    private District district;
}
