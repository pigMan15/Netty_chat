package com.itheima.hchat.service;

import com.itheima.hchat.pojo.TbArticle;

import java.util.List;

public interface ArticleService {
    List<TbArticle> findArticleByUserId(String userid);
}
