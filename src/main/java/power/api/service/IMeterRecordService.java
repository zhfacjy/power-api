package power.api.service;

import power.api.controller.paramModel.GetElectricDataParam;
import power.api.dto.RestResp;
import power.api.model.MeterRecord;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by 浩发 on 2019/2/4 13:46
 */
public interface IMeterRecordService {
    RestResp countActivePowerData(long createAt, GetElectricDataParam getElectricDataParam);

    RestResp countActivePowerDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam);
}
