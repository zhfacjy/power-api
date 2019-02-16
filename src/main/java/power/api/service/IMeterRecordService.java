package power.api.service;

import power.api.controller.paramModel.GetElectricDataParam;
import power.api.common.RestResp;

/**
 * Created by 浩发 on 2019/2/4 13:46
 */
public interface IMeterRecordService {
    RestResp countActivePowerData(long createAt, GetElectricDataParam getElectricDataParam);

    RestResp countActivePowerDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam);
}
