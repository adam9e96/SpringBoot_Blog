package com.adam9e96.BlogStudy.service;

import com.adam9e96.BlogStudy.domain.Article;
import com.adam9e96.BlogStudy.dto.AddArticleRequest;
import com.adam9e96.BlogStudy.dto.UpdateArticleRequest;
import com.adam9e96.BlogStudy.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor // final 이 붙거나 @NotNull 이 붙은 필드의 생성자 추가
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    /**
     * 블로그 글 추가 메서드
     * AddArticleRequest 타입(DTO)으로 객체를 받으면
     * AddArticleRequest 의 toEntity 메서드를 이용해서 Article(entity) 객체로 변환하고
     * JPA의 CRUD 메서드인 save() 를 이용해 데이터베이스에 저장합니다.
     *
     * @param request
     * @return
     */
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    /**
     * <h2>블로그 글 전체목록 조회 메서드</h2>
     * <p>
     * 블로그 글 전체 목록을 조회합니다.
     * </p>
     * <p>
     * JPA 지원 메서드인 findAll()을 호출하여 article 테이블에 저장되어 있는 모든 데이터를 조회합니다.
     * </p>
     */
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    /**
     * <h2>블로그 글을 id에 맞는 글을 조회 메서드</h2>
     * <p>
     * JPA 지원 메서드인 findById()를 호출하고 orElseThrow() 를 이용하여 값이 있으면
     * 해당 값을 반환하고 값이 없으면 원하는 예외와 문구를 출력합니다.
     * </p>
     */
    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("not found: " + id));
    }

    /**
     * <h2>블로그 id에 해당되는 글을 삭제합니다.</h2>
     */
    public void delete(Long id) {
        blogRepository.deleteById(id);
    }

    /**
     * <h2>블로그 글 수정 하기</h2>
     * <p>
     * 특정 id의 글을 수정합니다.
     * </p>
     */
    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }
}
