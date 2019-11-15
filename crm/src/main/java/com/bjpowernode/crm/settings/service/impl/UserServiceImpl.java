package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: 动力节点
 * 2019/9/16
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        Map<String,String> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        User user = userDao.login(map);

        if(user==null){

            throw new LoginException("账号密码不正确");

        }

        //如果代码能够顺利的走到该行，说明上面的异常没有抛出，说明账号密码是正确的
        //需要继续验证下面的其他信息

        //验证失效时间
        String expireTime = user.getExpireTime();
        if(expireTime.compareTo(DateTimeUtil.getSysTime())<0){

            throw new LoginException("账号已失效");

        }

        //验证锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){

            throw new LoginException("账号已锁定");

        }

        //验证ip地址
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){

            throw new LoginException("ip地址受限");

        }


        //如果程序能够走到这一行，说明上面所有验证都正确，所有throw都没有执行
        //返回user对象
        return user;
    }

    @Override
    public List<User> getUserList() {

        List<User> uList = userDao.getUserList();

        return uList;
    }
}






































