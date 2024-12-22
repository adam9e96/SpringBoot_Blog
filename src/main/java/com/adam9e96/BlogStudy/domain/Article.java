package com.adam9e96.BlogStudy.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 블로그 게시물을 나타내는 엔티티입니다.
 *
 * <p>이 클래스는 JPA를 사용하여 데이터베이스의 {@code articles} 테이블과 매핑됩니다.
 * 게시물의 제목과 내용 필드, 생성 및 수정 타임스탬프를 포함합니다.</p>
 *
 *
 * <p>
 * <strong>주요 기능:</strong>
 * <ul>
 *   <li>게시물의 제목과 내용을 저장</li>
 *   <li>게시물의 고유 식별자(ID)를 자동 생성</li>
 *   <li>Lombok을 사용하여 보일러플레이트 코드 감소</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>주의 사항:</strong>
 *       기본 생성자는 PROTECTED 수준으로 제한되어 있어, 외부에서 직접 인스턴스를 생성할 수 없습니다.
 *       대신 {@link #builder()} 메서드를 사용하여 객체를 생성해야 합니다.
 * </p>
 *
 * @author adam9e96
 * @version 1.0
 */
@Entity // 엔티티로 지정하여 JPA가 관리하도록 함
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 PROTECTED 로 설정
@Getter
@EntityListeners(AuditingEntityListener.class) // 블로그 글 뷰 구현 (생성시간, 수정시간 관련)
public class Article {

    /**
     * 게시물의 고유 식별자입니다.
     * <p>
     * 데이터베이스의 기본 키로 사용되며, 자동으로 1씩 증가합니다.
     * </p>
     */
    @Id // id 필드를 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키를 자동으로 1씩 증가
    @Column(name = "id", updatable = false) // 컬럼 이름과 속성 지정
    private Long id; // 일련번호, 기본키

    /**
     * 게시물의 제목입니다.
     * <p>
     * 이 필드는 {@code null} 값을 가질 수 없으며, 데이터베이스의 {@code title} 컬럼에 매핑됩니다.
     * </p>
     */
    @Column(name = "title", nullable = false)
    private String title; // 게시물의 제목

    /**
     * 게시물의 내용입니다.
     * <p>
     * 이 필드는 {@code null} 값을 가질 수 없으며, 데이터베이스의 {@code content} 컬럼에 매핑됩니다.
     * </p>
     */
    @Column(name = "content", nullable = false)
    private String content; // 내용


    /**
     * 게시물이 생성된 타임스탬프입니다.
     * 엔티티가 저장될 때 자동으로 설정됩니다.
     */
    @CreatedDate // 엔티티가 생성될 때 생성 시간 저장
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 게시물이 마지막으로 수정된 타임스탬프입니다.
     * 엔티티가 수정될 때 자동으로 업데이트됩니다.
     */
    @LastModifiedDate // 엔티티가 수정될 때 수정 시간 저장
    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    /**
     * 빌더 패턴을 사용하여 {@code Article} 인스턴스를 생성합니다.
     *
     * @param title   게시물의 제목
     * @param content 게시물의 내용
     */
    @Builder
    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /**
     * 게시물의 제목과 내용을 업데이트합니다.
     *
     * @param title   새로운 제목
     * @param content 새로운 내용
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }


}
