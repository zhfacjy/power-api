package power.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import power.api.util.RecordType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 浩发 on 2019/2/6 22:39
 * 电表和进线柜的关联
 */
@Data
@Entity
@Table(name = RecordType.DmvAndDlc)
@EqualsAndHashCode(callSuper=false)
public class DictMeterValueAndDictLineCountersId implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dictMeterValue;          // 电表号
    private Integer dictLineCountersId;     // 进线柜id
}
