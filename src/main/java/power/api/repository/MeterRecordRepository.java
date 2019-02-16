package power.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import power.api.model.MeterRecord;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 数据库表meter_record主要记录是电表发来的数据
 */
public interface MeterRecordRepository extends JpaRepository<MeterRecord, Integer> {
    List<MeterRecord> findByCreateAtGreaterThanEqualAndCreateAtLessThanEqual(Timestamp startAt, Timestamp endAt);

    List<MeterRecord> findByCreateAtBetween(Timestamp startAt, Timestamp endAt);
}
