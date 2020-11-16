package lk.bit.web.entity;

import lk.bit.web.entity.SuperEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "auth_role")
public class UserRole implements SuperEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "auth_role_id",columnDefinition = "INT")
    private int id;

    @Column(name = "role_name",length = 50,nullable = false)
    private String name;

}
