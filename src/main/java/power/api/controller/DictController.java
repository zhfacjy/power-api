package power.api.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import power.api.controller.paramModel.CreateDictParam;
import power.api.dto.RestResp;

@RestController
@Api(description = "字典表")
public class DictController {
    @PostMapping("/dict")
    public RestResp createDict(@RequestBody CreateDictParam createDictParam) {
        System.out.println(createDictParam);
        return RestResp.createBySuccess();
    }
}
