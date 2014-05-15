package com.zjhcsoft.rule.schedule.job;

import com.zjhcsoft.qin.exchange.utils.StringUtils;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroup;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.service.RuleGroupService;
import com.zjhcsoft.rule.config.service.RuleGroupTaskService;
import com.zjhcsoft.rule.config.util.RuleTaskCreator;
import com.zjhcsoft.rule.job.RuleClient;
import com.zjhcsoft.rule.job.RuleClientImpl;
import com.zjhcsoft.rule.schedule.TaskSchedule;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-11  Time: 下午5:15
 */
@Component
@Lazy(false)
public class RuleGroupJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(RuleGroupJob.class);

    public static final String DATA_KEY = "RuleGroup";


    private static RuleGroupService groupService;

    private static RuleTaskCreator taskCreator;

    @Inject
    public void setGroupService(RuleGroupService groupService) {
        RuleGroupJob.groupService = groupService;
    }

    @Inject
    public void setTaskCreator(RuleTaskCreator taskCreator) {
        RuleGroupJob.taskCreator = taskCreator;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("Start RuleGroupJob Schedule");
        Long ruleGroupRowId = (Long) context.getJobDetail().getJobDataMap().get(DATA_KEY);
        RuleGroup ruleGroup = groupService.get(ruleGroupRowId);
        if (null == ruleGroup || RuleConstants.Status.NORMAL != ruleGroup.getStatus()) {
            //非正常状态的任务 停止调度
            TaskSchedule.deleteJob(this.getClass(),ruleGroupRowId);
            return;
        }
        taskCreator.create(ruleGroup);
    }
}
