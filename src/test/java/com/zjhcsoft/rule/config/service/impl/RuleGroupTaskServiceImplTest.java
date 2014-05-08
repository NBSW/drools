package com.zjhcsoft.rule.config.service.impl;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroup;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.service.RuleGroupService;
import com.zjhcsoft.rule.config.service.RuleGroupTaskService;
import com.zjhcsoft.test.BaseTest2;
import org.junit.Test;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-5  Time: 上午10:51
 */
public class RuleGroupTaskServiceImplTest extends BaseTest2 {

    @Inject
    RuleGroupTaskService ruleGroupTaskService;

    @Inject
    RuleGroupService ruleGroupService;

    @Test
    public void testQueryByStatus() throws Exception {

    }

    @Test
    public void testQueryByRuleGroupRowId() throws Exception {

    }

    @Test
    public void testCreate() throws Exception {
        Iterable<RuleGroup> ruleGroups = ruleGroupService.findAll(null);
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        for (RuleGroup group : ruleGroups) {
            RuleGroupTask groupTask = new RuleGroupTask();
            groupTask.setRuleGroup(group);
            groupTask.setDateCd(format.format(Calendar.getInstance().getTime()));
            groupTask.setStatus(RuleConstants.Status.CALCULATE);
            //group.getRuleGroupTaskList().add(groupTask);
            //ruleGroupService.update(group);
            ruleGroupTaskService.create(groupTask);
        }
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
        Iterable<RuleGroupTask> ruleGroupTasks = ruleGroupTaskService.findAll(null);
        System.out.println();
    }
}
