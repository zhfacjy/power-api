package power.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import power.api.dto.MaxAvgMinDto;
import power.api.model.MeterRecord;

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
     * 计算直嘀咕
     *
     * @param startAt
     * @param endAt
     * @return
     */
    @Query(value = "select " +
            "new power.api.dto.MaxAvgMinDto(" +
            "    max(mr.activePower)," +
            "    avg(mr.activePower)," +
            "    min(mr.activePower), " +
            "    function('date_format', mr.createAt, '%Y-%m-%d') ) " +
            "from MeterRecord mr " +
            "where mr.createAt >= ?1 and mr.createAt <= ?2 " +
            "group by " +
            "    function('date_format', mr.createAt, '%Y-%m-%d')")
    List<MaxAvgMinDto> findMaxAvgMinByPower(Timestamp startAt, Timestamp endAt);
}
