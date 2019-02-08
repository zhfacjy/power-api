package power.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import power.api.model.User;

/**
 * Created by 浩发 on 2019/2/7 09:22
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByMobile(String mobile);

}
