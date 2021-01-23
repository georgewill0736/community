package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @Author 杜俊宏
 * Date on 2021/1/23 16:51
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "这里可以赌博可以嫖娼可以吸毒可以开票，哈哈哈哈nice!";
        String res = sensitiveFilter.filter(text);
        System.out.println(res);
        text = "这里可以赌！#博可以嫖@#%#￥%娼可以@@@吸##@￥@毒可以开！@#￥票！@@#，哈哈哈哈nice!";
        System.out.println(sensitiveFilter.filter(text));
    }

}
