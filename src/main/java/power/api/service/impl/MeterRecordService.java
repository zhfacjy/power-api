package power.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import power.api.common.RestResp;
import power.api.common.TableFieldEnum;
import power.api.controller.paramModel.GetElectricDataParam;
import power.api.controller.responseModel.BaseTableResponse;
import power.api.controller.responseModel.powerMonitoring.*;
import power.api.controller.responseModel.powerMonitoring.limitReport.*;
import power.api.controller.responseModel.powerMonitoring.runningReport.PhaseReportItem;
import power.api.controller.responseModel.powerMonitoring.runningReport.ReportResponse;
import power.api.dto.LimitReportDto;
import power.api.dto.MaxAvgMinDto;
import power.api.dto.MeterRecordDto;
import power.api.dto.PhaseVoltageReportDto;
import power.api.model.MeterRecord;
import power.api.repository.MeterRecordRepository;
import power.api.service.IMeterRecordService;
import power.api.util.AutoAssembleUtil;
import power.api.util.DateFormatUtil;
import power.api.util.PaddingTimeUtil;
import power.api.util.meterRecordCalculator.MeterRecordCalculator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by 浩发 on 2019/2/4 13:46
 */
@Service
public class MeterRecordService implements IMeterRecordService {

    @Autowired
    private MeterRecordRepository meterRecordRepository;

    private final String DATA_KEY = "data";
    private final String COLUMN_KEY = "column";
    private DecimalFormat decimalFormat = new DecimalFormat("0.0");


    private HashMap<String, List> countCommon(long createAt, TableFieldEnum[] fieldEnums) throws InvocationTargetException, IllegalAccessException {
        // 日期转为完整的日期
        String dateString = DateFormatUtil.formatDateTo(createAt, DateFormatUtil.SECOND_FORMAT);
        // 获取数据，并从DTO转为实体类型，以便使用计算工具类
        List<MeterRecordDto> dtoList = this.getMeterRecordDtoListByCreateAt(dateString);
//        List<MeterRecord> recordList = new ArrayList<>(dtoList.size());
//        for (MeterRecordDto dto : dtoList) {
//            MeterRecord m = new MeterRecord();
//            AutoAssembleUtil.assembleSameNameField(m, dto);
//            recordList.add(m);
//        }
        // 构造响应给前端的自定义列
        List<BaseColumnItem> columnItemList = this.produceColumnListAndIndex(fieldEnums);
        HashMap<String, List> dataResponse = new HashMap<>();
        dataResponse.put(COLUMN_KEY, columnItemList);
        dataResponse.put(DATA_KEY, dtoList);
        return dataResponse;
    }

    @Override
    public RestResp countActivePowerData(long createAt, GetElectricDataParam getElectricDataParam) {
        TableFieldEnum[] fieldEnums = new TableFieldEnum[]{
                TableFieldEnum.LOOP_NAME,
                TableFieldEnum.COLECTION_TIME,
                TableFieldEnum.P_A,
                TableFieldEnum.P_B,
                TableFieldEnum.P_C,
                TableFieldEnum.P
        };
        HashMap<String, List> map = null;
        try {
            map = this.countCommon(createAt, fieldEnums);
            List<MeterRecordDto> meterRecordList = map.get(DATA_KEY);
            List<BaseColumnItem> columnItemList = map.get(COLUMN_KEY);
            // 构造表格每一行的值
            List<JSONObject> dataList = new LinkedList<>();
            for (MeterRecordDto record : meterRecordList) {
                JSONObject dataItem = new JSONObject();

                String dataItemKey = TableFieldEnum.LOOP_NAME.getValue();
                dataItem.put(dataItemKey, "主进线柜");

                dataItemKey = TableFieldEnum.COLECTION_TIME.getValue();
                dataItem.put(dataItemKey, record.getCreateAt());

                // TODO 计算A相有功功率
                dataItemKey = TableFieldEnum.P_A.getValue();
                dataItem.put(dataItemKey, 0);

                // TODO 计算B相有功功率
                dataItemKey = TableFieldEnum.P_B.getValue();
                dataItem.put(dataItemKey, 0);

                // TODO 计算C相有功功率
                dataItemKey = TableFieldEnum.P_C.getValue();
                dataItem.put(dataItemKey, 0);

                dataItemKey = TableFieldEnum.P.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(record.getActivePower()));
                dataList.add(dataItem);
            }
            BaseTableResponse<JSONObject> baseTableResponse = new BaseTableResponse<JSONObject>();
            baseTableResponse.setColumnList(columnItemList);
            baseTableResponse.setData(dataList);
            return RestResp.createBySuccess(baseTableResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.createBy(RestResp.ERROR, "服务器内部错误");
        }
    }

