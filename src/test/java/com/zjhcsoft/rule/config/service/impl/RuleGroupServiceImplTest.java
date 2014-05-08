package com.zjhcsoft.rule.config.service.impl;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroup;
import com.zjhcsoft.rule.config.service.RuleGroupService;
import com.zjhcsoft.test.BaseTest2;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-5  Time: 上午10:13
 */
public class RuleGroupServiceImplTest extends BaseTest2 {

    @Inject
    RuleGroupService ruleGroupService;

    @Test
    public void testCreate() throws Exception {
        RuleGroup ruleGroup = new RuleGroup();
        ruleGroup.setCreateDate(Calendar.getInstance().getTime());
        ruleGroup.setCommId("1");
        ruleGroup.setCommName("浙江");
        ruleGroup.setCreateCron("0 0 0 3 * * ?");
        ruleGroup.setCreateUserName("门户管理员");
        ruleGroup.setCreateUserRowId(1330l);
        ruleGroup.setCycle(RuleConstants.Cycle.DAY);
        ruleGroup.setLatnId("Z");
        ruleGroup.setStatus(RuleConstants.Status.NORMAL);
        ruleGroup.setTaskCode(UUID.randomUUID().toString().replaceAll("-", ""));
        ruleGroup.setTaskName("测试主题1234132123");
        ruleGroupService.create(ruleGroup);
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
