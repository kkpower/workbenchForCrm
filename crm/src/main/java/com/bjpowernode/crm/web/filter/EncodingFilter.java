package com.bjpowernode.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Author: 动力节点
 * 2019/9/16
 */
public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        System.out.println("进入到过滤字符编码的过滤器");

        //过滤post请求中文参数
        req.setCharacterEncoding("UTF-8");
        //过滤响应流的中文信息
        resp.setContentType("text/html;charset=utf-8");

        //将请求放行
        chain.doFilter(req, resp);

    }
}












































