package lk.bit.web.entity.admin_entity;

import lk.bit.web.entity.SuperEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "user_role")
public class UserRole implements SuperEntity {
    @Id
    private String id;
    @Column(name = "role_name")
    private String name;

    public UserRole(String id) {
        this.id = id;
    }
}
