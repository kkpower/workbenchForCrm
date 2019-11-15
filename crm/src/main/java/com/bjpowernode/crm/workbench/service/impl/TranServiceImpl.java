package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;

import java.util.List;
import java.util.Map;

/**
 * Author: 动力节点
 * 2019/9/21
 */
public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public boolean save(Tran t, String customerName) {

        /*

            1.根据客户名称到客户表查询有没有次客户（使用等于号进行名称的精确匹配）
                如果有此客户，将客户查出来，取其id
                如果没有此客户，创建一个新客户，取其id

            2.将客户的id封装到t中，添加交易

            3.添加一条交易历史

         */

        boolean flag = true;

        Customer cus = customerDao.getCustomerByName(customerName);

        if(cus==null){

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setOwner(t.getOwner());
            cus.setNextContactTime(t.getNextContactTime());
            cus.setName(customerName);
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setCreateBy(t.getCreateBy());

            int count1 = customerDao.save(cus);
            if(count1!=1){
                flag = false;
            }


        }

        //添加交易
        t.setCustomerId(cus.getId());
        int count2 = tranDao.save(t);
        if(count2!=1){
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getCreateBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());
        th.setTranId(t.getId());

        int count3 = tranHistoryDao.save(th);
        if(count3!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {

        List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(tranId);

        return thList;
    }

    @Override
    public boolean changeStage(Tran t) {

        boolean flag = true;

        //修改阶段
        int count1 = tranDao.changeStage(t);
        if(count1!=1){
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getEditBy());
        int count2 = tranHistoryDao.save(th);
        if(count2!=1){
            flag = false;
        }


        return flag;
    }

    @Override
    public PaginationVo<Map<String, Object>> getCharts() {

        //取得total
        int total = tranDao.getTotal();

        //取得dataList
        List<Map<String,Object>> dataList = tranDao.getCharts();

        //创建一个vo对象，将total和dataList封装到vo对象中
        PaginationVo<Map<String,Object>> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //返回vo对象
        return vo;
    }
}






































































