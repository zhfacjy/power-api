package power.api.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by 浩发 on 2019/2/7 22:10
 */
public class UserDetail implements UserDetails {

    private Integer userId;
    private String mobile;
    private String username;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userNo) {
        this.userId = userNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public UserDetail(Integer userId,String mobile,String username) {
        this.userId = userId;
        this.mobile = mobile;
        this.username = username;
    }
}