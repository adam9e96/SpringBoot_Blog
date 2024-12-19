package com.adam9e96.BlogStudy.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <h1>DAO</h1>
 * {@code Article} 클래스는 블로그 게시물을 나타내는 엔티티입니다.
 * <p>
 * 이 클래스는 JPA를 사용하여 데이터베이스의 {@code articles} 테이블과 매핑됩니다.
 * 각 인스턴스는 하나의 게시물을 나타내며, 게시물의 제목과 내용을 포함합니다.
 * </p>
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
 * <ul>
 *   <li>기본 생성자는 {@link AccessLevel#PROTECTED}로 제한되어 있어, 외부에서 직접 인스턴스를 생성할 수 없습니다.
 *       대신 {@link #builder()} 메서드를 사용하여 객체를 생성해야 합니다.</li>
 * </ul>
 * </p>
 *
 * @author adam9e96
 * @version 1.0
 */
@Entity // 엔티티로 지정하여 JPA가 관리하도록 함
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 PROTECTED 로 설정
@Getter
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
     * {@code Article} 클래스의 빌더 패턴을 사용한 생성자입니다.
     * <p>
     * 이 생성자는 Lombok의 {@link Builder} 어노테이션에 의해 자동 생성됩니다.
     * </p>
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
     * 블로그글 수정을 위한 메서드 : update()
     * @param title
     * @param content
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //    protected Article() { // 기본 생성자
    //    }
}
