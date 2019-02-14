package power.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import power.api.model.MeterRecord;

public interface MeterRecordRepository extends JpaRepository<MeterRecord, Integer> {
}
