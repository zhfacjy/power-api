package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;

@Data
public class BaseColumnItem {
    String name;
    String value;

    BaseColumnItem() {
    }

    public BaseColumnItem(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
