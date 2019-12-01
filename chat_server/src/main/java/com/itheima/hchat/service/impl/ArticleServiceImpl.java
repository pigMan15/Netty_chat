package com.itheima.hchat.service.impl;

import com.itheima.hchat.mapper.TbArticleMapper;
import com.itheima.hchat.mapper.TbFriendMapper;
import com.itheima.hchat.mapper.TbUserMapper;
import com.itheima.hchat.pojo.*;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private TbArticleMapper tbArticleMapper;

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private TbFriendMapper tbFriendMapper;

    @Override
    public List<TbArticle> findArticleByUserId(String userid) {

        //查找当前用户的朋友列表数据

        TbFriendExample tbFriendExample = new TbFriendExample();
        TbFriendExample.Criteria friendCriteria = tbFriendExample.createCriteria();
        friendCriteria.andUseridEqualTo(userid);
        List<TbFriend> tbfriendList = tbFriendMapper.selectByExample(tbFriendExample);

        //查找每个朋友的说说数据

        List<TbArticle> articles = new ArrayList<>();
        for(TbFriend tbFriend : tbfriendList){
            TbArticleExample articleExample = new TbArticleExample();
            TbArticleExample.Criteria articleCriteria = articleExample.createCriteria();
            articleCriteria.andUseridEqualTo(tbFriend.getFriendsId());
            List<TbArticle> list = tbArticleMapper.selectByExample(articleExample);
            articles.addAll(list);
        }

        return articles;

    }
}
