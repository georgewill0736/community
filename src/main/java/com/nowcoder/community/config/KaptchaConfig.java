package com.nowcoder.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean
    public Producer kaptchaProducer() {
        Properties properties= new Properties();
        properties.setProperty("kaptcha.image.width","100"); //图片宽度，为100 像素
        properties.setProperty("kaptcha.image.height","40"); //图片高度，为40  像素
        properties.setProperty("kaptcha.textproducer.font.size","32"); // 字体，字号为32号大小
        properties.setProperty("kaptcha.textproducer.font.color","0,0,0"); // 字体，颜色为基于RGB
        properties.setProperty("kaptcha.textproducer.char.string","0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"); // 设定验证码的随机字符串的范围
        properties.setProperty("kaptcha.textproducer.char.length","4"); // 设定验证码的长度
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise"); // 设定验证码的干扰为无干扰

        DefaultKaptcha kaptcha =new DefaultKaptcha();
        Config config =new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
