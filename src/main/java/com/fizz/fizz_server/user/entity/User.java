package com.fizz.fizz_server.user.entity;


import com.fizz.fizz_server.global.base.domain.BaseEntity;
import com.fizz.fizz_server.security.common.enums.ProviderInfo;
import com.fizz.fizz_server.security.common.enums.Role;
import com.fizz.fizz_server.user.dto.request.UserRequestDto;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // OAuth2 provider 상의 식별자값
    private String identifier;

    // OAuth2 provider enum (google, naver 등)
    @Enumerated(EnumType.STRING)
    private ProviderInfo providerInfo;


    @Enumerated(EnumType.STRING)
    private Role role;



    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;




    // TODO: update 메서드 추가
    public void update(UserRequestDto dto) {
        if (dto.getUsername() != null) {
            this.username = dto.getUsername();
        }
        if (dto.getPassword() != null) {
            this.password = dto.getPassword();
        }
    }

}
