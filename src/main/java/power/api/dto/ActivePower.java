package power.api.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ActivePower {
    private Timestamp createAt;
    private float pa;
    private float pb;
    private float pc;
    private double p;

    public ActivePower() {
    }
}
