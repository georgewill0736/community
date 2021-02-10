package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author 杜俊宏
 * Date on 2021/2/8 21:56
 */
@Mapper
public interface MessageMapper {

    //查询当前用户的会话列表，针对每个会话返回一条最新的私信
    List<Message> selectConversations(@Param("userId") int userId,@Param("offset") int offset
            ,@Param("limit") int limit);

    //查询当前用户的会话数量。
    int selectConversationCount(int userId);

    //查询某个会话包含的私信列表
    List<Message> selectLetters(@Param("conversationId") String conversationId,
                                @Param("offset") int offset,@Param("limit") int limit);

    //查询某个会话包含的私信数量
    int selectLetterCount(String conversationId);

    //查询未读私信数量
    int selectLetterUnReadCount(@Param("userId") int userId,@Param("conversationId") String conversationId);

    //添加私信
    int insertMessage(Message message);

    //修改消息的状态
    int updateStatus(@Param("ids") List<Integer> ids,@Param("status") int status);


}