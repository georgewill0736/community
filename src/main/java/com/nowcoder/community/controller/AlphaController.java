package com.nowcoder.community.controller;


import com.nowcoder.community.Service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.OverridesAttribute;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @Autowired
    public AlphaService  alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public  String Hello()
    {
        return "Hello Spring Boot!!!";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while(enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name+": "+value);
        }
        System.out.println(request.getParameter("code"));


        //返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try (
                PrintWriter writer = response.getWriter()
        ) {
            writer.write("<hi>牛客网</hi>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //GET请求
    //查询所有学生  /student?cur=1&limit=20

    @RequestMapping(path="/students",method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(@RequestParam (name="current",required = false,defaultValue = "1") int current,
                              @RequestParam (name="limit",required = false,defaultValue = "10") int limit) {
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // /student/123
    @RequestMapping(path="/student/{id}",method = RequestMethod.GET)
    @ResponseBody

    public String getStundent(@PathVariable("id") int id) {
        System.out.println(id);
        return "a student";
    }

    //POST请求
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }
    //响应HTML数据
    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("name","库里");
        mav.addObject("age","30");
        mav.setViewName("/demo/view");
        return mav;
    }

    //
   @RequestMapping(path = "/school",method = RequestMethod.GET)
   public String getSchool(Model model) {
        model.addAttribute("name","哈尔滨工业大学");
        model.addAttribute("age",100);
        return "/demo/view";
   }

    //响应json数据
    //Java对象
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmp() {
        List<Map<String,Object>> list = new ArrayList<>();
        Map emp = new HashMap();
        emp.put("name","杜俊宏");
        emp.put("age",24);
        emp.put("salary",1000000000);

        list.add(emp);
        emp=new HashMap();
        emp.put("name","罗伊");
        emp.put("team","开拓者");
        emp.put("age",39);
        list.add(emp);
        return list;
    }

    //cookie示例
    @RequestMapping(path = "/cookie/set",method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        //创建cookie
        Cookie cookie = new Cookie("code",CommunityUtil.generateUUID());
        //设置cookie的生效范围
        cookie.setPath("/community/alpha");
        //设置cookie的生存时间
        cookie.setMaxAge(60*10);
        //发送cookie
        response.addCookie(cookie);
        return "set cookie";
    }

    @RequestMapping(path = "/cookie/get",method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code) {
        System.out.println(code);
        return "get cookie";
    }

    //session示例
    @RequestMapping(path = "/session/set",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session) {
        session.setAttribute("id",1);
        session.setAttribute("name","test");
        return "setSession";
    }

    @RequestMapping(path = "/session/get",method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "getSession";
    }

}
