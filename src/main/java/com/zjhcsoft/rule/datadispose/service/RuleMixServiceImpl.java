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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger logger = LoggerFactory.getLogger(RuleMixServiceImpl.class);

    private static RuleKpiProcessLogService logService;

    private static RuleKpiDefineService ruleKpiDefineService;

    @Override
    public void runMixKpi(List mixKpiIds) {
        outerLoop:
        for (Object id : mixKpiIds) {
            logger.debug("确认组合指标-依赖完成请况 KPI——RowId:{}", id);
            List<Long> requiredBaseKpi = ruleRelationService.queryIdByFromIdRelType((Long) id, new String[]{RuleConstants.Type.MIX_BASE_KPI_REL, RuleConstants.Type.MIX_BASE_KPI_REL_2});
            logger.debug("依赖KPI ：{}", requiredBaseKpi.toString());
            for (Long baseKpi : requiredBaseKpi) {
                RuleKpiDefine kpiDefine = ruleKpiDefineService.get(baseKpi);
                if (null != kpiDefine) {
                    boolean success = logService.isFinish(kpiDefine.getKpiCode(), getRuleGroupTask().getDateCd());
                    if (!success) {
                        logger.debug("依赖KPI ：{} 未完成", baseKpi);
                        continue outerLoop;
                    }
                }
            }

            RuleKpiDefine ready2execute = ruleKpiDefineService.get((Long) id);
            //异步方法调用处理组合指标
            //获得KPI运行锁
            if (KpiRunLock.getLock(ready2execute.getKpiCode(), getRuleGroupTask().getDateCd())) {
                logger.debug("成功获得运行锁 {} {}", ready2execute.getKpiCode(), getRuleGroupTask().getDateCd());
                kpiHandler.executeMixKpi(ruleKpiDefineService.get((Long) id), this);
            } else {
                logger.debug("运行锁获得失败 {} {}", ready2execute.getKpiCode(), getRuleGroupTask().getDateCd());
            }
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
