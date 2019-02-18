package power.api.dto;

/**
 * 这个类用来装载Sql查询后返回的结果
 */
public interface MaxAvgMinDto {
    Double getMaxHolder();

    Double getAvgHolder();

    Double getMinHolder();

    String getCreateAt();
}
