package power.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import power.api.util.RecordType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 浩发 on 2019/2/7 08:34
 * 计划的执行成员
 */
@Data
@Entity
@Table(name = RecordType.UserIdAndPatrolPlanId)
@EqualsAndHashCode(callSuper=false)
public class UserIdAndPatrolPlanId extends CommModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer patrolPlanId;  // 巡检计划id

}
