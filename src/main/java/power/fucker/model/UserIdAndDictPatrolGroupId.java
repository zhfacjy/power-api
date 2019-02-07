package power.fucker.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import power.fucker.util.RecordType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 浩发 on 2019/2/7 08:30
 * 用户与巡检组
 */
@Data
@Entity
@Table(name = RecordType.UserIdDictPatrolGroupId)
@EqualsAndHashCode(callSuper=false)
public class UserIdAndDictPatrolGroupId extends CommModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer dictPatrolGroupId;  // 巡检组id

}
