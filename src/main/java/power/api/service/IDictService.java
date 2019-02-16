package power.api.service;

import power.api.controller.paramModel.CreateDictParam;
import power.api.common.RestResp;

public interface IDictService {
    RestResp createDictItem(CreateDictParam createDictParam);

    RestResp getDictListByType(String type);
}
