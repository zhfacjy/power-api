package power.api.controller.paramModel;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "注册管理员的参数")
public class LoginParam {
    private String username;
    private String password;
}
