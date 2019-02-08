package power.api.model;

import power.api.util.RecordType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 浩发 on 2019/2/4 10:39
 * 字典表
 */
@Data
@Entity
@Table(name = RecordType.Dict)
@EqualsAndHashCode(callSuper=false)
public class Dict extends CommModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String label;       // 字典描述
    private String value;       // 字典值
    private Integer parentId;   // 父id
    private String type;        // 类型


}
