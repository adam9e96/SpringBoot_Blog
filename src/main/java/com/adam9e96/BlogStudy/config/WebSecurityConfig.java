package com.adam9e96.BlogStudy.config;

import com.adam9e96.BlogStudy.repository.UserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

/**
 * <h1>WebSecurityConfig</h1>
 * 스프링 시큐리티 설정 클래스입니다.
 * - 스프링 시큐리티의 필터 체인 설정
 * - 인증 관리자 및 패스워드 인코더 설정
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userDetailService;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    // authorizeRequests() 대신 authorizeHttpRequests() 사용

    /**
     * 스프링 시큐리티의 필터 체인 설정
     * 인증 / 인가 처리
     * 로그인 성공 / 실패 시 리다이렉션 처리
     * <p>
     * 스프링 시큐리티는 AUthenticationManager 를 통해 인증을 처리하는데 내부적으로 빈으로 설정되어잇으면 명시하지 않아도 됩닏.
     * Spring Security의 자동 구성:
     * `@Bean`으로 설정한 AuthenticationManager는 자동으로 Spring의 ApplicationContext에 등록됨
     * SecurityFilterChain이 필요할 때 이 AuthenticationManager를 찾아서 사용
     * 현재는 .authenticationManager() 메소드를 통해 명시적으로 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authenticationManager(authenticationManager(http, bCryptPasswordEncoder(), userDetailService))
                .authorizeHttpRequests(auth -> auth // 인증, 인가 설정
                        .requestMatchers( // /login, /signup, /user 경로는 인증없이 접근가능
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/signup"),
                                new AntPathRequestMatcher("/user")
                        ).permitAll()
                        .anyRequest().authenticated())// 위에서 명시한 경로를 제외한 모든 요청은 인증이 필요하도록 설정(/articles/**도 포함)
                // formLogin() 메소드를 호출할때 자동으로 UsernamePasswordAuthenticationFilter 가 등록됨)
                .formLogin(formLogin -> formLogin // 폼 기반 로그인 설정
                        .loginPage("/login")
                        .defaultSuccessUrl("/articles")
                        .successHandler((request, response, authentication) -> {  // 추가된 부분
                            log.info("=== 로그인 성공 ===");
                            log.info("인증 객체: {}", authentication);
                            log.info("Principal: {}", authentication.getPrincipal());
                            log.info("Credentials: {}", authentication.getCredentials()); // null 이 찍힘
                            log.info("Authorities: {}", authentication.getAuthorities());
                            log.info("Details: {}", authentication.getDetails());
                            log.info("=================");
                            response.sendRedirect("/articles");
                        })
                )
                .logout(logout -> logout // 로그아웃 설정
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                )
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .build();
    }

    /**
     * 실제 인증을 처리하는 AuthenticationManager 설정
     * <p>
     * 인증 처리 과정:
     * 1. DaoAuthenticationProvider 가 UserDetailService 로부터 사용자 정보 조회
     * - loadUserByUsername() 메소드 호출
     * - DB에서 사용자 정보를 조회
     * - UserDetails 객체로 변환하여 반환
     * <p>
     * 2. 입력받은 비밀번호와 저장된 비밀번호를 BCryptPasswordEncoder 를 통해 검증
     * - 비밀번호 일치시 -> 인증 성공
     * - 비밀번호 불일치시 -> 인증 실패
     * <p>
     * 여기서 중요한 점은 비밀번호는 검증 시 복호화(암호 해독) 되지 않습니다.
     * 사용자가 로그인 시도 시:
     * - UserDetailService 가 이메일로 사용자 정보를 DB에서 조회
     * - 입력받은 비밀번호를 BCrypt 로 암호화
     * - 암호화된 결과와 DB에 저장된 암호화된 비밀번호를 비교
     * - 일치하면 로그인 성공, 불일치하면 실패
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailService userDetailService) throws Exception {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
// DB에서 유저 정보를 가져오는 서비스 설정
        authProvider.setUserDetailsService(userDetailService); // 사용자 정보 가져오는 서비스 설정
        authProvider.setPasswordEncoder(bCryptPasswordEncoder); // 패스워드 인코더 설정
        return new ProviderManager(authProvider);
    }

    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
