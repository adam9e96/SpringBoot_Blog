package com.adam9e96.BlogStudy.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * 스프링 시큐리티의 인증에 필요한 사용자 정보를 로드하는 서비스<br>
 * UserDetailsService 인터페이스를 구현하여 인증 시 사용자 정보를 제공
 * <p>
 * 로그인할때 사용됨(회원가입때는 사용안됨)
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    // 사용자 이름(email)으로 사용자의 정보를 가져오는 메서드

    /**
     * 사용자 이메일(username)로 사용자 정보를 조회
     * <p>
     * 처리 과정:
     * 1. UserRepository 를 통해 DB에서 이메일로 사용자 검색
     * 2. 사용자가 존재하면 UserDetails 객체로 변환하여 반환
     * 3. 사용자가 존재하지 않으면 예외 발생
     * <p>
     * ====== 추가 설명 ======
     * 반환된 UserDetails 객체에 포함된 정보
     * - 사용자 이메일
     * - 암호화된 비밀번호
     * - 권한 정보
     * - 계정 상태 정보(잠금, 만료 등)
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        log.info("=== 사용자 인증 프로세스 시작 ===");
        log.info("이메일로 사용자 정보 조회 중... email: {}", email);

        UserDetails userDetails = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없음: {}", email);
                    return new IllegalArgumentException(email + "에 해당하는 사용자가 없습니다.");
                });

        log.info("사용자 정보 조회 완료: {}", email);
        log.info("저장된 암호화된 비밀번호: {}", userDetails.getPassword());
        log.info("=== 사용자 인증 프로세스 종료 ===");

        return userDetails;
    }
}