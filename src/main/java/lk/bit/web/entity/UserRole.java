package lk.bit.web.entity;

import lombok.*;

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
public class UserRole implements SuperEntity{
    @Id
    private String id;
    private String name;
}
