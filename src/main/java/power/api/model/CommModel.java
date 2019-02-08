package power.api.model;

import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

/**
 * Created by 浩发 on 2019/2/6 22:25
 */
public class CommModel {

    @CreatedDate
    private Date createAt;

}
