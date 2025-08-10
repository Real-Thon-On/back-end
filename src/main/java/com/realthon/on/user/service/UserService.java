package com.realthon.on.user.service;

import com.realthon.on.global.base.response.exception.BusinessException;
import com.realthon.on.global.base.response.exception.ExceptionType;
import com.realthon.on.security.jwt.enums.Role;
import com.realthon.on.user.dto.request.UserUpdateRequestDto;
import com.realthon.on.user.entity.User;
import com.realthon.on.user.dto.request.SignUpRequestDto;
import com.realthon.on.user.dto.response.SignUpResponseDto;
import com.realthon.on.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public SignUpResponseDto signup(SignUpRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException(ExceptionType.DUPLICATED_USERNAME);
        }

        // 비밀번호 암호화 및 저장
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = request.toEntity();
        user = userRepository.save(user);

        return SignUpResponseDto.fromEntity(user);
    }

    @Transactional
    public SignUpResponseDto completeSignup(Long userId, UserUpdateRequestDto request) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(()->new BusinessException(ExceptionType.USER_NOT_FOUND));

        if (user.getRole() != Role.NOT_REGISTERED) {
            throw new BusinessException(ExceptionType.ALREADY_REGISTERED_USER);
        }

        // 최종 회원 가입을 위한 추가 작업 정의
        user.update(request);

        // USER 권한으로 승격
        user.updateRole(Role.USER);

        return SignUpResponseDto.fromEntity(user);
    }





}