package com.zjhcsoft.rule.result.handler;

import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.handler.RuleMixKpiHandler;
import com.zjhcsoft.rule.result.service.RuleKpiResultSummaryService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by XuanLubin on 2014/4/2. 11:23
 */
public class ResultSummaryHandler {

    @Inject
    private RuleKpiResultSummaryService summaryService;

    @Inject
    private RuleMixKpiHandler ruleMixKpiHandler;

    @Async("summaryTaskExecutor")
    public Future<List<Long>> execute(RuleGroupTask task, RuleKpiDefine kpi) {

        //判断指标是否需要做汇总操作
        if(kpi.isSummary()) {
            summaryService.summaryDbSide(kpi.getKpiCode(), task.getDateCd());
        }

        // todo 汇总完成 call 组合指标
        //查询依赖规则
        return ruleMixKpiHandler.fetchMixKpi(task,kpi);
    }
}
