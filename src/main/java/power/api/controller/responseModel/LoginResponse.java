package power.api.controller.responseModel;

import lombok.Data;
import power.api.model.User;

@Data
public class LoginResponse {
    private User user;
    private String token;
}
