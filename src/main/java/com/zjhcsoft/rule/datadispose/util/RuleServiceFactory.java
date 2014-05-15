package com.zjhcsoft.rule.datadispose.util;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.service.RuleKpiDefineService;
import com.zjhcsoft.rule.config.service.RuleRelationService;
import com.zjhcsoft.rule.datadispose.service.RuleBaseServiceImpl;
import com.zjhcsoft.rule.datadispose.service.RuleService;
import com.zjhcsoft.rule.datadispose.service.RuleServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-3  Time: 下午4:07
 */
@Component
@Lazy(false)
public class RuleServiceFactory {

    private static Logger logger = LoggerFactory.getLogger(RuleServiceFactory.class);

    private static RuleRelationService ruleRelationService;

    private static RuleKpiDefineService ruleKpiDefineService;

    @Inject
    public void setRuleKpiDefineService(RuleKpiDefineService ruleKpiDefineService) {
        RuleServiceFactory.ruleKpiDefineService = ruleKpiDefineService;
    }

    @Inject
    public void setRuleRelationService(RuleRelationService ruleRelationService) {
        RuleServiceFactory.ruleRelationService = ruleRelationService;
    }


    public static RuleService createBaseKpiService(RuleGroupTask ruleGroupTask) {
        List<Long> ruleKpiList = ruleRelationService.queryIdByFromIdRelType(ruleGroupTask.getRuleGroup().getRuleGroupRowId(), new String[]{RuleConstants.Type.RULE_GROUP_BASE_RULE_KPI});
        if (!ruleKpiList.isEmpty()) {
            RuleBaseServiceImpl service = new RuleServiceImpl();
            service.setRuleGroupTask(ruleGroupTask);
            List<RuleKpiDefine> kpiDefineList = ruleKpiDefineService.findByPkIds(ruleKpiList);
            service.setRuleKpiDefineIterate(kpiDefineList);
            return service;
        }
        logger.error("Service 实例创建失败 未找到对应的 RuleKpiDefine Task:{}", ruleGroupTask);
        return null;
    }
}
