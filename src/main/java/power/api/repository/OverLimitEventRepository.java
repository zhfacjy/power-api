package power.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import power.api.model.OverLimitEvent;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * Created by 浩发 on 2019/2/4 10:45
 */
@Repository
public interface OverLimitEventRepository extends JpaRepository<OverLimitEvent, Integer> {

    @Modifying
    @Transactional
    @Query(value = "update OverLimitEvent set endDate = ?1 where id = ?2")
    void updateEndDateById(Date endDate,Integer id);

}
