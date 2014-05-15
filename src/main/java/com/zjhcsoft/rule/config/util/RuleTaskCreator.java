package com.zjhcsoft.rule.config.util;

import com.zjhcsoft.qin.exchange.utils.StringUtils;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroup;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.service.RuleGroupTaskService;
import com.zjhcsoft.rule.job.RuleClient;
import com.zjhcsoft.rule.schedule.TaskSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by XuanLubin on 2014/5/13. 8:55
 */
@Component
public class RuleTaskCreator {

    private Logger logger = LoggerFactory.getLogger(RuleTaskCreator.class);

    @Inject
    private RuleGroupTaskService service;

    public String create(RuleGroup ruleGroup){
        String msg;
        if (null == ruleGroup || RuleConstants.Status.NORMAL != ruleGroup.getStatus()) {
            msg = "主题处于非正常状态,不能生成任务";
            logger.error(msg);
            return msg;
        }
        Long ruleGroupRowId = ruleGroup.getRuleGroupRowId();
        RuleGroupTask ruleGroupTask = service.queryLastTaskByGroupId(ruleGroupRowId);
        String preDate = null;
        CYCLE cycle = CYCLE.get(ruleGroup.getCycle());
        if (null != ruleGroupTask) {
            preDate = ruleGroupTask.getDateCd();
        }
        String dateToCreate = cycle.getDate(preDate);
        if (null == dateToCreate) {
            msg = "主题发布时间不能创建";
            logger.error(msg);
            return msg;
        } else if (!service.isExist(ruleGroupRowId, dateToCreate)) {//防止重复调度
            //创建新任务
            RuleGroupTask newTask = new RuleGroupTask();
            newTask.setRuleGroup(ruleGroup);
            newTask.setDateCd(dateToCreate);
            newTask.setStatus(RuleConstants.Status.READY);
            service.create(newTask);
        }else {
            msg = "当前账期任务存在";
            logger.debug(msg);
            return msg;
        }
        msg = "任务创建成功";
        logger.debug(msg);
        return msg;
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
}
