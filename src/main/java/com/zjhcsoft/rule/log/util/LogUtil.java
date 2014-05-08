package com.zjhcsoft.rule.log.util;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.log.entity.RuleKpiProcessLog;
import com.zjhcsoft.rule.log.service.RuleKpiProcessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * Created by XuanLubin on 2014/4/2.
 */
@Component
@Lazy(false)
public class LogUtil {
    private static RuleKpiProcessLogService service;

    @Autowired
    private void setService(RuleKpiProcessLogService service) {
        LogUtil.service = service;
    }

    public static Long createLog(RuleKpiDefine kpi, RuleGroupTask task) {
        RuleKpiProcessLog log = new RuleKpiProcessLog();

        log.setStatus(RuleConstants.Status.RUNNING);

        log.setRuleGroupRowId(task.getRuleGroupRowId());
        log.setRuleGroupName(task.getTaskName());
        log.setRuleGroupTaskRowId(task.getRuleGroupTaskRowId());
        log.setKpiCode(kpi.getKpiCode());
        log.setKpiName(kpi.getKpiName());
        log.setClassCode(kpi.getClassCode());
        log.setDateCd(task.getDateCd());
        log.setLatnId(task.getLatnId());
        log.setCommId(task.getCommId());
        log.setCommName(task.getCommName());
        log.setStartTime(Calendar.getInstance().getTime());
        log.setMessage("指标开始运行;");
        log = service.create(log);
        return log.getRuleLogRowId();
    }

    public static void changeStatus(Long logRowId, Integer status, String message,boolean isEnd) {
        service.changeLogStatus(logRowId, status, message,isEnd);
    }

    public static boolean checkIsDone(Long logRowId) {
        RuleKpiProcessLog log = service.get(logRowId);
        if (log.getStatus() != RuleConstants.Status.RUNNING) {
            return true;
        } else {
            return false;
        }
    }
}
