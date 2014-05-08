package com.zjhcsoft.rule.config.service.impl;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.config.service.RuleGroupService;
import com.zjhcsoft.rule.config.service.RuleTableDefineService;
import com.zjhcsoft.test.BaseTest2;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-5  Time: 下午3:26
 */
public class RuleTableDefineServiceImplTest extends BaseTest2 {

    @Inject
    RuleTableDefineService ruleTableDefineService;

    @Inject
    RuleGroupService ruleGroupService;

    @Test
    public void testFetchByRowId() throws Exception {

    }

    @Test
    public void testQueryByTableDefineRowIds() throws Exception {

    }

    @Test
    public void testCreate() throws Exception {
        RuleTableDefine define = new RuleTableDefine();
        define.setDsCode("8a8a8aef448fce4901448fd41aa40004");
        define.setCommName("浙江");
        define.setLatnId("Z");
        define.setCreateDate(Calendar.getInstance().getTime());
        define.setCreateUserName("门户管理员");
        define.setCreateUserRowId(1330l);
        define.setDateField("STAT_CYCLE_ID");
        define.setDimField("");
        define.setType(RuleConstants.Type.PARAM_MODEL);
        define.setTableCode("T_SALES_DETAIL");
        define.setTableName("渠道经理清单表2");
        define.setRemark("渠道经理清单表2~~~~~~~~");
        define.setFields("{'columns':['','','','','','','','','','']}");
        define.setRelField("{'columns':['','','','','','','','','','']}");
        ruleTableDefineService.create(define);
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
