package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: 动力节点
 * 2019/9/16
 */
public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到用户模块控制器");

        String path = request.getServletPath();

        if("/settings/user/login.do".equals(path)){

            login(request,response);

        }else if("/settings/user/xxx.do".equals(path)){

            //xxx(request,response);

        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到验证登录操作");

        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //将密码通过MD5的形式来进行加密
        loginPwd = MD5Util.getMD5(loginPwd);

        /*

            取得浏览器端的ip地址

            取得ip地址，如果做测试时使用的是localhost，那么取得的ip地址应该是一堆0+1 ，这种ip地址是无效的
            我们可以使用127.0.0.1来做访问，可以有效的取得该ip地址


         */
        String ip = request.getRemoteAddr();

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try{

            User user = us.login(loginAct,loginPwd,ip);

            System.out.println("以上代码如果报错了，就不会执行我了。。。。。。");

            //如果我们有机会执行session域对象中保存user对象，那么不用进行多余的判断，上面的一句代码不会接受到任何的异常，user对象一定是有的
            request.getSession().setAttribute("user", user);

            //登录成功
            /*

                为前端提供的信息是：
                {"success":true}

             */

            /*String str = "{\"success\":true}";
            response.getWriter().print(str);*/

            PrintJson.printJsonFlag(response, true);

        }catch(Exception e){

            //登录失败
            //取得错误信息，错误信息就是异常信息
            String msg = e.getMessage();

            /*

                为前端提供的信息是：
                {"success":false,"msg":?}


             */

            Map<String,Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);

            PrintJson.printJsonObj(response, map);



        }





    }
}










































