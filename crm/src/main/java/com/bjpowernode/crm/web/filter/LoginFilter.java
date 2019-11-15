package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author: 动力节点
 * 2019/9/16
 */
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        System.out.println("进入到判断有没有登录过的过滤器");

        /*

            从session中取user
            判断user是否为null，就可以判断有没有登录过了

         */

        /*

            我们现在需要使用HttpServletRequest和HttpServletResponse  需要儿子

            但是我们现在只有ServletRequest和ServletResponse 我们现在有 父亲

            我们现在需要做一个父转子的操作，使用儿子
            需要强转

         */

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;


        //取得访问路径
        String path = request.getServletPath();
        //如果遇到访问这两个资源，应该自动放行
        if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){

            chain.doFilter(req, resp);

        //访问的是除了登录操作以外的其他资源，必须验证有没有登录过
        }else{

            User user = (User) request.getSession().getAttribute("user");

            if(user!=null){

                //登录过
                //将请求放行
                chain.doFilter(req, resp);

            }else{

                //没登录过
                //重定向到登录页
            /*

                不论使用转发还是使用重定向，关于路径，一定使用的是绝对路径

                转发：
                    转发的路径的写法，比较特殊，使用的虽然是绝对路径，但是前面不加/项目名，这种路径也叫做项目中的内部路径

                重定向：
                    重定向的路径的写法，就是传统的绝对路径的写法，前面必须加/项目名


                request.getContextPath():动态的取得当前项目的/项目名

             */

                response.sendRedirect(request.getContextPath() + "/login.jsp");

            }

        }

    }
}







































