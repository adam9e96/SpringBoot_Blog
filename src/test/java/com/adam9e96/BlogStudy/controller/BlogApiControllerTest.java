package com.adam9e96.BlogStudy.controller;

import com.adam9e96.BlogStudy.domain.Article;
import com.adam9e96.BlogStudy.dto.AddArticleRequest;
import com.adam9e96.BlogStudy.repository.BlogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest // 테스트용 애플리케이션 컨텍스트
@AutoConfigureMockMvc // MockMvc 생성 및 자동 구성
class BlogApiControllerTest {

    private static final Logger log = LoggerFactory.getLogger(BlogApiControllerTest.class);
    @Autowired
    protected MockMvc mockMvc;

    /**
     * 이 클래스로 만든 객체는 자바 객체를 JSON 데이터로 변환하는 직렬화 또는
     * 반대로 JSON 데이터를 자바에서 사용하기 위해 자바 객체로 변환하는 역직렬화를 할 때 사용합니다.
     */
    @Autowired
    protected ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 클래스

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        this.blogRepository.deleteAll();
    }

    // ============================================ //

    /**
     * <h2>CREATE</h2>
     * given : 블로그 글 추가에 필요한 요청 객체를 만듭니다.
     * when  : 블로그 글 추가 API에 요청을 보냅니다. 이떄 요청 타입은 JSON 이며, given 절에서 미리 만들어준 객체를 요청 본문으로
     * 함께 보냅니다.
     * then : 응답 코드가 201 Created 인지 확인합니다. Blog 를 전체 조회해 크기가 1인지 확인하고, 실제로 저장된 데이터와
     * 요청 값을 비교합니다.
     *
     * @throws Exception
     */
    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
        log.info(userRequest.toString());
        // 객체 JSON으로 직렬화
        // writeValueAsString : Java 값을 문자열로 직렬화하는 데 사용할 수 있는 메서드
        final String requestBody = objectMapper.writeValueAsString(userRequest);


        // when
        // 설정한 내용을 바탕으로 요청 전송
        ResultActions result = this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();
        assertThat(articles.size()).isEqualTo(1); // 크기가 1인지 검증. 블로그 글의 개수가 1인지 확인
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }


}