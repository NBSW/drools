package com.zjhcsoft.rule.config.service.impl;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.service.RuleKpiDefineService;
import com.zjhcsoft.test.BaseTest2;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-5  Time: 下午3:26
 */
public class RuleKpiDefineServiceImplTest extends BaseTest2 {

    @Inject
    RuleKpiDefineService ruleKpiDefineService;

    @Test
    public void testCreate() throws Exception {
        RuleKpiDefine ruleKpiDefine = new RuleKpiDefine();
        ruleKpiDefine.setClassCode("KM00002");
        ruleKpiDefine.setKpiCode("ZB000002");
        ruleKpiDefine.setCommName("浙江");
        ruleKpiDefine.setCreateDate(Calendar.getInstance().getTime());
        ruleKpiDefine.setCreateUserName("门户管理员");
        ruleKpiDefine.setCreateUserRowId(1330l);
        ruleKpiDefine.setKpiName("测试指标0002");
        ruleKpiDefine.setDeadline(Calendar.getInstance().getTime());
        ruleKpiDefine.setLatnId("Z");
        ruleKpiDefine.setStatus(RuleConstants.Status.NORMAL);
        ruleKpiDefine.setScriptRule("");
        ruleKpiDefineService.create(ruleKpiDefine);
    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testFindByPkId() throws Exception {

    }

    @Test
    public void testFindByPkIds() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testFindAll() throws Exception {

    }
}
