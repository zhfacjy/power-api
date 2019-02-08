package power.api.service.impl;

import org.springframework.stereotype.Service;
import power.api.model.MeterRecord;
import power.api.service.MeterRecordService;

import java.util.List;

/**
 * Created by 浩发 on 2019/2/4 13:46
 */
@Service
public class MeterRecordServiceImpl implements MeterRecordService {

    @Override
    public List<MeterRecord> findAllByIds(List<Integer> ids) {
        return null;
    }
}
