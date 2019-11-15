package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;

import javax.servlet.http.HttpServletRequest;

/**
 * Author: 动力节点
 * 2019/9/19
 */
public interface ClueService {
    boolean save(Clue c);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String clueId, String[] activityIds);

    boolean convert(String clueId, Tran t, String createBy);
}
