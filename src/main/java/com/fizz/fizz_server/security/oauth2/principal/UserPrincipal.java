package com.fizz.fizz_server.security.oauth2.principal;

import com.fizz.fizz_server.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class UserPrincipal implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes; // provider 가 넘겨준 전체 정보들, key-value 구조
    private String userNameAttributeName; // google : sub, kakako : id .. 등 식별자 키 명

    // User 엔티티로부터 UserPrincipal 를 생성하는 생성자
    public UserPrincipal(User user) {
        this.user = user;
    }


    @Override
    public String getName() {
        return user.getIdentifier();
    } // provider 상의 식별자 값 반환


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getKey()));
    }


    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(user.getId());
    } // 애플리케이션 상의 식별자값 반환

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