    @Override
    public RestResp countApparentPowerData(long createAt, GetElectricDataParam getElectricDataParam) {
        // 自定义的表格列
        TableFieldEnum[] fieldEnums = new TableFieldEnum[]{
                TableFieldEnum.LOOP_NAME,
                TableFieldEnum.COLECTION_TIME,
                TableFieldEnum.S_A,
                TableFieldEnum.S_B,
                TableFieldEnum.S_C,
                TableFieldEnum.S
        };
        HashMap<String, List> map = null;
        try {
            map = this.countCommon(createAt, fieldEnums);
            List<MeterRecordDto> meterRecordList = map.get(DATA_KEY);
            List<BaseColumnItem> columnItemList = map.get(COLUMN_KEY);
            // 构造表格每一行的值
            List<JSONObject> dataList = new LinkedList<>();

            for (MeterRecordDto record : meterRecordList) {
                JSONObject dataItem = new JSONObject();

                String dataItemKey = TableFieldEnum.LOOP_NAME.getValue();
                dataItem.put(dataItemKey, "主进线柜");

                dataItemKey = TableFieldEnum.COLECTION_TIME.getValue();
                dataItem.put(dataItemKey, record.getCreateAt());

                // 三相和总的视在功率
                dataItemKey = TableFieldEnum.S_A.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(MeterRecordCalculator.countApparentPowerA(record)));
                dataItemKey = TableFieldEnum.S_B.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(MeterRecordCalculator.countApparentPowerB(record)));
                dataItemKey = TableFieldEnum.S_C.getValue();
                dataItem.put(dataItemKey, MeterRecordCalculator.countApparentPowerC(record));
                dataItemKey = TableFieldEnum.S.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(MeterRecordCalculator.countApparentPowerTotal(record)));

