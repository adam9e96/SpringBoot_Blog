package com.adam9e96.BlogStudy.repository;

import com.adam9e96.BlogStudy.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * {@link Article} 엔티티를 관리하는 리포지토리 인터페이스입니다.
 *
 * <p>
 * <strong>메서드 상속 구조:</strong>
 * <pre>
 * com.adam9e96.BlogStudy.repository.BlogRepository
 *      └── extends org.springframework.data.jpa.repository.JpaRepository&lt;Article, Long&gt;
 *              └── extends org.springframework.data.repository.PagingAndSortingRepository&lt;Article, Long&gt;
 *                      └── extends org.springframework.data.repository.CrudRepository&lt;Article, Long&gt;
 *                              └── extends org.springframework.data.repository.Repository&lt;Article, Long&gt;
 * </pre>
 * </p>
 *
 * <p>
 * {@code save()} 메서드는 {@link CrudRepository} 인터페이스로부터 상속되었습니다.
 * 이를 통해 엔티티의 저장 및 업데이트 기능을 제공합니다.
 * </p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.springframework.data.repository.CrudRepository
 * @see com.adam9e96.BlogStudy.domain.Article
 */
public interface BlogRepository extends JpaRepository<Article, Long> {

}
