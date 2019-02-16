package power.api.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.Temporal;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;
/**
 * Created by 浩发 on 2019/2/6 22:25
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class CommModel {

    @CreatedDate
    private Timestamp createAt;

}