                dataList.add(dataItem);
            }
            BaseTableResponse<JSONObject> baseTableResponse = new BaseTableResponse<JSONObject>();
            baseTableResponse.setColumnList(columnItemList);
            baseTableResponse.setData(dataList);
            return RestResp.createBySuccess(baseTableResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.createBy(RestResp.ERROR, "服务器内部错误");
        }
    }

    @Override
    public RestResp countPhaseCurrentData(long createAt, GetElectricDataParam getElectricDataParam) {
        // 自定义的表格列
        TableFieldEnum[] fieldEnums = new TableFieldEnum[]{
                TableFieldEnum.LOOP_NAME,
                TableFieldEnum.COLECTION_TIME,
                TableFieldEnum.I_A,
                TableFieldEnum.I_B,
                TableFieldEnum.I_C
        };
        HashMap<String, List> map = null;
        try {
            map = this.countCommon(createAt, fieldEnums);
            List<MeterRecordDto> meterRecordList = map.get(DATA_KEY);
            List<BaseColumnItem> columnItemList = map.get(COLUMN_KEY);
            // 构造表格每一行的值
            List<JSONObject> dataList = new LinkedList<>();
            for (MeterRecordDto record : meterRecordList) {

                JSONObject dataItem = new JSONObject();

                String dataItemKey = TableFieldEnum.LOOP_NAME.getValue();
                dataItem.put(dataItemKey, "主进线柜");

                dataItemKey = TableFieldEnum.COLECTION_TIME.getValue();
                dataItem.put(dataItemKey, record.getCreateAt());

                dataItemKey = TableFieldEnum.I_A.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(record.getIa()));
                dataItemKey = TableFieldEnum.I_B.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(record.getIb()));
                dataItemKey = TableFieldEnum.I_C.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(record.getIc()));

                dataList.add(dataItem);
            }
            BaseTableResponse<JSONObject> baseTableResponse = new BaseTableResponse<JSONObject>();
            baseTableResponse.setColumnList(columnItemList);
            baseTableResponse.setData(dataList);
            return RestResp.createBySuccess(baseTableResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.createBy(RestResp.ERROR, "服务器内部错误");
        }
    }


    @Override
    public RestResp countPhaseVoltageData(long createAt, GetElectricDataParam getElectricDataParam) {
        // 自定义的表格列
        TableFieldEnum[] fieldEnums = new TableFieldEnum[]{
                TableFieldEnum.LOOP_NAME,
                TableFieldEnum.COLECTION_TIME,
                TableFieldEnum.U_A,
                TableFieldEnum.U_B,
                TableFieldEnum.U_C
        };
        HashMap<String, List> map = null;
        try {
            map = this.countCommon(createAt, fieldEnums);
            List<MeterRecordDto> meterRecordList = map.get(DATA_KEY);
            List<BaseColumnItem> columnItemList = map.get(COLUMN_KEY);
            // 构造表格每一行的值
            List<JSONObject> dataList = new LinkedList<>();
            for (MeterRecordDto record : meterRecordList) {

                JSONObject dataItem = new JSONObject();

                String dataItemKey = TableFieldEnum.LOOP_NAME.getValue();
                dataItem.put(dataItemKey, "主进线柜");

                dataItemKey = TableFieldEnum.COLECTION_TIME.getValue();
                dataItem.put(dataItemKey, record.getCreateAt());

                dataItemKey = TableFieldEnum.U_A.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(record.getUa()));
                dataItemKey = TableFieldEnum.U_B.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(record.getUb()));
                dataItemKey = TableFieldEnum.U_C.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(record.getUc()));

                dataList.add(dataItem);
            }
            BaseTableResponse<JSONObject> baseTableResponse = new BaseTableResponse<JSONObject>();
            baseTableResponse.setColumnList(columnItemList);
            baseTableResponse.setData(dataList);
            return RestResp.createBySuccess(baseTableResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.createBy(RestResp.ERROR, "服务器内部错误");
        }
    }

    @Override
    public RestResp countLineVoltageData(long createAt, GetElectricDataParam getElectricDataParam) {
        // 自定义的表格列
        TableFieldEnum[] fieldEnums = new TableFieldEnum[]{
                TableFieldEnum.LOOP_NAME,
                TableFieldEnum.COLECTION_TIME,
                TableFieldEnum.U_AB,
                TableFieldEnum.U_BC,
                TableFieldEnum.U_CA
        };
        HashMap<String, List> map = null;
        try {
            map = this.countCommon(createAt, fieldEnums);
            List<MeterRecordDto> meterRecordList = map.get(DATA_KEY);
            List<BaseColumnItem> columnItemList = map.get(COLUMN_KEY);
            // 构造表格每一行的值
            List<JSONObject> dataList = new LinkedList<>();
            for (MeterRecordDto record : meterRecordList) {

                JSONObject dataItem = new JSONObject();

                String dataItemKey = TableFieldEnum.LOOP_NAME.getValue();
                dataItem.put(dataItemKey, "主进线柜");

                dataItemKey = TableFieldEnum.COLECTION_TIME.getValue();
                dataItem.put(dataItemKey, record.getCreateAt());

                dataItemKey = TableFieldEnum.U_AB.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(MeterRecordCalculator.countLineVoltageUab(record)));
                dataItemKey = TableFieldEnum.U_BC.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(MeterRecordCalculator.countLineVoltageUbc(record)));
                dataItemKey = TableFieldEnum.U_CA.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(MeterRecordCalculator.countLineVoltageUca(record)));

                dataList.add(dataItem);
            }
            BaseTableResponse<JSONObject> baseTableResponse = new BaseTableResponse<JSONObject>();
            baseTableResponse.setColumnList(columnItemList);
            baseTableResponse.setData(dataList);
            return RestResp.createBySuccess(baseTableResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.createBy(RestResp.ERROR, "服务器内部错误");
        }
    }

    @Override
    public RestResp countPowerFactorData(long createAt, GetElectricDataParam getElectricDataParam) {
        // 自定义的表格列
        TableFieldEnum[] fieldEnums = new TableFieldEnum[]{
                TableFieldEnum.LOOP_NAME,
                TableFieldEnum.COLECTION_TIME,
                TableFieldEnum.P_FA,
                TableFieldEnum.P_FB,
                TableFieldEnum.P_FC,
                TableFieldEnum.P
        };
        HashMap<String, List> map = null;
        try {
            map = this.countCommon(createAt, fieldEnums);
            List<MeterRecordDto> meterRecordList = map.get(DATA_KEY);
            List<BaseColumnItem> columnItemList = map.get(COLUMN_KEY);
            // 构造表格每一行的值
            List<JSONObject> dataList = new LinkedList<>();
            for (MeterRecordDto record : meterRecordList) {

                JSONObject dataItem = new JSONObject();

                String dataItemKey = TableFieldEnum.LOOP_NAME.getValue();
                dataItem.put(dataItemKey, "主进线柜");

                dataItemKey = TableFieldEnum.COLECTION_TIME.getValue();
                dataItem.put(dataItemKey, record.getCreateAt());

                dataItemKey = TableFieldEnum.P_FA.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(record.getPfa()));
                dataItemKey = TableFieldEnum.P_FB.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(record.getPfb()));
                dataItemKey = TableFieldEnum.P_FC.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(record.getPfc()));

                // TODO 把计算方式写进工具类
                float total = record.getPfa() + record.getPfb() + record.getPfc();
                dataItemKey = TableFieldEnum.P.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(total));

                dataList.add(dataItem);
            }
            BaseTableResponse<JSONObject> baseTableResponse = new BaseTableResponse<JSONObject>();
            baseTableResponse.setColumnList(columnItemList);
            baseTableResponse.setData(dataList);
            return RestResp.createBySuccess(baseTableResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.createBy(RestResp.ERROR, "服务器内部错误");
        }
    }

    @Override
    public RestResp countReactivePowerData(long createAt, GetElectricDataParam getElectricDataParam) {
        // 自定义的表格列
        TableFieldEnum[] fieldEnums = new TableFieldEnum[]{
                TableFieldEnum.LOOP_NAME,
                TableFieldEnum.COLECTION_TIME,
                TableFieldEnum.Q_A,
                TableFieldEnum.Q_B,
                TableFieldEnum.Q_C,
                TableFieldEnum.Q
        };
        HashMap<String, List> map = null;
        try {
            map = this.countCommon(createAt, fieldEnums);
            List<MeterRecordDto> meterRecordList = map.get(DATA_KEY);
            List<BaseColumnItem> columnItemList = map.get(COLUMN_KEY);
            // 构造表格每一行的值
            List<JSONObject> dataList = new LinkedList<>();
            for (MeterRecordDto record : meterRecordList) {

                JSONObject dataItem = new JSONObject();

                String dataItemKey = TableFieldEnum.LOOP_NAME.getValue();
                dataItem.put(dataItemKey, "主进线柜");

                dataItemKey = TableFieldEnum.COLECTION_TIME.getValue();
                dataItem.put(dataItemKey, record.getCreateAt());

                // TODO 计算A相无功功率
                dataItemKey = TableFieldEnum.Q_A.getValue();
                dataItem.put(dataItemKey, 0);

                // TODO 计算B相无功功率
                dataItemKey = TableFieldEnum.Q_B.getValue();
                dataItem.put(dataItemKey, 0);

                // TODO 计算C相无功功率
                dataItemKey = TableFieldEnum.Q_C.getValue();
                dataItem.put(dataItemKey, 0);

                // 暂时性使用这个方法
                float total = MeterRecordCalculator.countReactivePowerTotalTest(record);
                dataItemKey = TableFieldEnum.Q.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(total));

                dataList.add(dataItem);
            }
            BaseTableResponse<JSONObject> baseTableResponse = new BaseTableResponse<JSONObject>();
            baseTableResponse.setColumnList(columnItemList);
            baseTableResponse.setData(dataList);
            return RestResp.createBySuccess(baseTableResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.createBy(RestResp.ERROR, "服务器内部错误");
        }

        //以下注释不要删，以后要用的
/*        PhaseHolder reactivePowerResponseHolder = new PhaseHolder();

        for (MeterRecord m : meterRecordList) {
            reactivePowerResponseHolder.setPhaseA(m.getUa() * m.getIa() * (float) (-Math.cos(Math.PI / 2 + Math.toDegrees(Math.acos(m.getPfa())))));
            reactivePowerResponseHolder.setPhaseB(m.getUb() * m.getIb() * (float) (-Math.cos(Math.PI / 2 + Math.toDegrees(Math.acos(m.getPfb())))));
            reactivePowerResponseHolder.setPhaseC(m.getUc() * m.getIc() * (float) (-Math.cos(Math.PI / 2 + Math.toDegrees(Math.acos(m.getPfc())))));
            ReactivePowerResponse reactivePowerResponse = new ReactivePowerResponse();

            reactivePowerResponse.setCreateAt(m.getCreateAt());
            reactivePowerResponse.setReactivePowerA(reactivePowerResponseHolder.getPhaseA());
            reactivePowerResponse.setReactivePowerB(reactivePowerResponseHolder.getPhaseB());
            reactivePowerResponse.setReactivePowerC(reactivePowerResponseHolder.getPhaseC());
            reactivePowerResponse.setReactivePowerTotal(reactivePowerResponseHolder.getPhaseA() + reactivePowerResponseHolder.getPhaseB()+ reactivePowerResponseHolder.getPhaseC());
            reactivePowerResponseList.add(reactivePowerResponse);
        }*/
    }

    @Override
    public RestResp countFrequencyData(long createAt, GetElectricDataParam getElectricDataParam) {
        // 自定义的表格列
        TableFieldEnum[] fieldEnums = new TableFieldEnum[]{
                TableFieldEnum.LOOP_NAME,
                TableFieldEnum.COLECTION_TIME,
                TableFieldEnum.F_R
        };
        HashMap<String, List> map = null;
        try {
            map = this.countCommon(createAt, fieldEnums);
            List<MeterRecordDto> meterRecordList = map.get(DATA_KEY);
            List<BaseColumnItem> columnItemList = map.get(COLUMN_KEY);
            // 构造表格每一行的值
            List<JSONObject> dataList = new LinkedList<>();
            for (MeterRecordDto record : meterRecordList) {

                JSONObject dataItem = new JSONObject();

                String dataItemKey = TableFieldEnum.LOOP_NAME.getValue();
                dataItem.put(dataItemKey, "主进线柜");

                dataItemKey = TableFieldEnum.COLECTION_TIME.getValue();
                dataItem.put(dataItemKey, record.getCreateAt());

                // 暂时性使用这个方法
                dataItemKey = TableFieldEnum.F_R.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(record.getFrequency()));

                dataList.add(dataItem);
            }
            BaseTableResponse<JSONObject> baseTableResponse = new BaseTableResponse<JSONObject>();
            baseTableResponse.setColumnList(columnItemList);
            baseTableResponse.setData(dataList);
            return RestResp.createBySuccess(baseTableResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.createBy(RestResp.ERROR, "服务器内部错误");
        }
    }

    @Override
    public RestResp countDegreeOfThreePhaseUnbalanceData(long createAt, GetElectricDataParam getElectricDataParam) {
        // 自定义的表格列
        TableFieldEnum[] fieldEnums = new TableFieldEnum[]{
                TableFieldEnum.LOOP_NAME,
                TableFieldEnum.COLECTION_TIME,
                TableFieldEnum.I_UNB,
                TableFieldEnum.U_UNB
        };
        HashMap<String, List> map = null;
        try {
            map = this.countCommon(createAt, fieldEnums);
            List<MeterRecordDto> meterRecordList = map.get(DATA_KEY);
            List<BaseColumnItem> columnItemList = map.get(COLUMN_KEY);
            // 构造表格每一行的值
            List<JSONObject> dataList = new LinkedList<>();
            for (MeterRecordDto record : meterRecordList) {

                JSONObject dataItem = new JSONObject();

                String dataItemKey = TableFieldEnum.LOOP_NAME.getValue();
                dataItem.put(dataItemKey, "主进线柜");

                dataItemKey = TableFieldEnum.COLECTION_TIME.getValue();
                dataItem.put(dataItemKey, record.getCreateAt());

                dataItemKey = TableFieldEnum.I_UNB.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(MeterRecordCalculator.countCurrentThreePhaseUnbalanced(record)));
                dataItemKey = TableFieldEnum.U_UNB.getValue();
                dataItem.put(dataItemKey, decimalFormat.format(MeterRecordCalculator.countVoltageThreePhaseUnbalanced(record)));

                dataList.add(dataItem);
            }
            BaseTableResponse<JSONObject> baseTableResponse = new BaseTableResponse<JSONObject>();
            baseTableResponse.setColumnList(columnItemList);
            baseTableResponse.setData(dataList);
            return RestResp.createBySuccess(baseTableResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.createBy(RestResp.ERROR, "服务器内部错误");
        }
    }

    /**
     * 根据枚举数据产生BaseColumnItem列表
     *
     * @param fieldEnums
     * @return
     */
    private List<BaseColumnItem> produceColumnListAndIndex(TableFieldEnum[] fieldEnums) {
        int columnLength = fieldEnums.length;
        List<BaseColumnItem> columnItemList = new ArrayList<>(columnLength);
        for (TableFieldEnum fieldEnum : fieldEnums) {
            BaseColumnItem columnItem = new BaseColumnItem(fieldEnum.getName(), fieldEnum.getValue());
            columnItemList.add(columnItem);
        }
        return columnItemList;
    }


    @Override
    public RestResp countElectricEnergyData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countElectricEnergyDataRange(createAt, remain, getElectricDataParam);
    }

    @Override
    public RestResp countElectricEnergyDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<ElectricEnergyResponse> electricEnergyResponsesList = new ArrayList<>(meterRecordList.size());

        for (MeterRecord m : meterRecordList) {

            ElectricEnergyResponse electricEnergyResponse = new ElectricEnergyResponse();

            electricEnergyResponse.setCreateAt(m.getCreateAt());
            electricEnergyResponse.setElectricEnergy(m.getElectricEnergy());

            electricEnergyResponsesList.add(electricEnergyResponse);
        }

        return RestResp.createBySuccess(electricEnergyResponsesList);
    }

    @Override
    public RestResp countTemperatureData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countTemperatureDataRange(createAt, remain, getElectricDataParam);
    }

    @Override
    public RestResp countTemperatureDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<TemperatureResponse> temperatureResponsesList = new ArrayList<>(meterRecordList.size());

        for (MeterRecord m : meterRecordList) {

            TemperatureResponse temperatureResponse = new TemperatureResponse();

            temperatureResponse.setCreateAt(m.getCreateAt());
            temperatureResponse.setTemperature(m.getTemperature());

            temperatureResponsesList.add(temperatureResponse);
        }

        return RestResp.createBySuccess(temperatureResponsesList);
    }


    @Override
    public RestResp countActivePowerMaxAvgMin(long startAt, long endAt) {
        /**
         * 结束日期为当天的最后一刻，即前端的参数可能为2019-02-17 10:00:00
         * 需要修正为2019-02-17 23:59:59，所以需要增加毫秒数
         */
        endAt = endAt + this.getRemainMillisSecondsOneDay(endAt);

        Timestamp start = new Timestamp(startAt);
        Timestamp end = new Timestamp(endAt);
        List<MaxAvgMinDto> maxAvgMinDtoList = meterRecordRepository.findMaxAvgMinByPower(start, end);
        return RestResp.createBySuccess(maxAvgMinDtoList);
    }

    @Override
    public RestResp producePhaseVoltageReport(long createAt, int minuteInterval) {
        long endAt = createAt + this.getRemainMillisSecondsOneDay(createAt);

        Timestamp start = new Timestamp(createAt);
        Timestamp end = new Timestamp(endAt);
        List<PhaseVoltageReportDto> phaseVoltageReportDtoList = meterRecordRepository.findByCreateAtInterval(start, end, minuteInterval);
        List<PhaseReportItem> reportItemList = new LinkedList<>();
        try {
            for (PhaseVoltageReportDto dto : phaseVoltageReportDtoList) {
                PhaseReportItem item = new PhaseReportItem();
                // 合并同名成员变量
                AutoAssembleUtil.assembleSameNameField(item, dto);
                // TODO 计算其他相关数据

                //添加到用于返回给前端的数据结构中
                reportItemList.add(item);
            }
            List<PhaseReportItem> phaseReportItems = PaddingTimeUtil.paddingZeroBy(reportItemList, minuteInterval, "CreateAt", PhaseReportItem.class);
            ReportResponse<PhaseReportItem> reportResponse = new ReportResponse<PhaseReportItem>();
            reportResponse.setInnerItemList(phaseReportItems);
            return RestResp.createBySuccess(reportResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestResp.createBySuccess(reportItemList);
    }

    /**
     * 功率极值
     *
     * @param createAt
     * @return
     */
    @Override
    public RestResp producePowerLimitReport(long createAt, String createAtFormat, String sqlFormat) {
        // 将前端传来的时间戳转换为与sqlFormat格式相同的时间字符串格式
        String createAtString = DateFormatUtil.formatDateTo(createAt, createAtFormat);
        try {
            List<LimitMaxAvgMin> limitMaxAvgMinList = new LinkedList<>();

            // 查找有功功率的最大值最小值平均值并填充
            String dataType = "ActivePower";
            String listDesc = "有功功率";
            LimitMaxAvgMin limitMaxAvgMin = this.produceLimitItem(dataType, listDesc, sqlFormat, createAtString);
            limitMaxAvgMinList.add(limitMaxAvgMin);

            // 查找无功功率的最大值最小值平均值并填充
            dataType = "ReactivePower";
            listDesc = "无功功率";
            limitMaxAvgMin = this.produceLimitItem(dataType, listDesc, sqlFormat, createAtString);
            limitMaxAvgMinList.add(limitMaxAvgMin);

            // 查找视在功率的最大值最小值平均值并填充
            dataType = "ApparentPower";
            listDesc = "视在功率";
            limitMaxAvgMin = this.produceLimitItem(dataType, listDesc, sqlFormat, createAtString);
            limitMaxAvgMinList.add(limitMaxAvgMin);

            return RestResp.createBySuccess(limitMaxAvgMinList);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.createBy(RestResp.ERROR, "产生内部错误，来源：极值报表统计");
        }
    }

    @Override
    public RestResp produceElectricCurrentLimitReport(long createAt, String createAtFormat, String sqlFormat) {
        String createAtString = DateFormatUtil.formatDateTo(createAt, createAtFormat);
        try {
            List<LimitMaxAvgMin> limitMaxAvgMinList = new LinkedList<>();
            // 查找A相电流的最大值最小值平均值并填充
            String dataType = "Ia";
            String listDesc = "A相电流";
            LimitMaxAvgMin limitMaxAvgMin = this.produceLimitItem(dataType, listDesc, sqlFormat, createAtString);
            limitMaxAvgMinList.add(limitMaxAvgMin);

            // 查找B相电流的最大值最小值平均值并填充
            dataType = "Ib";
            listDesc = "B相电流";
            limitMaxAvgMin = this.produceLimitItem(dataType, listDesc, sqlFormat, createAtString);
            limitMaxAvgMinList.add(limitMaxAvgMin);

            // 查找C相电流的最大值最小值平均值并填充
            dataType = "Ic";
            listDesc = "C相电流";
            limitMaxAvgMin = this.produceLimitItem(dataType, listDesc, sqlFormat, createAtString);
            limitMaxAvgMinList.add(limitMaxAvgMin);
            //构造给前端的数据
            return RestResp.createBySuccess(limitMaxAvgMinList);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.createBy(RestResp.ERROR, "产生内部错误，来源：极值报表统计");
        }
    }

    @Override
    public RestResp producePhaseVoltageLimitReport(long createAt, String createAtFormat, String sqlFormat) {
        String createAtString = DateFormatUtil.formatDateTo(createAt, createAtFormat);
        try {
            List<LimitMaxAvgMin> limitMaxAvgMinList = new LinkedList<>();

            // 查找A相电压的最大值最小值平均值并填充
            String dataType = "Ua";
            String listDesc = "A相电压";
            LimitMaxAvgMin limitMaxAvgMin = this.produceLimitItem(dataType, listDesc, sqlFormat, createAtString);
            limitMaxAvgMinList.add(limitMaxAvgMin);

            // 查找B相电压的最大值最小值平均值并填充
            dataType = "Ub";
            listDesc = "B相电压";
            limitMaxAvgMin = this.produceLimitItem(dataType, listDesc, sqlFormat, createAtString);
            limitMaxAvgMinList.add(limitMaxAvgMin);

            // 查找C相电压的最大值最小值平均值并填充
            dataType = "Uc";
            listDesc = "C相电压";
            limitMaxAvgMin = this.produceLimitItem(dataType, listDesc, sqlFormat, createAtString);
            limitMaxAvgMinList.add(limitMaxAvgMin);

            //构造给前端的数据
            return RestResp.createBySuccess(limitMaxAvgMinList);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.createBy(RestResp.ERROR, "产生内部错误，来源：极值报表统计");
        }
    }

    @Override
    public RestResp produceLineVoltageLimitReport(long createAt, String createAtFormat, String sqlFormat) {
        String createAtString = DateFormatUtil.formatDateTo(createAt, createAtFormat);
        try {
            List<LimitMaxAvgMin> limitMaxAvgMinList = new LinkedList<>();

            // 响应对象和电表之间的索引，方便查找装载数据
            HashMap<String, LineVoltageReportResponse> lineVoltageReportHashMap = new HashMap<>();
            // 查找A相电压的最大值最小值平均值并填充
            String dataType = "Uab";
            String listDesc = "AB线电压";
            LimitMaxAvgMin limitMaxAvgMin = this.produceLimitItem(dataType, listDesc, sqlFormat, createAtString);
            limitMaxAvgMinList.add(limitMaxAvgMin);

            // 查找B相电压的最大值最小值平均值并填充
            dataType = "Ubc";
            listDesc = "BC线电压";
            limitMaxAvgMin = this.produceLimitItem(dataType, listDesc, sqlFormat, createAtString);
            limitMaxAvgMinList.add(limitMaxAvgMin);

            // 查找C相电压的最大值最小值平均值并填充
            dataType = "Uca";
            listDesc = "CA线电压";
            limitMaxAvgMin = this.produceLimitItem(dataType, listDesc, sqlFormat, createAtString);
            limitMaxAvgMinList.add(limitMaxAvgMin);

            //构造给前端的数据
            return RestResp.createBySuccess(limitMaxAvgMinList);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.createBy(RestResp.ERROR, "产生内部错误，来源：极值报表统计");
        }
    }


    /**
     * 利用dataType获取最大、最小、平均值，封装成LimitMaxAvgMin对象
     * <p>
     * 约定：
     * MeterRecordRepository内的方法名
     * 统一以 find[Max|Min|Avg] 开头
     * 中间为类型：如activePower代表有功功率
     * 统一以 ByCreateAt 结尾
     *
     * @param dataType       数据的类型
     * @param listDesc       对数据的中文描述
     * @param sqlFormat      sql时间格式
     * @param createAtString 查询时间
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private LimitMaxAvgMin produceLimitItem(String dataType, String listDesc, String sqlFormat, String createAtString) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String maxMethod = "findMax" + dataType + "ByCreateAt";
        String minMethod = "findMin" + dataType + "ByCreateAt";
        String avgMethod = "findAvg" + dataType + "ByCreateAt";
        List<LimitReportDto> maxList = (List<LimitReportDto>) invokeMeterRecordByTwoStringParam(maxMethod, sqlFormat, createAtString);
        List<LimitReportDto> minList = (List<LimitReportDto>) invokeMeterRecordByTwoStringParam(minMethod, sqlFormat, createAtString);
        List<LimitReportDto> avgList = (List<LimitReportDto>) invokeMeterRecordByTwoStringParam(avgMethod, sqlFormat, createAtString);
        List<LimitMaxAvgMinItem> itemList = this.assembleReportItemValue(maxList, avgList, minList);
        LimitMaxAvgMin limitMaxAvgMin = new LimitMaxAvgMin();
        limitMaxAvgMin.setListDesc(listDesc);
        limitMaxAvgMin.setList(itemList);
        return limitMaxAvgMin;
    }

    /**
     * 调用MeterRecordRepository中，参数为两个字符串的方法，根据methodName
     *
     * @param methodName
     * @param sqlFormat
     * @param createAtString
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object invokeMeterRecordByTwoStringParam(String methodName, String sqlFormat, String createAtString) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = MeterRecordRepository.class.getMethod(methodName, String.class, String.class);
        return method.invoke(meterRecordRepository, sqlFormat, createAtString);
    }

    private List<LimitMaxAvgMinItem> assembleReportItemValue(List<LimitReportDto> maxList,
                                                             List<LimitReportDto> avgList,
                                                             List<LimitReportDto> minList) {
        if (maxList.isEmpty()) return new ArrayList<>();
        HashMap<String, LimitMaxAvgMinItem> itemCache = new HashMap<>();
        int length = maxList.size();
        for (int i = 0; i < length; i++) {
            LimitReportDto maxDto = maxList.get(i);
            LimitReportDto avgDto = avgList.get(i);
            LimitReportDto minDto = minList.get(i);

            LimitMaxAvgMinItem item = this.produceItemByCache(itemCache, maxDto);
            item.setMaxValue(maxDto.getLimitValue());
            item.setMaxValueCreateAt(maxDto.getCreateAt());

            item = this.produceItemByCache(itemCache, minDto);
            item.setMinValue(minDto.getLimitValue());
            item.setMinValueCreateAt(minDto.getCreateAt());

            item = this.produceItemByCache(itemCache, avgDto);
            item.setAvgValue(avgDto.getLimitValue());
        }
        Iterator<String> iterator = itemCache.keySet().iterator();

        List<LimitMaxAvgMinItem> itemList = new ArrayList<>(itemCache.size());
        while (iterator.hasNext()) {
            itemList.add(itemCache.get(iterator.next()));
        }
        return itemList;
    }

    private LimitMaxAvgMinItem produceItemByCache(HashMap<String, LimitMaxAvgMinItem> itemCache, LimitReportDto dto) {
        LimitMaxAvgMinItem item = itemCache.get(dto.getMeter());
        if (item == null) {
            item = new LimitMaxAvgMinItem();
            item.setMeter(dto.getMeter());
            itemCache.put(dto.getMeter(), item);
        }
        return item;
    }

    private long getRemainMillisSecondsOneDay(long currentTimestamp) {
        LocalDateTime midnight = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimestamp),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimestamp),
                ZoneId.systemDefault());
        return ChronoUnit.MILLIS.between(currentDateTime, midnight);
    }

    private List<MeterRecordDto> getMeterRecordDtoListByCreateAt(String createAt) {
        return meterRecordRepository.findByCreateAtAndMinuteInterval(createAt, 5);
    }

    private List<MeterRecord> getMeterRecordList(long startAt, long endAt) {
        return meterRecordRepository.findByCreateAtGreaterThanEqualAndCreateAtLessThanEqual(new Timestamp(startAt), new Timestamp(endAt));
    }
}
