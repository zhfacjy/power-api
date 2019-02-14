package power.api.repository;

import org.springframework.stereotype.Repository;
import power.api.model.Dict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 浩发 on 2019/2/4 10:45
 */
@Repository
public interface DictRepository extends JpaRepository<Dict, Integer> {
    List<Dict> findAllByType(String type);
}
