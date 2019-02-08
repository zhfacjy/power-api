package power.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import power.api.util.RecordType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 浩发 on 2019/2/7 08:26
 */
@Data
@Entity
@Table(name = RecordType.User)
@EqualsAndHashCode(callSuper=false)
public class User extends CommModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String mobile;
}
