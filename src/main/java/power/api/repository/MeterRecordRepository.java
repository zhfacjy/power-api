package power.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import power.api.dto.LimitReportDto;
import power.api.dto.MaxAvgMinDto;
import power.api.dto.PhaseVoltageReportDto;
import power.api.model.MeterRecord;

import javax.persistence.SqlResultSetMapping;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 数据库表meter_record主要记录是电表发来的数据
 */
public interface MeterRecordRepository extends JpaRepository<MeterRecord, Integer> {
    /**
     * Spring Data JPA语法，指定meter_record中的createAt在一个区间范围内的数据
     *
     * @param startAt
     * @param endAt
     * @return
     */
    List<MeterRecord> findByCreateAtGreaterThanEqualAndCreateAtLessThanEqual(Timestamp startAt, Timestamp endAt);

    /**
     * 计算有功功率的最大值、平均值、最小值
     * 按照日期分组
     * 条件为日期区间
     * <p>
     * 其中的MaxAvgMinDto为自定义接口，用于装载查询结果
     * AS 后面跟着的字段名 与dto中的方法名对应
     *
     * @param startAt
     * @param endAt
     * @return
     */
    @Query(value = "SELECT " +
            "  DATE_FORMAT(mr.create_at, '%Y-%m-%d') AS createAt, " +
            "  MAX(mr.active_power)                  AS maxHolder, " +
            "  AVG(mr.active_power)                  AS avgHolder, " +
            "  MIN(mr.active_power)                  AS minHolder " +
            "FROM meter_record mr " +
            "WHERE mr.create_at >= :startAt " +
            "      AND mr.create_at <= :endAt " +
            "GROUP BY createAt", nativeQuery = true)
    List<MaxAvgMinDto> findMaxAvgMinByPower(@Param("startAt") Timestamp startAt, @Param("endAt") Timestamp endAt);


    /**
     * 获取指定日期内，间隔minuteInterval分钟的平均数据
     *
     * @param startAt
     * @param endAt
     * @param minuteInterval
     * @return
     */
    @Query(value = "SELECT " +
            "  AVG(mr.va) AS ua, " +
            "  AVG(mr.vb) AS ub, " +
            "  AVG(mr.vc) AS uc, " +
            "  AVG(mr.ia) AS ia, " +
            "  AVG(mr.ib) AS ib, " +
            "  AVG(mr.ic) AS ic, " +
            "  AVG(mr.active_power) AS activePower, " +
            "  date_add(:startAt, INTERVAL :minuteInterval * floor(timestampdiff(MINUTE, :startAt, mr.create_at) / :minuteInterval) MINUTE) createAt " +
            "FROM meter_record mr " +
            "  WHERE mr.create_at >= :startAt " +
            "        AND mr.create_at < :endAt " +
            "GROUP BY floor(timestampdiff(MINUTE, :startAt, mr.create_at) / :minuteInterval)", nativeQuery = true)
    List<PhaseVoltageReportDto> findByCreateAtInterval(@Param("startAt") Timestamp startAt,
                                                       @Param("endAt") Timestamp endAt,
                                                       @Param("minuteInterval") int minuteInterval);

    @Query(value = "SELECT " +
            "  m1.meter        AS                             meter, " +
            "  m1.active_power AS                             limitValue, " +
            "  DATE_FORMAT(m1.create_at, '%Y-%m-%d %H:%i:%S') createAt " +
            "FROM meter_record AS m1 INNER JOIN ( " +
            "                                     SELECT " +
            "                                       meter, " +
            "                                       MAX(active_power) AS max_active_power " +
            "                                     FROM meter_record " +
            "                                     WHERE create_at >= :startAt " +
            "                                           AND create_at < :endAt " +
            "                                     GROUP BY meter " +
            "                                   ) AS m2 ON m1.meter = m2.meter AND m1.active_power = m2.max_active_power;", nativeQuery = true)
    List<LimitReportDto> getMaxActivePowerByCreateAt(@Param("startAt") Timestamp startAt,
                                                     @Param("endAt") Timestamp endAt);

