package com.fizz.fizz_server.security.jwt.principal;


import com.fizz.fizz_server.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 자체로그인만을 위한 principal 구현체
 */
//@Getter
//@Builder
//public class CustomUserDetails implements UserDetails {
//    private final User user;
//    private final Collection<? extends GrantedAuthority> authorities;
//
//    public CustomUserDetails(User user) {
//        this.user = user;
//        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole().getKey()));
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUsername();
//    }
//
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//
//    @Override public boolean isAccountNonExpired() { return true; }
//    @Override public boolean isAccountNonLocked() { return true; }
//    @Override public boolean isCredentialsNonExpired() { return true; }
//    @Override public boolean isEnabled() { return true; }
//
//}
//
//
