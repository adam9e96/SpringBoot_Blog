package com.adam9e96.BlogStudy.controller;

import com.adam9e96.BlogStudy.domain.Article;
import com.adam9e96.BlogStudy.dto.AddArticleRequest;
import com.adam9e96.BlogStudy.dto.ArticleResponse;
import com.adam9e96.BlogStudy.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <h1>Controller</h1>
 * {@code BlogApiController} 클래스는 블로그 게시물과 관련된 RESTful API 엔드포인트를 제공합니다.
 * <p>
 * 이 컨트롤러는 클라이언트로부터의 HTTP 요청을 처리하고, {@link BlogService}를 통해
 * 비즈니스 로직을 수행한 후 적절한 HTTP 응답을 반환합니다.
 * </p>
 *
 * <p>
 * <strong>주요 기능:</strong>
 * <ul>
 *   <li>새로운 게시물 추가</li>
 *   <li>게시물 조회, 수정, 삭제 등의 추가적인 API 엔드포인트 확장 가능</li>
 * </ul>
 * </p>
 *
 * <p>
 * <strong>사용된 주요 어노테이션:</strong>
 * <ul>
 *   <li>{@link RestController}: 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다.</li>
 *   <li>{@link RequiredArgsConstructor}: Lombok을 사용하여 final 필드의 생성자를 자동 생성합니다.</li>
 *   <li>{@link Slf4j}: Lombok을 사용하여 로깅 기능을 제공합니다.</li>
 * </ul>
 * </p>
 *
 * @see BlogService
 * @see Article
 * @see AddArticleRequest
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class BlogApiController {

    /**
     * {@code BlogService} 인스턴스입니다.
     * <p>
     * 이 서비스는 블로그 게시물과 관련된 비즈니스 로직을 처리합니다.
     * </p>
     */
    private final BlogService blogService;

    /**
     * 새로운 블로그 게시물을 추가하는 API 엔드포인트입니다.
     * <p>
     * 클라이언트로부터 {@link AddArticleRequest} 객체를 JSON 형식으로 전달받아,
     * 이를 {@link Article} 엔티티로 변환한 후 데이터베이스에 저장합니다.
     * 성공적으로 저장되면 저장된 게시물 정보를 포함하여 HTTP 201 (Created) 상태 코드를 반환합니다.
     * </p>
     *
     * @param request 클라이언트로부터 전달받은 게시물 추가 요청 데이터
     *                {@link AddArticleRequest} 객체로 매핑됩니다.
     * @return {@link ResponseEntity} 객체로, 저장된 {@link Article} 엔티티와
     * HTTP 상태 코드를 포함하여 반환됩니다.
     * <ul>
     *   <li>{@code 201 Created}: 게시물이 성공적으로 생성되었을 때 반환됩니다.</li>
     *   <li>{@code 400 Bad Request}: 요청 데이터가 유효하지 않을 경우 반환될 수 있습니다.</li>
     *   <li>{@code 500 Internal Server Error}: 서버 내부 오류가 발생했을 때 반환될 수 있습니다.</li>
     * </ul>
     * @throws IllegalArgumentException 요청 데이터가 유효하지 않은 경우 예외를 던질 수 있습니다.
     */
    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        // 서비스 레이어를 통해 게시물 저장
        Article savedArticle = blogService.save(request);

        // 저장된 게시물 정보를 로그에 기록
        log.info("Saved article: {}", savedArticle);

        /*
         * 응답 코드 설명:
         *
         * - HttpStatus.CREATED (201):
         *   요청이 성공적으로 수행되었으며, 새로운 리소스(게시물)가 생성되었음을 나타냅니다.
         *   클라이언트는 이 응답을 통해 새로 생성된 리소스의 URI를 알 수 있습니다.
         *
         * - HttpStatus.BAD_REQUEST (400):
         *   클라이언트가 잘못된 데이터를 전송했을 경우 반환됩니다.
         *   예를 들어, 필수 필드가 누락되었거나 데이터 형식이 잘못된 경우입니다.
         *
         * - HttpStatus.INTERNAL_SERVER_ERROR (500):
         *   서버 내부에서 예기치 않은 오류가 발생했을 때 반환됩니다.
         *   이는 주로 코드의 버그나 외부 시스템과의 통신 오류 등으로 인해 발생할 수 있습니다.
         */
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    /**
     * /api/articles GET 요청이 오면 글 목록을 조회할 findAllArticles()
     * /api/articles GET 요청이 오면 글 전체를 조회하는 findAll() 메서드를 호출 한 다음
     * 응답용 객체인 ArticleResponse 로 파싱해 body에 담아 클라이언트에 전송합니다.
     */
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();
        return ResponseEntity.ok()
                .body(articles);

    }

    // 추가적인 API 엔드포인트가 필요하다면 여기에 정의할 수 있습니다.
}
