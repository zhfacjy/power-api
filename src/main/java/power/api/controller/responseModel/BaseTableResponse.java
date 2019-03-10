package power.api.controller.responseModel;

import lombok.Data;
import power.api.controller.responseModel.powerMonitoring.BaseColumnItem;

import java.util.List;

@Data
public class BaseTableResponse<T> {
    List<BaseColumnItem> columnList;
    List<T> data;
}
