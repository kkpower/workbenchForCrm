package com.bjpowernode.crm.test.user;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: 动力节点
 * 2019/9/16
 */
public class UserTest {

    public static void main(String[] args) {

        //验证失效时间
        /*String expireTime = "2018-11-10 10:10:10";

        Date date = new Date();

        *//*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentSysTime = sdf.format(date);*//*

        String currentSysTime = DateTimeUtil.getSysTime();


        int count = expireTime.compareTo(currentSysTime);

        if(count<0){

            System.out.println("账号已失效");

        }*/


        //验证锁定状态
        /*String lockState = "0";

        if("0".equals(lockState)){

            System.out.println("账号已锁定");

        }*/

        //验证ip地址
        //浏览器端的ip
        /*String ip = "192.168.1.6";

        String allowIps = "192.168.1.1,192.168.1.2,192.168.1.3";

        if(!allowIps.contains(ip)){

            System.out.println("ip地址失效");

        }*/

        //测试MD5加密
        String pwd = "BJpowernode126@.com";

        pwd = MD5Util.getMD5(pwd);

        System.out.println(pwd);


    }

}
























































