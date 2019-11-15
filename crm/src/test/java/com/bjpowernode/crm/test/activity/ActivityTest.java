package com.bjpowernode.crm.test.activity;

import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Author: 动力节点
 * 2019/9/20
 */
public class ActivityTest {

    @Test
    public void testSave(){

        System.out.println("测试添加操作");

        //市场活动添加操作

        Activity a = new Activity();
        a.setId(UUIDUtil.getUUID());
        a.setName("宣传推广会");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.save(a);

        //断言
        Assert.assertEquals(flag,true);


    }
    @Test
    public void testUpdate(){

        System.out.println("测试修改操作1");

    }
    @Test
    public void testDelete(){

        System.out.println("测试删除操作");

    }
    @Test
    public void testSelect1(){

        System.out.println("测试查询操作123");

    }
    @Test
    public void testSelect2(){

        System.out.println("测试查询操作2");

    }
    @Test
    public void testSelect3(){

        System.out.println("测试查询操作3");

    }



    /*public static void main(String[] args) {

        //System.out.println("测试添加操作");

        //System.out.println("测试修改操作");

        //System.out.println("测试删除操作");

        //System.out.println("测试查询操作1");

        //System.out.println("测试查询操作2");

        //System.out.println("测试查询操作3");

        //System.out.println("测试查询操作4");

        //System.out.println("测试查询操作5");

        //System.out.println("测试查询操作6");

        //System.out.println("测试添加操作");

        //System.out.println("测试修改操作");

        //System.out.println("测试删除操作");

        //System.out.println("测试查询操作1");

        //System.out.println("测试查询操作2");

        //System.out.println("测试查询操作3");

        //System.out.println("测试查询操作4");

        //System.out.println("测试查询操作5");

        //System.out.println("测试查询操作6");

        //System.out.println("测试添加操作");

        //System.out.println("测试修改操作");

        //System.out.println("测试删除操作");

        //System.out.println("测试查询操作1");

        //System.out.println("测试查询操作2");

        //System.out.println("测试查询操作3");

        //System.out.println("测试查询操作4");

        //System.out.println("测试查询操作5");

        //System.out.println("测试查询操作6");
        //System.out.println("测试添加操作");

        //System.out.println("测试修改操作");

        //System.out.println("测试删除操作");

        //System.out.println("测试查询操作1");

        //System.out.println("测试查询操作2");

        //System.out.println("测试查询操作3");

        //System.out.println("测试查询操作4");

        //System.out.println("测试查询操作5");

        //System.out.println("测试查询操作6");
        //System.out.println("测试添加操作");

        //System.out.println("测试修改操作");

        //System.out.println("测试删除操作");

        //System.out.println("测试查询操作1");

        //System.out.println("测试查询操作2");

        //System.out.println("测试查询操作3");

        //System.out.println("测试查询操作4");

        //System.out.println("测试查询操作5");

        //System.out.println("测试查询操作6");

    }*/

}
