package com.zjhcsoft.rule.datadispose.service;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.config.service.RuleKpiDefineService;
import com.zjhcsoft.rule.datadispose.util.JDBCFetchUtil;
import com.zjhcsoft.rule.engine.util.FactTypeColumnUtil;
import com.zjhcsoft.rule.log.service.RuleKpiProcessLogService;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.definition.type.FactType;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-3  Time: 下午3:38
 */
public abstract class RuleMixServiceImpl extends RuleBaseServiceImpl {

    private List<Long> dealMixKpi = new ArrayList<>();

    private static RuleKpiProcessLogService logService;

    private static RuleKpiDefineService ruleKpiDefineService;

    @Override
    public void runMixKpi(List mixKpiIds) {
        outerLoop:
        for (Object id : mixKpiIds) {
            List<Long> requiredBaseKpi = ruleRelationService.queryIdByFromIdRelType((Long) id, new String[]{RuleConstants.Type.MIX_BASE_KPI_REL});
            for (Long baseKpi : requiredBaseKpi) {
                RuleKpiDefine kpiDefine = ruleKpiDefineService.get(baseKpi);
                if (null != kpiDefine) {
                    boolean success = logService.isFinish(kpiDefine.getKpiCode(), getRuleGroupTask().getDateCd());
                    if (!success) {
                        continue outerLoop;
                    }
                }
            }
            dealMixKpi.add((Long) id);

            //异步方法调用处理组合指标
            kpiHandler.executeMixKpi(ruleKpiDefineService.get((Long) id), this);
        }
    }

    @Inject
    public void setRuleKpiDefineService(RuleKpiDefineService ruleKpiDefineService) {
        RuleMixServiceImpl.ruleKpiDefineService = ruleKpiDefineService;
    }

    @Inject
    public void setLogService(RuleKpiProcessLogService logService) {
        RuleMixServiceImpl.logService = logService;
    }
}
