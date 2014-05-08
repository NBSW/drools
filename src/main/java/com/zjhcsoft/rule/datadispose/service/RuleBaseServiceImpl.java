package com.zjhcsoft.rule.datadispose.service;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.config.service.RuleGroupTaskService;
import com.zjhcsoft.rule.config.service.RuleRelationService;
import com.zjhcsoft.rule.config.service.RuleTableDefineService;
import com.zjhcsoft.rule.datadispose.component.JDBCTemplateStore;
import com.zjhcsoft.rule.datadispose.handler.KpiHandler;
import com.zjhcsoft.rule.engine.util.EngineUtil;
import org.kie.api.definition.type.FactType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Inject;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-3  Time: 下午3:41
 */
public abstract class RuleBaseServiceImpl implements RuleService {

    private static Logger logger = LoggerFactory.getLogger(RuleBaseServiceImpl.class);

    // todo 指标定义
    protected List<RuleKpiDefine> ruleKpiDefineIterate = new ArrayList<>();

    //运行任务
    protected RuleGroupTask ruleGroupTask;

    @Override
    public void start() {
        ruleGroupTask.setStartTime(Calendar.getInstance().getTime());
        ruleGroupTask.setStatus(RuleConstants.Status.CALCULATE);
        ruleGroupTaskService.update(ruleGroupTask);
        //异步方法
        kpiHandler.executeBaseKpi(this,ruleKpiDefineIterate);
    }

    protected static RuleTableDefineService ruleTableDefineService;

    protected static RuleRelationService ruleRelationService;

    protected static RuleGroupTaskService ruleGroupTaskService;

    protected static KpiHandler kpiHandler;

    @Inject
    public void setRuleGroupTaskService(RuleGroupTaskService ruleGroupTaskService) {
        RuleBaseServiceImpl.ruleGroupTaskService = ruleGroupTaskService;
    }

    @Inject
    public void setRuleRelationService(RuleRelationService ruleRelationService) {
        RuleBaseServiceImpl.ruleRelationService = ruleRelationService;
    }

    @Inject
    public void setRuleTableDefineService(RuleTableDefineService ruleTableDefineService) {
        RuleBaseServiceImpl.ruleTableDefineService = ruleTableDefineService;
    }

    @Inject
    public void setKpiHandler(KpiHandler kpiHandler) {
        RuleBaseServiceImpl.kpiHandler = kpiHandler;
    }

    public List<RuleTableDefine> getParamTableList(RuleTableDefine ruleTableDefine) {
        return ruleTableDefineService.getParamTableList(ruleTableDefine.getTableDefineRowId());
    }

    public JdbcTemplate getJDBCTemplate(String dsCode) {
        return JDBCTemplateStore.getJDBCTemplate(dsCode);
    }

    public FactType getFactType(RuleKpiDefine kpiDefine, RuleTableDefine tableDefine) {
        return EngineUtil.getFactType(kpiDefine.getScriptRule(), kpiDefine.getRuleKpiDefineRowId(), tableDefine.getTableDefineRowId());
    }

    public void setRuleKpiDefineIterate(List<RuleKpiDefine> ruleKpiDefineIterate) {
        this.ruleKpiDefineIterate = ruleKpiDefineIterate;
    }

    public void setRuleGroupTask(RuleGroupTask ruleGroupTask) {
        this.ruleGroupTask = ruleGroupTask;
    }

    public RuleGroupTask getRuleGroupTask() {
        return ruleGroupTask;
    }
}
