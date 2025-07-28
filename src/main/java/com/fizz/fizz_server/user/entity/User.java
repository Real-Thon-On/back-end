package com.fizz.fizz_server.user.entity;


import com.fizz.fizz_server.global.base.domain.BaseEntity;
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

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    public void update(UserRequestDto dto) {
        if (dto.getUsername() != null) {
            this.username = dto.getUsername();
        }
        if (dto.getPassword() != null) {
            this.password = dto.getPassword();
        }
    }

}
