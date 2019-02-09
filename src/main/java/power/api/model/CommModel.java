package power.api.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created by 浩发 on 2019/2/6 22:25
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class CommModel {

    @CreatedDate
    private Date createAt;

}
