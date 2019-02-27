package power.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import power.api.util.RecordType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 浩发 on 2019/2/26 10:08
 * 越限事件
 */
@Data
@Entity
@Table(name = RecordType.OverLimitEvent)
@EqualsAndHashCode(callSuper=false)
public class OverLimitEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date beginDate;                 // 开始时间
    private Date endDate;                   // 结束时间
    private String type;                    // 类型 01、高温报警
    private String meter;                   // 电表
    private String defaultValue;            // 限定值
    private String warningValue;            // 报警值
    private Integer hasRead = 0;                // 已读未读


}
