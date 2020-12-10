package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "advertisement")
public class Advertisement implements SuperEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adver_id")
    private int advertisementId;

    @Column(name = "adver_name",length = 50,nullable = false)
    private String advertisementName;

    @Column(name = "adver_image",nullable = false)
    private String advertisementImage;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creadted_date_time",nullable = false)
    private Date createdDateTime;

    @Column(nullable = false,length = 20)
    private String status;
}
