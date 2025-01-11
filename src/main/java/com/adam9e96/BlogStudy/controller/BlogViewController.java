package com.adam9e96.BlogStudy.controller;

import com.adam9e96.BlogStudy.domain.Article;
import com.adam9e96.BlogStudy.dto.ArticleListViewResponse;
import com.adam9e96.BlogStudy.dto.ArticleViewResponse;
import com.adam9e96.BlogStudy.service.BlogServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogServiceImpl blogService;

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        log.info(articles.toString());
        model.addAttribute("articles", articles); // 블로그 글 리스트 저장
        return "articleList"; // articleList.html 라는 뷰 조회
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));
        return "article";
    }

    // id 키를 가진 쿼리 파라미터의 값을 id 변수에 매핑(id는 없을 수도 있음)
    @GetMapping("/new-article")
    public String newArticle(@RequestParam(name = "id", required = false) Long id, Model model) {
        if (id == null) {// id 가 없으면 생성
            model.addAttribute("article", new ArticleViewResponse());
        } else { // id 가 없으면 수정
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "newArticle";
    }

}
