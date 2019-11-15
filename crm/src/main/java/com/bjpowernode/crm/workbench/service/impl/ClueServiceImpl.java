package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Author: 动力节点
 * 2019/9/19
 */
public class ClueServiceImpl implements ClueService {

    //线索相关表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    //客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    //联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    //交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean save(Clue c) {

        boolean flag = true;

        int count = clueDao.save(c);

        if(count!=1){

            flag = false;

        }

        return flag;
    }

    @Override
    public Clue detail(String id) {

        Clue c = clueDao.detail(id);

        return c;
    }

    @Override
    public boolean unbund(String id) {

        boolean flag = true;

        int count = clueActivityRelationDao.delete(id);

        if(count!=1){

            flag = false;

        }

        return flag;
    }

    @Override
    public boolean bund(String clueId, String[] activityIds) {

        boolean flag = true;

        for(String activityId:activityIds){

            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(clueId);
            car.setActivityId(activityId);

            int count = clueActivityRelationDao.save(car);
            if(count!=1){
                flag = false;
            }

        }

        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {

        boolean flag = true;

        //开始执行线索转换业务

        /*

            1.通过clueId查询得到线索的单条信息（根据id查单条）
                查询线索单条数据的目的，是为了将线索中的信息取得
                将来，会从线索对象中抽取出与公司相关的信息生成一个客户
                        从线索对象中抽取出与人相关的信息生成一个联系人

         */
        Clue c = clueDao.getById(clueId);

        /*

            2.根据以上线索对象c，取得公司名称 c.getCompany()
                根据该线索中的公司名称到客户表中去进行查询操作，看看有没有这个客户
                注意：查询客户要求按照名称 精确匹配（用=） ，而不是模糊匹配（不用like）
                根据查询结果，如果有此客户，则不新建客户
                            如果没有此客户，则新建一个客户

                分析：查询客户的sql语句

                    int count = 执行select count(*) from tbl_customer where name=#{company}
                    count:0 没有此客户
                    count:1 有此客户
                    以上操作仅仅只是能够查询出来有没有这个客户
                    但是将来如果要使用这个客户的信息（id）的时候，我们就没有办法拿到这个信息了

                    我们选择使用该形式：只有查询出来对象本身，才能取得里面的信息来使用
                    Customer cus = 执行select * from tbl_customer where name=#{company}
                    cus：null 没有此客户
                    cus：不是null 有此客户
                    这种做法的好处，如果查询到了这个客户，我们可以直接通过cus对象来取得里面所有的信息来使用


         */

        String company = c.getCompany();
        Customer cus = customerDao.getCustomerByName(company);

        //如果cus为null，需要创建一个客户
        if(cus==null){

            //添加客户
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setOwner(c.getOwner());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setName(company);
            cus.setDescription(c.getDescription());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setCreateBy(createBy);
            cus.setContactSummary(c.getContactSummary());
            cus.setAddress(c.getAddress());

            int count1 = customerDao.save(cus);
            if(count1!=1){
                flag = false;
            }

        }

        /*

            以上我们是处理了客户
            如果下面的操作需要使用到客户的id
            我们使用cus.getId()就可以了

         */

        //3.从c对象中抽取出与人相关的信息，生成一个联系人
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setSource(c.getSource());
        con.setOwner(c.getOwner());
        con.setNextContactTime(c.getNextContactTime());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setFullname(c.getFullname());
        con.setEmail(c.getEmail());
        con.setDescription(c.getDescription());
        con.setCustomerId(cus.getId());
        con.setCreateTime(DateTimeUtil.getSysTime());
        con.setCreateBy(createBy);
        con.setContactSummary(c.getContactSummary());
        con.setAppellation(c.getAppellation());
        con.setAddress(c.getAddress());

        int count2 = contactsDao.save(con);
        if(count2!=1){
            flag = false;
        }

        /*

            以上我们是处理了联系人
            以下的操作如果使用到联系人的id
            我们使用con.getId()就可以了

         */

        /*

            4.查询得到与该线索关联的备注信息列表
                将备注信息，备份（转换）到客户备注以及联系人的备注中去

         */
        List<ClueRemark> clueRemarkList = clueRemarkDao.getRemarkListByClueId(clueId);

        //遍历每一个线索备注
        for(ClueRemark clueRemark:clueRemarkList){

            //取得每一个备注信息
            String noteContent = clueRemark.getNoteContent();

            //生成客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setCreateTime(DateTimeUtil.getSysTime());
            customerRemark.setCreateBy(createBy);
            int count3 = customerRemarkDao.save(customerRemark);
            if(count3!=1){
                flag = false;
            }

            //生成联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setEditFlag("0");
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
            contactsRemark.setCreateBy(createBy);
            int count4 = contactsRemarkDao.save(contactsRemark);
            if(count4!=1){
                flag = false;
            }

        }

        /*

            5.查询得到与该线索相关的线索市场活动关联关系列表
                将原有的与线索相关联的市场活动
                转换到与联系人做关联

         */
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);

        //遍历每一个关联关系
        for(ClueActivityRelation clueActivityRelation:clueActivityRelationList){

            //取出每一个关联的市场活动
            String activityId = clueActivityRelation.getActivityId();

            //使每一个市场活动与联系人做新的关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if(count5!=1){
                flag = false;
            }
        }

        /*

            6.如果t不为null，我们需要在线索转换的同时，创建一笔交易

         */
        if(t!=null){

            /*

                t已经在controller中封装了一些信息
                    id,createBy,createTime,money,name,stage,activityId,expectedDate

                其他的信息，我们通过以上转换得到的信息，尽量去为t填充

             */

            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setNextContactTime(c.getNextContactTime());
            t.setDescription(c.getDescription());
            t.setCustomerId(cus.getId());
            t.setContactSummary(c.getContactSummary());
            t.setContactsId(con.getId());

            int count6 = tranDao.save(t);
            if(count6!=1){
                flag = false;
            }

            //7.交易的创建，伴随着生产一条交易历史
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setTranId(t.getId());
            th.setStage(t.getStage());
            th.setMoney(t.getMoney());
            th.setExpectedDate(t.getExpectedDate());
            th.setCreateTime(DateTimeUtil.getSysTime());
            th.setCreateBy(createBy);

            int count7 = tranHistoryDao.save(th);
            if(count7!=1){
                flag = false;
            }

        }


        //8.删除线索关联的备注
        for(ClueRemark clueRemark:clueRemarkList){

            int count8 = clueRemarkDao.delete(clueRemark);
            if(count8!=1){
                flag = false;
            }
        }

        //9.删除线索与市场活动的关联关系
        for(ClueActivityRelation clueActivityRelation:clueActivityRelationList){

            int count9 = clueActivityRelationDao.delete(clueActivityRelation.getId());
            if(count9!=1){
                flag = false;
            }

        }

        //10.删除线索
        int count10 = clueDao.delete(clueId);
        if(count10!=1){
            flag = false;
        }


        return flag;
    }


}





















