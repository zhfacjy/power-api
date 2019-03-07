package power.api.controller;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import power.api.common.RestResp;
import power.api.controller.paramModel.SearchOverLimitEventParam;
import power.api.service.IOverLimitEventService;
import power.api.util.DictValue;

import javax.validation.Valid;

/**
 * Created by 浩发 on 2019/3/5 17:22
 * 越限事件
 */
@RestController
@RequestMapping("/over/limit/event")
public class OverLimitEventController {

    @Autowired
    private IOverLimitEventService overLimitEventService;

    /**
     * 获取越限事件类型
     * @return
     */
    @GetMapping("/type")
    public RestResp getType() {
        return new RestResp(DictValue.overLimitType);
    }

    /**
     * 查询越限事件
     * @param params
     * @return
     */
    @PostMapping("/search")
    public RestResp search(@RequestBody @Valid SearchOverLimitEventParam params,
                           @ApiParam("页数") @RequestParam(name = "pageNo",defaultValue = "1",required = false) Integer pageNo,
                           @ApiParam("页码") @RequestParam(name = "pageSize",defaultValue = "10",required = false) Integer pageSize) {
        return new RestResp(overLimitEventService.search(params,pageNo,pageSize));
    }
}
