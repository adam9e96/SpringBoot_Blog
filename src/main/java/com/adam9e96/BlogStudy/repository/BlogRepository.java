package com.adam9e96.BlogStudy.repository;

import com.adam9e96.BlogStudy.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link Article} 엔티티를 관리하는 리포지토리 인터페이스입니다.
 *
 * <p>
 * Spring Data JPA의 {@link JpaRepository}를 확장하여 기본적인 CRUD(Create, Read, Update, Delete)
 * 기능과 페이징, 정렬 기능을 제공합니다.
 * </p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.adam9e96.BlogStudy.domain.Article
 */
@Repository
public interface BlogRepository extends JpaRepository<Article, Long> {

    // 필요한 경우 커스텀 쿼리 메서드를 여기에 추가할 수 있습니다.

}
