package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author 杜俊宏
 * Date on 2021/2/7 23:50
 */

@Mapper
public interface CommentMapper {


    List<Comment> selectCommentByEntity(@Param("entityType") int entityType
            ,@Param("entityId") int entityId,@Param("offset") int offset,@Param("limit") int limit);


    int selectCountByEntity(@Param("entityType") int entityType,@Param("entityId") int entityId);


    int insertComment(Comment comment);
}
