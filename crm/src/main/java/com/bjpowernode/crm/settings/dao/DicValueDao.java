package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;

/**
 * Author: 动力节点
 * 2019/9/19
 */
public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
