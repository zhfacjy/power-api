package power.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import power.api.dto.RestResp;
import power.api.repository.DictRepository;

@Service
public class DictService {
    @Autowired
    private DictRepository dictRepository;

    public RestResp createDict() {
        return null;
    }
}