    @Query(value = "SELECT " +
            "  a.meter        AS                             meter, " +
            "  a.active_power AS                             limitValue, " +
            "  DATE_FORMAT(b.create_at, '%Y-%m-%d %H:%i:%S') createAt " +
            "FROM ( " +
            "       SELECT " +
            "         MAX(active_power) AS active_power, " +
            "         meter " +
            "       FROM meter_record " +
            "       WHERE DATE_FORMAT(create_at, :format) = :createAt " +
            "       GROUP BY meter) AS a " +
            "  LEFT JOIN meter_record b ON a.active_power = b.active_power " +
            "                              AND DATE_FORMAT(b.create_at, :format) = :createAt AND b.meter = a.meter;", nativeQuery = true)
    List<LimitReportDto> findMaxActivePowerByCreateAt(@Param("format") String format,
                                                      @Param("createAt") String createAt);

    @Query(value = "SELECT " +
            "  a.meter        AS                             meter, " +
            "  a.active_power AS                             limitValue, " +
            "  DATE_FORMAT(b.create_at, '%Y-%m-%d %H:%i:%S') createAt " +
            "FROM ( " +
            "       SELECT " +
            "         MIN(active_power) AS active_power, " +
            "         meter " +
            "       FROM meter_record " +
            "       WHERE DATE_FORMAT(create_at, :format) = :createAt " +
            "       GROUP BY meter) AS a " +
            "  LEFT JOIN meter_record b ON a.active_power = b.active_power " +
            "                              AND DATE_FORMAT(b.create_at, :format) = :createAt AND b.meter = a.meter;", nativeQuery = true)
    List<LimitReportDto> findMinActivePowerByCreateAt(@Param("format") String format,
                                                      @Param("createAt") String createAt);

    @Query(value = "SELECT " +
            "  meter, " +
            "  AVG(active_power)                  AS limitValue, " +
            "  DATE_FORMAT(create_at, '%Y-%m-%d') AS createAt " +
            "FROM meter_record " +
            "WHERE DATE_FORMAT(create_at, :format) = :createAt " +
            "GROUP BY meter;", nativeQuery = true)
    List<LimitReportDto> findAvgActivePowerByCreateAt(@Param("format") String format,
                                                      @Param("createAt") String createAt);


    @Query(value = "SELECT " +
            "  m1.meter              AS                       meter, " +
            "  m1.max_reactive_power AS                       limitValue, " +
            "  DATE_FORMAT(m2.create_at, '%Y-%m-%d %H:%i:%S') createAt " +
            "FROM ( " +
            "       SELECT " +
            "         m2.meter, " +
            "         MAX((m2.ia * m2.va + m2.ib * m2.vb + m2.ic * m2.vc) * (-cos((pi() / 2) + DEGREES( " +
            "             ACOS(m2.active_power / ((m2.ia * m2.va + m2.ib * m2.vb + " +
            "                                      m2.ic * m2.vc))))))) AS max_reactive_power " +
            "       FROM meter_record AS m2 " +
            "       WHERE DATE_FORMAT(create_at, :format) = :createAt " +
            "       GROUP BY meter) AS m1 LEFT JOIN meter_record m2 ON m1.meter = m2.meter AND m1.max_reactive_power = " +
            "                                                                                  (m2.ia * m2.va + m2.ib * m2.vb + " +
            "                                                                                   m2.ic * m2.vc) * " +
            "                                                                                  (-cos((pi() / 2) + DEGREES( " +
            "                                                                                      ACOS(m2.active_power / (( " +
            "                                                                                        m2.ia * m2.va + m2.ib * m2.vb + " +
            "                                                                                        m2.ic * m2.vc))))));", nativeQuery = true)
    List<LimitReportDto> findMaxReactivePowerByCreateAt(@Param("format") String format,
                                                        @Param("createAt") String createAt);

    @Query(value = "SELECT " +
            "  m1.meter              AS                       meter, " +
            "  m1.max_reactive_power AS                       limitValue, " +
            "  DATE_FORMAT(m2.create_at, '%Y-%m-%d %H:%i:%S') createAt " +
            "FROM ( " +
            "       SELECT " +
            "         m2.meter, " +
            "         MIN((m2.ia * m2.va + m2.ib * m2.vb + m2.ic * m2.vc) * (-cos((pi() / 2) + DEGREES( " +
            "             ACOS(m2.active_power / ((m2.ia * m2.va + m2.ib * m2.vb + " +
            "                                      m2.ic * m2.vc))))))) AS max_reactive_power " +
            "       FROM meter_record AS m2 " +
            "       WHERE DATE_FORMAT(create_at, :format) = :createAt " +
            "       GROUP BY meter) AS m1 LEFT JOIN meter_record m2 ON m1.meter = m2.meter AND m1.max_reactive_power = " +
            "                                                                                  (m2.ia * m2.va + m2.ib * m2.vb + " +
            "                                                                                   m2.ic * m2.vc) * " +
            "                                                                                  (-cos((pi() / 2) + DEGREES( " +
            "                                                                                      ACOS(m2.active_power / (( " +
            "                                                                                        m2.ia * m2.va + m2.ib * m2.vb + " +
            "                                                                                        m2.ic * m2.vc))))));", nativeQuery = true)
    List<LimitReportDto> findMinReactivePowerByCreateAt(@Param("format") String format,
                                                        @Param("createAt") String createAt);

