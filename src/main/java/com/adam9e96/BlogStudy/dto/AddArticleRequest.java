package com.adam9e96.BlogStudy.dto;

import com.adam9e96.BlogStudy.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <h1>DTO</h1>
 */
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
public class AddArticleRequest {
    private String title;
    private String content;

    /**
     * 빌더 패턴을 사용해 DTO를 엔티티로 만들어주는 메서드
     * 추후 블로그 글을 추가할때 저장할 엔티티로 변환하는 용도로 사용합니다.
     */
    public Article toEntity() {
        return Article.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
