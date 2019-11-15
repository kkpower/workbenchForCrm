package com.bjpowernode.crm.vo;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;

/**
 * Author: 动力节点
 * 2019/9/17
 */
public class PaginationVo<T> {

    private List<T> dataList;
    private int total;

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
