package power.fucker.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import power.fucker.util.RecordType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 浩发 on 2019/2/6 22:39
 * 进线柜和变电站的关联
 */
@Data
@Entity
@Table(name = RecordType.DlcTransformerSub)
@EqualsAndHashCode(callSuper=false)
public class DictLineCountersIdAndSubstationId implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer transformerSubstationId;    // 变电站id
    private Integer dictLineCountersId;         // 进线柜id
}
