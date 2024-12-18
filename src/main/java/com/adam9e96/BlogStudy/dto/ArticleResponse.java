package com.adam9e96.BlogStudy.dto;

import com.adam9e96.BlogStudy.domain.Article;
import lombok.Getter;

@Getter
public class ArticleResponse {
    private String title;
    private String content;

    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }

}
