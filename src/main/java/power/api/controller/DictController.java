package power.api.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import power.api.controller.paramModel.CreateDictParam;
import power.api.dto.RestResp;
import power.api.service.IDictService;
import power.api.service.impl.DictService;

// TODO 所有接口的类型检查

@RestController
@Api(description = "字典表")
public class DictController {

    @Autowired
    private IDictService iDictService;

    @PostMapping("/dict")
    public RestResp createDict(@RequestBody CreateDictParam createDictParam) {
        return iDictService.createDictItem(createDictParam);
    }

    @GetMapping("/dict/{type}/list")
    public RestResp getDictList(@PathVariable String type) {
        return iDictService.getDictListByType(type);
    }
}
