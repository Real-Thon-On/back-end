package com.fizz.fizz_server.user.entity;


import com.fizz.fizz_server.global.base.domain.BaseEntity;
import com.fizz.fizz_server.security.common.enums.Role;
import com.fizz.fizz_server.security.jwt.entity.RefreshToken;
import com.fizz.fizz_server.security.oauth2.enums.ProviderType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User extends BaseEntity {

    /**
     * 우리 애플리케이션 상의 (물리적) 식별자값
     * 자체로그인, OAuth2 공통
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * OAuth2 provider 벤더명 (KAKAO, NAVER, GOOGLE)
     * 자체 로그인일 경우 null
     */
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    /**
     * OAuth2 provider 상의 식별자 값
     * 자체 로그인일 경우 null
     */
    private String providerId;



    /**
     * 자체로그인 논리적 식별자값
     * OAuth2 로그인일경우 null
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * 자체로그인 비밀번호
     * OAuth2 로그인일경우 null
     */
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // 리프레시 토큰
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    // 닉네임. 사용자명
    private String nickname;
    private String email;





    // 비밀번호 암호화
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    // TODO: update 메서드 추가
//    public void update(UserUpdateRequestDto dto) {
//        if (dto.getUsername() != null) {
//            this.username = dto.getUsername();
//        }
//        if (dto.getPassword() != null) {
//            this.password = dto.getPassword();
//        }
//    }


}
