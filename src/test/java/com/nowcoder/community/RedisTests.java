package com.nowcoder.community;

import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
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


    //统计20万个重复数据的独立总数
    @Test
    public void testHyperLogLog() {
        String redisKey = "test:hll:01";
        for (int i = 1; i <= 100000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey,i);
        }
        for (int i = 1; i <= 100000; i++) {
            int r = (int)(Math.random()*100000+1);
            redisTemplate.opsForHyperLogLog().add(redisKey,r);
        }
        Long size = redisTemplate.opsForHyperLogLog().size(redisKey);
        System.out.println(size);
    }

    //将三组数据合并,再统计合并后的重复数据的独立总数
    @Test
    public void testHyperLogLogUnion() {
        String redisKey2 = "test:hll:02";
        for (int i = 1; i <= 10000 ; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey2,i);
        }

        String redisKey3 = "test:hll:03";
        for (int i = 5001; i <= 15000 ; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey3,i);
        }


        String redisKey4 = "test:hll:04";
        for (int i = 10001; i <= 20000 ; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey4,i);
        }

        String unionKey = "test:hll:union";
        redisTemplate.opsForHyperLogLog().union(unionKey,redisKey2,redisKey3,redisKey4);
        Long size = redisTemplate.opsForHyperLogLog().size(unionKey);
        System.out.println(size);

    }


    //统计一组数据bool值
    @Test
    public void testBitMap() {
        String redisKey = "test:bm:01";

        //忘redis中存数据,默认值为false，因此false不用存
        redisTemplate.opsForValue().setBit(redisKey,1,true);
        redisTemplate.opsForValue().setBit(redisKey,4,true);
        redisTemplate.opsForValue().setBit(redisKey,7,true);

        //查结果
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,2));
        //统计
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.bitCount(redisKey.getBytes());
            }
        });
        System.out.println(obj);
    }

    //统计3组数据的布尔值，再对这3组数据做OR运算
    @Test
    public void testBitMapOperation() {
        String  redisKey2 = "test:bm:02";
        redisTemplate.opsForValue().setBit(redisKey2,0,true);
        redisTemplate.opsForValue().setBit(redisKey2,1,true);
        redisTemplate.opsForValue().setBit(redisKey2,2,true);


        String  redisKey3 = "test:bm:03";
        redisTemplate.opsForValue().setBit(redisKey3,2,true);
        redisTemplate.opsForValue().setBit(redisKey3,3,true);
        redisTemplate.opsForValue().setBit(redisKey3,4,true);


        String  redisKey4 = "test:bm:04";
        redisTemplate.opsForValue().setBit(redisKey4,4,true);
        redisTemplate.opsForValue().setBit(redisKey4,5,true);
        redisTemplate.opsForValue().setBit(redisKey4,6,true);

        String redisKey = "test:bm:or";
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.bitOp(RedisStringCommands.BitOperation.OR,
                        redisKey.getBytes(),redisKey2.getBytes(),redisKey3.getBytes(),redisKey4.getBytes());
                return redisConnection.bitCount(redisKey.getBytes());
            }
        });
        System.out.println(obj);
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,2));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,3));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,4));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,5));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,6));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,7));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,8));
    }

}
