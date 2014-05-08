package com.zjhcsoft.rule.schedule.job;

import com.zjhcsoft.qin.exchange.utils.StringUtils;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroup;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.service.RuleGroupService;
import com.zjhcsoft.rule.config.service.RuleGroupTaskService;
import com.zjhcsoft.rule.job.RuleClient;
import com.zjhcsoft.rule.job.RuleClientImpl;
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

    private static RuleGroupTaskService service;

    private static RuleGroupService groupService;

    private static RuleClient ruleClient;

    @Inject
    public void setService(RuleGroupTaskService service) {
        RuleGroupJob.service = service;
    }

    @Inject
    public void setGroupService(RuleGroupService groupService) {
        RuleGroupJob.groupService = groupService;
    }

    @Inject
    public void setRuleClient(RuleClient ruleClient) {
        RuleGroupJob.ruleClient = ruleClient;
    }

    protected static enum CYCLE {
        DAY_WEEK(new SimpleDateFormat("yyyyMMdd"), Calendar.DATE), MON(new SimpleDateFormat("yyyyMM"), Calendar.MONTH);

        CYCLE(DateFormat df, int calField) {
            this.df = df;
            this.calField = calField;
        }

        private DateFormat df;

        private int calField;

        public String getDate(String preDate) {
            Calendar calendar = Calendar.getInstance();
            if (StringUtils.isNotBlank(preDate)) {
                try {
                    Date pd = df.parse(preDate);
                    calendar.setTime(pd);
                    calendar.add(calField, 1);
                } catch (ParseException e) {
                    calendar.add(calField, -1);
                }
            } else {
                calendar.add(calField, -1);
            }
            try {
                if (!calendar.getTime().before(df.parse(format(new Date())))) {
                    return null;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return df.format(calendar.getTime());
        }

        public String format(Date date) {
            return df.format(date);
        }

        public static CYCLE get(int cycle) {
            if (RuleConstants.Cycle.WEEK == cycle || RuleConstants.Cycle.DAY == cycle) {
                return DAY_WEEK;
            } else if (RuleConstants.Cycle.MONTH == cycle) {
                return MON;
            }
            return null;
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("Start RuleGroupJob Schedule");
        Long ruleGroupRowId = (Long) context.getJobDetail().getJobDataMap().get(DATA_KEY);
        RuleGroup ruleGroup = groupService.get(ruleGroupRowId);
        if (null == ruleGroup || RuleConstants.Status.NORMAL != ruleGroup.getStatus()) {
            return;
        }
        RuleGroupTask ruleGroupTask = service.queryLastTaskByGroupId(ruleGroupRowId);
        String preDate = null;
        CYCLE cycle = CYCLE.get(ruleGroup.getCycle());
        if (null != ruleGroupTask) {
            preDate = ruleGroupTask.getDateCd();
        }
        String dateToCreate = cycle.getDate(preDate);
        if (null == dateToCreate) {
            return;
        } else if (!service.isExist(ruleGroupRowId, dateToCreate)) {//防止重复调度
            //创建新任务
            RuleGroupTask newTask = new RuleGroupTask();
            newTask.setRuleGroup(ruleGroup);
            newTask.setDateCd(dateToCreate);
            newTask.setStatus(RuleConstants.Status.READY);
            newTask = service.create(newTask);

            //发布任务
            ruleClient.sendNewTask(newTask);
        }
    }
}
