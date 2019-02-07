package power.fucker.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import power.fucker.util.RecordType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 浩发 on 2019/2/7 08:13
 * 巡检计划表
 */
@Data
@Entity
@Table(name = RecordType.PatrolPlan)
@EqualsAndHashCode(callSuper=false)
public class PatrolPlan extends CommModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer transformerSubstationId;    // 变电站id
    private Integer dictPatrolGroupId;          // 巡检组别id
    private Integer dictPatrolCategoryId;       // 巡检类别id
    private Integer dictPatrolNatureId;         // 巡检性质id
    private String patrolContent;               // 巡检内容
    private Integer dictPlanStatusId;           // 执行状态
    private Date reviewAt;                      // 审核日期
    private Integer reviewBy;                   // 审核人
    private Date finishAt;                      // 完成日期
    private Integer createBy;

}
