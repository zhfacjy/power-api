package power.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import power.api.controller.paramModel.CreateDictParam;
import power.api.common.RestResp;
import power.api.model.Dict;
import power.api.repository.DictRepository;
import power.api.service.IDictService;

import java.util.List;

@Service
public class DictService implements IDictService {
    @Autowired
    private DictRepository dictRepository;

    @Override
    public RestResp createDictItem(CreateDictParam createDictParam) {
        Dict dict = new Dict();
        dict.setLabel(createDictParam.getLabel());
        dict.setParentId(createDictParam.getParentId());
        dict.setValue(createDictParam.getValue());
        dict.setType(createDictParam.getType());
        dictRepository.saveAndFlush(dict);
        return RestResp.createBySuccess(dict);
    }

    @Override
    public RestResp getDictListByType(String type) {
        List<Dict> dictList = dictRepository.findAllByType(type);
        return RestResp.createBySuccess(dictList);
    }
}
