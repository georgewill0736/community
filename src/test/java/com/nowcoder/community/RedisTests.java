package com.nowcoder.community;

import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.TimeUnit;

/**
 * @Author 杜俊宏
 * Date on 2021/2/10 19:40
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings() {
        String key = "test:count";
        redisTemplate.opsForValue().set(key,12);
        System.out.println(redisTemplate.opsForValue().get(key));
        System.out.println(redisTemplate.opsForValue().decrement(key,33));
        System.out.println(redisTemplate.opsForValue().increment(key));
        System.out.println(redisTemplate.opsForValue().increment(key,33));

    }


    @Test
    public void testHash() {
        String key = "test:user";
        redisTemplate.opsForHash().put(key,"id",24);
        redisTemplate.opsForHash().put(key,"username","djh");
        redisTemplate.opsForHash().put(key,"age",18);

        System.out.println("编号"+redisTemplate.opsForHash().get(key,"id"));
        System.out.println("姓名"+redisTemplate.opsForHash().get(key,"username"));
        System.out.println("年龄"+redisTemplate.opsForHash().get(key,"age"));
    }

    @Test
    public void testLists() {

        String key = "test:ids";

        redisTemplate.opsForList().leftPush(key,1221);
        redisTemplate.opsForList().leftPush(key,456);
        redisTemplate.opsForList().leftPush(key,7888);

        System.out.println(redisTemplate.opsForList().size(key));
        System.out.println(redisTemplate.opsForList().index(key,0));
        System.out.println(redisTemplate.opsForList().range(key,0,2));
        System.out.println(redisTemplate.opsForList().leftPop(key));
        System.out.println(redisTemplate.opsForList().rightPop(key));
        System.out.println(redisTemplate.opsForList().leftPop(key));

    }



    @Test
    public void testSet() {
        String key = "test:teachers";

        redisTemplate.opsForSet().add(key,"huaishdau","hhhhas","quqweir","gdfg");
        System.out.println(redisTemplate.opsForSet().size(key));
        System.out.println(redisTemplate.opsForSet().pop(key));
        System.out.println(redisTemplate.opsForSet().members(key));

    }


    @Test
    public void testSortedSet() {

        String key = "test:students";

        redisTemplate.opsForZSet().add(key,"djh",1000);
        redisTemplate.opsForZSet().add(key,"hhh",753);
        redisTemplate.opsForZSet().add(key,"dfdf",53757);
        redisTemplate.opsForZSet().add(key,"wrgr",0202);

        System.out.println(redisTemplate.opsForZSet().zCard(key));
        System.out.println(redisTemplate.opsForZSet().score(key,"djh"));
        System.out.println(redisTemplate.opsForZSet().reverseRank(key,"djh"));
        System.out.println(redisTemplate.opsForZSet().range(key,0,2));
        System.out.println(redisTemplate.opsForZSet().reverseRange(key,0,2));

    }


    @Test
    public void testKeys() {

        String key = "test:user";
        redisTemplate.delete(key);
        System.out.println(redisTemplate.hasKey(key));
        redisTemplate.expire("test:students",10,TimeUnit.SECONDS);

    }


    //多次访问同一个key
    @Test
    public void testBoundOperation() {
        String key = "test:count";

        BoundValueOperations boundValueOperations = redisTemplate.boundValueOps(key);
        boundValueOperations.increment();
        boundValueOperations.increment();
        boundValueOperations.increment();

        System.out.println(boundValueOperations.get());

    }


    //编程式事务
    @Test
    public void testTransactional() {
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String key = "test:tx";
                redisOperations.multi();
                redisOperations.opsForSet().add(key,"sdfsaf");
                redisOperations.opsForSet().add(key,"werew");
                redisOperations.opsForSet().add(key,"qwretty");
                System.out.println(redisOperations.opsForSet().members(key));
                return redisOperations.exec();
            }
        });
        System.out.println(obj);
    }

}
