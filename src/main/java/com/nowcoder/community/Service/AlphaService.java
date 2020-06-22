package com.nowcoder.community.Service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
//@Scope("prototype")
public class AlphaService  {

    @Autowired
    public AlphaDao alphaDao;

    public  AlphaService() {
        System.out.println("实例化我的AlphaService");
    }

    @PostConstruct
    public  void init() {
        System.out.println("初始化AlphaService");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("销毁AlphaService");
    }

    public  String find() {
        return alphaDao.select();
    }

}
