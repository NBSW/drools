package com.zjhcsoft.rule.config.handler;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleRelation;
import com.zjhcsoft.rule.config.service.RuleGroupTaskService;
import com.zjhcsoft.rule.config.service.RuleKpiDefineService;
import com.zjhcsoft.rule.config.service.RuleRelationService;
import com.zjhcsoft.rule.log.entity.RuleKpiProcessLog;
import com.zjhcsoft.rule.log.service.RuleKpiProcessLogService;
import com.zjhcsoft.rule.log.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-10  Time: 下午3:04
 * 任务运行状态处理
 */
@Component
public class RuleGroupTaskResultHandler {

    private static Logger logger = LoggerFactory.getLogger(RuleGroupTaskResultHandler.class);

    @Async("groupTaskExecutor")
    public void execute(RuleGroupTask ruleGroupTask) {

        List<Long> ruleIdList = relationService.queryIdByFromIdRelType(ruleGroupTask.getRuleGroupRowId(), new String[]{RuleConstants.Type.RULE_GROUP_MIX_RULE_KPI, RuleConstants.Type.RULE_GROUP_BASE_RULE_KPI});
        for (Long rule : ruleIdList) {
            RuleKpiDefine define = kpiDefineService.get(rule);
            if (!logService.isFinish(ruleGroupTask.getDateCd(), define.getKpiCode())) {
                return;
            }
        }
        //todo 更新任务状态
        ruleGroupTask = service.get(ruleGroupTask.getRuleGroupTaskRowId());
        if (RuleConstants.Status.EXCEPTION != ruleGroupTask.getStatus()) {
            ruleGroupTask.setStatus(RuleConstants.Status.COMPLETED);
        }
        ruleGroupTask.setFinishTime(Calendar.getInstance().getTime());
        ruleGroupTask = service.update(ruleGroupTask);
        logger.debug("任务 ：{} ,耗时:开始{}  结束{}", ruleGroupTask, ruleGroupTask.getStartTime(), ruleGroupTask.getFinishTime());
    }

    @Inject
    private RuleGroupTaskService service;

    @Inject
    private RuleRelationService relationService;

    @Inject
    private RuleKpiProcessLogService logService;

    @Inject
    private RuleKpiDefineService kpiDefineService;
}
