package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: 动力节点
 * 2019/9/19
 */
public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);


    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String, List<DicValue>> map = new HashMap<>();

        //取得字典类型列表
        List<DicType> dtList = dicTypeDao.getTypeList();

        //遍历所有的字典类型
        for(DicType dt:dtList){

            //取得每一种类型的字典类型编码
            String code = dt.getCode();

            //根据字典类型编码却对应的字典值列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);

            map.put(code+"List", dvList);

        }

        return map;
    }
}





























