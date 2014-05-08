package com.zjhcsoft.rule.config.event;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.service.RuleGroupTaskService;
import com.zjhcsoft.rule.engine.component.RuleEngineEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-6  Time: 下午4:58
 */
@Component
@Lazy(false)
public class RuleTaskEventListener implements RuleEngineEvent {

    private RuleGroupTask ruleGroupTask;

    private static RuleGroupTaskService ruleGroupTaskService;

    public RuleTaskEventListener() {
    }

    public RuleTaskEventListener(RuleGroupTask ruleGroupTask) {
        this.ruleGroupTask = ruleGroupTask;
    }

    @Override
    public void onException(Exception ex) {
        ruleGroupTask = ruleGroupTaskService.get(ruleGroupTask.getRuleGroupTaskRowId());
        ruleGroupTask.setStatus(RuleConstants.Status.EXCEPTION);
        ruleGroupTaskService.update(ruleGroupTask);
    }

    @Inject
    public void setRuleGroupTaskService(RuleGroupTaskService ruleGroupTaskService) {
        RuleTaskEventListener.ruleGroupTaskService = ruleGroupTaskService;
    }

    @Override
    public void afterExecute(List list) {
    }

    @Override
    public void beforeExecute(List list) {
    }

    @Override
    public void setLogId(Long id) {

    }
}
