package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
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
public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到交易模块控制器");

        String path = request.getServletPath();

        if("/workbench/transaction/add.do".equals(path)){

            add(request,response);

        }else if("/workbench/transaction/getCustomerNames.do".equals(path)){

            getCustomerNames(request,response);

        }else if("/workbench/transaction/save.do".equals(path)){

            save(request,response);

        }else if("/workbench/transaction/detail.do".equals(path)){

            detail(request,response);

        }else if("/workbench/transaction/getHistoryListByTranId.do".equals(path)){

            getHistoryListByTranId(request,response);

        }else if("/workbench/transaction/changeStage.do".equals(path)){

            changeStage(request,response);

        }else if("/workbench/transaction/getCharts.do".equals(path)){

            getCharts(request,response);

        }



    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得统计图表数据");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        /*

            int total
            List<Map> dataList

            vo

         */
        PaginationVo<Map<String,Object>> vo = ts.getCharts();

        PrintJson.printJsonObj(response, vo);


    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到改变阶段的操作");

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.changeStage(t);

        //处理可能性
        Map<String,String> pMap = (Map<String,String>)this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        Map<String,Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("t", t);

        PrintJson.printJsonObj(response, map);


    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据交易id，取得对应的交易历史列表");

        String tranId = request.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> thList = ts.getHistoryListByTranId(tranId);

        Map<String,String> pMap = (Map<String,String>)this.getServletContext().getAttribute("pMap");

        for(TranHistory th:thList){

            th.setPossibility(pMap.get(th.getStage()));

        }

        PrintJson.printJsonObj(response, thList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("跳转到交易详细信息页");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = ts.detail(id);

        //阶段
        String stage = t.getStage();
        //阶段和可能性之间的对应关系
        ServletContext application = this.getServletContext();
        Map<String,String> pMap = (Map<String,String>)application.getAttribute("pMap");
        //取得可能性
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        request.setAttribute("t", t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);

    }

    private void save(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("执行交易添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName"); //对于客户，我们现在只有name，还没有id
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setNextContactTime(nextContactTime);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setDescription(description);
        t.setContactSummary(contactSummary);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(t,customerName);

        if(flag){

            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");

        }


    }

    private void getCustomerNames(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据客户名称关键字模糊查询客户名称列表");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerNames(name);

        PrintJson.printJsonObj(response, sList);

    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {

        System.out.println("跳转到交易添加页");

        //取得用户信息列表
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        request.setAttribute("uList", uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);

    }


}










































