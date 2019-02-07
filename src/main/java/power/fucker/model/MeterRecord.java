package power.fucker.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import power.fucker.util.RecordType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 浩发 on 2019/2/6 22:17
 * 电表数据
 */
@Data
@Entity
@Table(name = RecordType.MeterRecord)
@EqualsAndHashCode(callSuper=false)
public class MeterRecord extends CommModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String centralNode;         // 中心节点
    private String meter;               // 电表
    private String command;             // 命令
    private String earlyWarning;        // 预警
    private Float va;                   // a相电压
    private Float vb;
    private Float vc;
    private Float ia;                   // A相电流
    private Float ib;
    private Float ic;
    private Double power;               // 功率
    private Double electricEnergy;      // 电能
    private Integer temperature;        // 温度
    private Integer currentLimit;       // 电流上限
    private String crc;
}