    @Query(value = "SELECT " +
            "  m2.meter                                          AS meter, " +
            "  AVG((m2.ia * m2.va + m2.ib * m2.vb + m2.ic * m2.vc) * (-cos((pi() / 2) + DEGREES( " +
            "      ACOS(m2.active_power / ((m2.ia * m2.va + m2.ib * m2.vb + " +
            "                               m2.ic * m2.vc))))))) AS limitValue, " +
            "  DATE_FORMAT(m2.create_at, '%Y-%m-%d')             AS createAt " +
            "FROM meter_record AS m2 " +
            "WHERE DATE_FORMAT(create_at, :format) = :createAt " +
            "GROUP BY meter;", nativeQuery = true)
    List<LimitReportDto> findAvgReactivePowerByCreateAt(@Param("format") String format,
                                                        @Param("createAt") String createAt);

    @Query(value = "SELECT " +
            "  m1.meter AS                                    meter, " +
            "  m1.power AS                                    limitValue, " +
            "  DATE_FORMAT(m2.create_at, '%Y-%m-%d %H:%i:%S') createAt " +
            "FROM ( " +
            "       SELECT " +
            "         m2.meter, " +
            "         MAX(m2.ia * m2.va + m2.ib * m2.vb + m2.ic * m2.vc) AS power " +
            "       FROM meter_record AS m2 " +
            "       WHERE DATE_FORMAT(create_at, :format) = :createAt " +
            "       GROUP BY meter) AS m1 LEFT JOIN meter_record m2 ON m1.meter = m2.meter " +
            "                                                          AND m1.power = (m2.ia * m2.va " +
            "                                                                          + m2.ib * m2.vb " +
            "                                                                          + m2.ic * m2.vc);", nativeQuery = true)
    List<LimitReportDto> findMaxApparentPowerByCreateAt(@Param("format") String format,
                                                        @Param("createAt") String createAt);

    @Query(value = "SELECT " +
            "  m1.meter AS                                    meter, " +
            "  m1.power AS                                    limitValue, " +
            "  DATE_FORMAT(m2.create_at, '%Y-%m-%d %H:%i:%S') createAt " +
            "FROM ( " +
            "       SELECT " +
            "         m2.meter, " +
            "         MIN(m2.ia * m2.va + m2.ib * m2.vb + m2.ic * m2.vc) AS power " +
            "       FROM meter_record AS m2 " +
            "       WHERE DATE_FORMAT(create_at, :format) = :createAt " +
            "       GROUP BY meter) AS m1 LEFT JOIN meter_record m2 ON m1.meter = m2.meter " +
            "                                                          AND m1.power = (m2.ia * m2.va " +
            "                                                                          + m2.ib * m2.vb " +
            "                                                                          + m2.ic * m2.vc);", nativeQuery = true)
    List<LimitReportDto> findMinApparentPowerByCreateAt(@Param("format") String format,
                                                        @Param("createAt") String createAt);


    @Query(value = "SELECT " +
            "  m2.meter                                           AS meter, " +
            "  AVG(m2.ia * m2.va + m2.ib * m2.vb + m2.ic * m2.vc) AS limitValue, " +
            "  DATE_FORMAT(m2.create_at, '%Y-%m-%d')              AS createAt " +
            "FROM meter_record AS m2 " +
            "WHERE DATE_FORMAT(create_at, :format) = :createAt " +
            "GROUP BY meter;", nativeQuery = true)
    List<LimitReportDto> findAvgApparentPowerByCreateAt(@Param("format") String format,
                                                        @Param("createAt") String createAt);
}
