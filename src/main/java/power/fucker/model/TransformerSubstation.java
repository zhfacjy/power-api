package power.fucker.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import power.fucker.util.RecordType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 浩发 on 2019/2/7 08:20
 * 变电站
 */
@Data
@Entity
@Table(name = RecordType.TransformerSubstation)
@EqualsAndHashCode(callSuper=false)
public class TransformerSubstation extends CommModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String label;                       // 变电站名称
    private String voltageLevel;                // 电压等级
    private String installedCapacity;           // 装机容量
    private String declaredDemand;              // 申报需求
    private String measurementControlNumber;    // 测控装置
    private String address;                     // 所在地址

}
