package power.fucker.service;

import power.fucker.model.MeterRecord;

import java.util.List;

/**
 * Created by 浩发 on 2019/2/4 13:46
 */
public interface MeterRecordService {

    List<MeterRecord> findAllByIds(List<Integer> ids);
}
