package com.itheima.hchat.mapper;

import com.itheima.hchat.pojo.TbArticle;
import com.itheima.hchat.pojo.TbArticleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbArticleMapper {
    int countByExample(TbArticleExample example);

    int deleteByExample(TbArticleExample example);

    int deleteByPrimaryKey(String id);

    int insert(TbArticle record);

    int insertSelective(TbArticle record);

    List<TbArticle> selectByExample(TbArticleExample example);

    TbArticle selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TbArticle record, @Param("example") TbArticleExample example);

    int updateByExample(@Param("record") TbArticle record, @Param("example") TbArticleExample example);

    int updateByPrimaryKeySelective(TbArticle record);

    int updateByPrimaryKey(TbArticle record);
}