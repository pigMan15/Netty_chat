package com.itheima.hchat.controller;


import com.itheima.hchat.pojo.TbArticle;
import com.itheima.hchat.pojo.vo.Result;
import com.itheima.hchat.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/article")
public class ArticleController {


    @Autowired
    private ArticleService articleService;


    @RequestMapping(value = "/findArticleByUserId")
    public List<TbArticle> findArticleByUserId(String userid){

        try{
           return  articleService.findArticleByUserId(userid);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
