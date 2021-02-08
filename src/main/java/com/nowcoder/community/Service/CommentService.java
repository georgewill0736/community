package com.nowcoder.community.Service;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 杜俊宏
 * Date on 2021/2/8 0:00
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;


    public List<Comment> findCommentByEntity(int entityType,int entityId,int offset,int limit) {
        return commentMapper.selectCommentByEntity(entityType,entityId,offset,limit);
    }

    public int findCommentCountByEntity(int entityType,int entityId) {
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

}
