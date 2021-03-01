package com.nowcoder.community;

import com.nowcoder.community.Service.AlphaService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author 杜俊宏
 * Date on 2021/2/28 19:12
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ThreadPoolTests {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTests.class);


    //JDK普通的线程池的初始化
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    //JDK可执行定时任务的线程池的初始化
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    //注入Spring普通的线程池,(能注入说明Spring已经在容器中做好了，不用自己建)
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    //注入Spring可执行定时任务的线程池，(能注入说明Spring已经在容器中做好了，不用自己建)
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private AlphaService alphaService;

    private void sleep(int m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //1,JDK普通线程池
    @Test
    public void testExecutorService() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ExecutorService");
            }
        };
        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }
        sleep(10000);
    }


    //2，JDK定时任务线程池
    @Test
    public void testScheduledExecutorService() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ScheduledExecutorService");
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(task,10000,1000,TimeUnit.MILLISECONDS);
        sleep(30000);
    }


    //3，Spring普通线程池
    @Test
    public void testThreadPoolTaskExecutor() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ThreadPoolTaskExecutor");
            }
        };
        for (int i = 0; i < 10; i++) {
            threadPoolTaskExecutor.submit(task);
        }
        sleep(10000);
    }

    //4，spring定时任务的线程池
    @Test
    public void testThreadPoolTaskScheduler() {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ThreadPoolTaskScheduler");
            }
        };
        Date startTime = new Date(System.currentTimeMillis()+10000);
        threadPoolTaskScheduler.scheduleAtFixedRate(task,startTime,1000);
        sleep(30000);

    }

    //5，Spring普通线程池简化的使用方式
    @Test
    public void testThreadPoolTaskExecutorSimple() {
        for (int i = 0; i < 10; i++) {
            alphaService.execute1();
        }
        sleep(10000);
    }


    //6，Spring定时任务线程池(简化)
    @Test
    public void testThreadPoolTaskSchedulerSimple() {
        sleep(30000);
    }


}
