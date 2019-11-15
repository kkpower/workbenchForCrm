package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;
import sun.security.util.AuthResources_it;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * Author: 动力节点
 * 2019/9/19
 */
public class SysInitListener implements ServletContextListener {

    /*

        contextInitialized：
            是用来监听application对象创建的方法
            当application对象创建了，则马上执行该方法

        event：
            我们可以通过该参数get得到监听的域对象
            例如我们现在监听的是application域对象
            event就可以get到这个对象

     */
    @Override
    public void contextInitialized(ServletContextEvent event) {

        //System.out.println("application对象创建了");

        System.out.println("服务器缓存处理数据字典开始");

        ServletContext application = event.getServletContext();

        //System.out.println("对象是："+application);

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());

        //7组键值对
        /*

            key                     value
            appellationList         dvList1
            stageList               dvList2
            clueStateList           dvList3
            ...
            ...                     dvList7

         */
        Map<String,List<DicValue>> map = ds.getAll();

        Set<String> set = map.keySet();

        for(String key:set){

            application.setAttribute(key, map.get(key));

        }

        System.out.println("服务器缓存处理数据字典结束");

        //解析Stage2possibility.properties属性文件
        /*

            将properties中的兼职对解析出来，保存在map对象中
            在将map保存到服务器缓存中

         */
        System.out.println("解析Stage2possibility.properties");

        Map<String,String> pMap = new HashMap<>();

        //使用ResourceBundle解析properties文件，指定的文件不能加后缀名
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");

        Enumeration<String> e = rb.getKeys();

        while(e.hasMoreElements()){

            //stage
            String key = e.nextElement();
            //possibility
            String value = rb.getString(key);

            System.out.println("key:"+key);
            System.out.println("value:"+value);
            System.out.println("-------------");

            pMap.put(key, value);

        }

        application.setAttribute("pMap", pMap);

    }
}

























