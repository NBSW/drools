package com.zjhcsoft.rule.schedule;

import com.zjhcsoft.qin.exchange.utils.PropertyHelper;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.common.util.BaseSchedule;
import com.zjhcsoft.rule.config.entity.RuleGroup;
import com.zjhcsoft.rule.config.service.RuleGroupService;
import com.zjhcsoft.rule.config.service.RuleGroupTaskService;
import com.zjhcsoft.rule.config.service.RuleRelationService;
import com.zjhcsoft.rule.job.RuleClient;
import com.zjhcsoft.rule.schedule.job.RuleGroupJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-11  Time: 下午3:59
 */
@Component
@Lazy(false)
public class TaskSchedule extends BaseSchedule {

    private static Logger logger = LoggerFactory.getLogger(TaskSchedule.class);

    @Inject
    private RuleGroupService service;

    @Inject
    private RuleRelationService relationService;

    //任务生产主机？
    private boolean isMaster = Boolean.parseBoolean(PropertyHelper.get("ruleGroup.master"));

    @Override
    @Scheduled(cron = "0 0/10 * * * ?")
    public void roll() {
        //非任务发布主机不发布任务
        if (!isMaster) {
            return;
        }
        List<RuleGroup> ruleGroupList = service.findAllByStatus(RuleConstants.Status.NORMAL, null);
        for (final RuleGroup ruleGroup : ruleGroupList) {
            List<Long> baseKpi = relationService.queryIdByFromIdRelType(ruleGroup.getRuleGroupRowId(),new String[]{RuleConstants.Type.RULE_GROUP_BASE_RULE_KPI});
            if(baseKpi.isEmpty()){
                logger.debug("没有基础指标");
                continue;
            }
            if (RuleConstants.Status.NORMAL != ruleGroup.getStatus()) {
                deleteJob(RuleGroupJob.class, ruleGroup.getRuleGroupRowId());
            } else {
                Map<String, Object> model = new HashMap<>();
                model.put(RuleGroupJob.DATA_KEY, ruleGroup.getRuleGroupRowId());
                create(model, ruleGroup.getCreateCron(), RuleGroupJob.class, ruleGroup.getRuleGroupRowId());
            }
        }
    }

}