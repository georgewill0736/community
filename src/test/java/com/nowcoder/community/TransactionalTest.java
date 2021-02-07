package com.nowcoder.community;

import com.nowcoder.community.Service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.security.AlgorithmConstraints;

/**
 * @Author 杜俊宏
 * Date on 2021/2/7 23:15
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class TransactionalTest {

    @Autowired
    private AlphaService alphaService;

    @Test
    public void testSave1() {
        Object object = alphaService.save1();
        System.out.println(object);
    }

    @Test
    public void testSave2() {
        Object object = alphaService.save2();
        System.out.println(object);
    }

}
