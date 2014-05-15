package com.zjhcsoft.rule.config.handler;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.service.RuleRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by XuanLubin on 2014/5/8. 14:35
 */
@Component
public class RuleMixKpiHandler {

    private static final Logger logger = LoggerFactory.getLogger(RuleMixKpiHandler.class);

    @Inject
    private RuleRelationService relationService;

    public Future<List<Long>> fetchMixKpi(RuleGroupTask task, RuleKpiDefine kpi){
        List<Long> mixKpi = relationService.queryIdByTargetIdRelType(kpi.getRuleKpiDefineRowId(), new String[]{RuleConstants.Type.MIX_BASE_KPI_REL, RuleConstants.Type.MIX_BASE_KPI_REL_2});
        logger.debug("活动{} KPI {} 账期 {} 完成 查询组合指标  组合指标条数:{} {}", task.getTaskName(), kpi.getKpiCode(), task.getDateCd(), mixKpi.size(),mixKpi.toString());
        if (mixKpi.size() > 0) {
            return new AsyncResult<>(mixKpi);
        } else {
            return new AsyncResult<>(null);
        }
    }
}
