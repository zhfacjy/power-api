package power.fucker.repository;

import org.springframework.stereotype.Repository;
import power.fucker.model.Dict;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 浩发 on 2019/2/4 10:45
 */
@Repository
public interface DictRepository extends JpaRepository<Dict, Integer> {

}
