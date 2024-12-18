package com.adam9e96.BlogStudy.service;

import com.adam9e96.BlogStudy.domain.Article;
import com.adam9e96.BlogStudy.dto.AddArticleRequest;
import com.adam9e96.BlogStudy.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor // final 이 붙거나 @NotNull 이 붙은 필드의 생성자 추가
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    /**
     * 블로그 글 목록 조회를 위한 API
     * JPA 지원 메서드인 findAll() 을 호출해 article 테이블에 저장되어 있는
     * 모든 데이터를 조회합니다.
     */
    public List<Article> findAll() {
        return blogRepository.findAll();
    }
}
