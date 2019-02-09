package power.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import power.api.model.User;
import power.api.repository.UserRepository;

/**
 * Created by 浩发 on 2019/2/7 09:55
 */
@Service
public class MyUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在！");
        }
        UserDetail userDetail = new UserDetail(user.getId(), user.getMobile(), user.getUsername());
        return userDetail;
    }

}