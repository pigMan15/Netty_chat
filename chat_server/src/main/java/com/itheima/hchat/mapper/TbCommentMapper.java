package com.itheima.hchat.mapper;

import com.itheima.hchat.pojo.TbComment;
import com.itheima.hchat.pojo.TbCommentExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbCommentMapper {
    int countByExample(TbCommentExample example);

    int deleteByExample(TbCommentExample example);

    int deleteByPrimaryKey(String id);

    int insert(TbComment record);

    int insertSelective(TbComment record);

    List<TbComment> selectByExample(TbCommentExample example);

    TbComment selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TbComment record, @Param("example") TbCommentExample example);

    int updateByExample(@Param("record") TbComment record, @Param("example") TbCommentExample example);

    int updateByPrimaryKeySelective(TbComment record);

    int updateByPrimaryKey(TbComment record);
}
