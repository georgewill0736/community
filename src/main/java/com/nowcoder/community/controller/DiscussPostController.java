package com.nowcoder.community.controller;

import com.nowcoder.community.Service.CommentService;
import com.nowcoder.community.Service.DiscussPostService;
import com.nowcoder.community.Service.LikeService;
import com.nowcoder.community.Service.UserService;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @Author 杜俊宏
 * Date on 2021/2/1 22:59
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title,String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403,"还没有登录");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setContent(content);
        post.setTitle(title);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);
        //报错的情况，将来统一处理
        return CommunityUtil.getJSONString(0,"发布成功");
    }


    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        //帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",post);
        //作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);

        //点赞有关信息
        //点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,discussPostId);
        model.addAttribute("likeCount",likeCount);
        //点赞状态
        int likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_POST,discussPostId);
        model.addAttribute("likeStatus",likeStatus);

        //评论的分页信息
        page.setLimit(10);
        page.setPath("/discuss/detail/"+discussPostId);
        page.setRows(post.getCommentCount());

        // 评论：给帖子的评论
        // 回复：给评论的评论
        // 评论列表
        List<Comment> commentList = commentService.findCommentByEntity(ENTITY_TYPE_POST,post.getId()
                ,page.getOffset(),page.getLimit());
        //评论VO列表
        List<Map<String,Object>> commentVOList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                //评论VO
                Map<String,Object> commentVO = new HashMap<>();
                //评论
                commentVO.put("comment",comment);
                //作者
                commentVO.put("user",userService.findUserById(comment.getUserId()));

                //点赞数量
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId());
                commentVO.put("likeCount",likeCount);
                //点赞状态
                likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT,comment.getId());
                commentVO.put("likeStatus",likeStatus);


                //回复列表
                List<Comment> repltList = commentService.findCommentByEntity(ENTITY_TYPE_COMMENT
                        ,comment.getId(),page.getOffset(),page.getLimit());
                //回复的VO列表
                List<Map<String,Object>> replyVOList = new ArrayList<>();
                if (repltList != null) {
                    for (Comment reply :repltList) {
                        Map<String,Object> replyVO = new HashMap<>();
                        //回复
                        replyVO.put("reply",reply);
                        //作者
                        replyVO.put("user",userService.findUserById(reply.getUserId()));
                        //回复的目标
                        User target = reply.getTargetId() == 0?null:userService.findUserById(reply.getTargetId());
                        replyVO.put("target",target);

                        //点赞数量
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,reply.getId());
                        replyVO.put("likeCount",likeCount);
                        //点赞状态
                        likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT,reply.getId());
                        replyVO.put("likeStatus",likeStatus);

                        replyVOList.add(replyVO);
                    }
                }
                commentVO.put("replys",replyVOList);
                //回复数量
                int replyCount = commentService.findCommentCountByEntity(ENTITY_TYPE_COMMENT,comment.getId());
                commentVO.put("replyCount",replyCount);
                commentVOList.add(commentVO);
            }
        }
        model.addAttribute("comments",commentVOList);
        return "/site/discuss-detail";
    }

}
