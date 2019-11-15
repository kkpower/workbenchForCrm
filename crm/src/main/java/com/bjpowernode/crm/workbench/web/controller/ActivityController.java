package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.*;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: 动力节点
 * 2019/9/16
 */
public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到市场活动模块控制器");

        String path = request.getServletPath();

        if("/workbench/activity/getUserList.do".equals(path)){

            getUserList(request,response);

        }else if("/workbench/activity/save.do".equals(path)){

            save(request,response);

        }else if("/workbench/activity/pageList.do".equals(path)){

            pageList(request,response);

        }else if("/workbench/activity/delete.do".equals(path)){

            delete(request,response);

        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){

            getUserListAndActivity(request,response);

        }else if("/workbench/activity/update.do".equals(path)){

            update(request,response);

        }else if("/workbench/activity/detail.do".equals(path)){

            detail(request,response);

        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)){

            getRemarkListByAid(request,response);

        }else if("/workbench/activity/deleteRemark.do".equals(path)){

            deleteRemark(request,response);

        }else if("/workbench/activity/saveRemark.do".equals(path)){

            saveRemark(request,response);

        }else if("/workbench/activity/updateRemark.do".equals(path)){

            updateRemark(request,response);

        }
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行修改备注的操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditFlag(editFlag);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.updateRemark(ar);

        Map<String,Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("ar", ar);

        PrintJson.printJsonObj(response, map);


    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行添加备注的操作");

        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityId);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.saveRemark(ar);

        Map<String,Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("ar", ar);

        PrintJson.printJsonObj(response, map);


    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行删除备注的操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.deleteRemark(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据市场活动id，查询备注信息列表");

        String activityId = request.getParameter("activityId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> arList = as.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(response, arList);


    }

    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("跳转到详细信息页");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity a = as.detail(id);

        request.setAttribute("a", a);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动的修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //修改时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人：当前登录用户名称
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditBy(editBy);
        a.setEditTime(editTime);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.update(a);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户列表和市场活动单条操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*

            service返回

            uList
            a



         */
        Map<String,Object> map = as.getUserListAndActivity(id);

        PrintJson.printJsonObj(response, map);


    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动的删除操作");

        String[] ids = request.getParameterValues("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.delete(ids);

        PrintJson.printJsonFlag(response, flag);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动信息列表（结合条件查询+分页查询）");

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        //页码
        String pageNoStr = request.getParameter("pageNo");
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");

        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);

        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*

            controller管service要什么
            完全在于前端管controller要什么

            前端管controller要dataList和total
            那么controller就应该管service要dataList和total

            业务层需要为controller返回两项信息
            dataList
            total

            下层往上层同时返回多项信息，要么返map，要么返vo

         */
        PaginationVo<Activity> vo = as.pageList(map);

        PrintJson.printJsonObj(response, vo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动的添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户名称
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateBy(createBy);
        a.setCreateTime(createTime);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.save(a);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response, uList);

    }


}










































