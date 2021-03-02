package com.nowcoder.community.quartz;

import com.nowcoder.community.Service.DiscussPostService;
import com.nowcoder.community.Service.ElasticSearchService;
import com.nowcoder.community.Service.LikeService;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.RedisKeyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author 杜俊宏
 * Date on 2021/3/2 22:46
 */

public class PostScoreRefreshJob implements Job,CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(PostScoreRefreshJob.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    //牛客纪元
    private static final Date epoch;

    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-08-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化牛客纪元失败！",e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        String redisKey = RedisKeyUtil.getPostScoreKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(redisKey);
        if (operations.size().equals(0)) {
            logger.info("任务取消，没有需要刷新的帖子");
            return;
        }
        logger.info("【任务开始】 正在刷新帖子分数："+operations.size());

        while (operations.size()>0) {
            this.refresh((Integer)operations.pop());
        }

        logger.info("【任务结束】 帖子分数刷新完毕！");

    }


    private void refresh(Integer postId) {
        DiscussPost discussPost = discussPostService.findDiscussPostById(postId);
        if (discussPost == null) {
            logger.error("该帖子不存在！id = " + postId);
            return;
        }

        //是否加精
        boolean wonderful = discussPost.getStatus() == 1;
        //评论数量
        int commentCount = discussPost.getCommentCount();
        //点赞数量
        Long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,postId);

        //计算权重
        double w = (wonderful?75:0) + commentCount*10 +likeCount*2;

        //分数 = 帖子权重 + 距离天数
        double score = Math.log10(Math.max(w,1))
                +(discussPost.getCreateTime().getTime()-epoch.getTime())/(1000*3600*24);

        //更新帖子分数
        discussPostService.updateScore(postId,score);

        //同步ES搜索数据
        discussPost.setScore(score);
        elasticSearchService.saveDiscussPost(discussPost);

    }



}
