package com.adam9e96.BlogStudy.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 사용자 정보를 담당하는 도메인 클래스
 * UserDetails 인터페이스를 구현하여 사용자 정보를 담당
 */
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails { // UserDetails 를 상속받아 인증 객체로 사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Builder
    public User(String email, String password, String auth) {
        this.email = email;
        this.password = password;
    }

    /**
     * 스프링 시큐리티는 사용자 권한을 문자열로 관리하지 않고 GrantedAuthority 객체로 관리합니다.
     * 현재는 단일 권한 "user"만 부여 하고있습니다.
     *
     * @return
     */
    @Override // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }


    // ===== 인증에 필요한 메서드 : getPassword(), getUsername() =====

    // 사용자 패스워드 반환
    @Override
    public String getPassword() {
        return password;
    }

    // 사용자 id를 반환(고유한 값)
    @Override
    public String getUsername() {
        return email;
    }

    // ===== 계정 상태 관련 메서드 : isAccountNonExpired(), isAccountNonLocked(), isCredentialsNonExpired(), isEnabled() =====
    // 현재는 모두 true로 설정하여 인증에 별다른 제약을 두지 않았습니다.
    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠기지 않음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true; // true -> 사용 가능
    }
}
