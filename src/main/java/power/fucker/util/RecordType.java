package power.fucker.util;

/**
 * Created by 浩发 on 2019/2/4 10:40
 */
public class RecordType {

    // 电表数据记录
    public final static String MeterRecord = "meter_record";
    // 字典表
    public final static String Dict = "dict";
    // 进线柜和变电站的关联
    public final static String DlcTransformerSub = "dict_line_counters_id__transformer_substation_id";
    // 电表和进线柜的关联
    public final static String DmvAndDlc = "dict_meter_value__dict_line_counters_id";
    // 巡检计划表
    public final static String PatrolPlan = "patrol_plan";
    // 变电站
    public final static String TransformerSubstation = "transformer_substation";
    // 用户表
    public final static String User = "user";
    // 用户与巡检组
    public final static String UserIdDictPatrolGroupId = "user_id__dict_patrol_group_id";
    // 计划的执行成员
    public final static String UserIdAndPatrolPlanId = "user_id__patrol_plan_id";

}
