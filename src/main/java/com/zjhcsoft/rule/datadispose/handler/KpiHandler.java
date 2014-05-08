package com.zjhcsoft.rule.datadispose.handler;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.config.handler.RuleGroupTaskResultHandler;
import com.zjhcsoft.rule.config.handler.RuleKpiRunStatusHandler;
import com.zjhcsoft.rule.datadispose.component.JDBCTemplateStore;
import com.zjhcsoft.rule.datadispose.service.RuleBaseServiceImpl;
import com.zjhcsoft.rule.datadispose.util.FactRowMapper;
import com.zjhcsoft.rule.datadispose.util.JDBCFetchUtil;
import com.zjhcsoft.rule.engine.component.RuleEngineEvent;
import com.zjhcsoft.rule.engine.util.EngineUtil;
import com.zjhcsoft.rule.engine.util.ResultMessage;
import com.zjhcsoft.rule.log.util.LogUtil;
import com.zjhcsoft.rule.result.event.RuleResultDetailEventListener;
import com.zjhcsoft.rule.result.event.RuleResultSummaryEventListener;
import com.zjhcsoft.rule.result.service.RuleKpiResultDetailService;
import com.zjhcsoft.rule.result.service.RuleKpiResultSummaryService;
import org.kie.api.definition.type.FactType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-6  Time: 下午5:09
 * 基础指标运算处理
 */
@Component
public class KpiHandler {

    private static Logger logger = LoggerFactory.getLogger(KpiHandler.class);

    @Inject
    private RuleKpiResultDetailService detailService;

    public void executeBaseKpi(RuleBaseServiceImpl ruleService, List<RuleKpiDefine> baseKpiDefineList) {

        for (RuleKpiDefine kpi : baseKpiDefineList) {
            executeBaseKpi(ruleService, kpi);
        }
    }

    @Async("groupTaskExecutor")
    public void executeBaseKpi(RuleBaseServiceImpl ruleService, RuleKpiDefine kpi) {
        RuleGroupTask task = ruleService.getRuleGroupTask();

        JDBCFetchUtil fetchUtil = new JDBCFetchUtil();
        //清空该KPI改月清单数据
        detailService.delete(kpi.getKpiCode(), task.getDateCd(), null);

        RuleTableDefine tableDefine = kpi.getRuleTableDefine();
        List<RuleTableDefine> paramTableList = ruleService.getParamTableList(tableDefine);

        //数据处理器
        List<RuleEngineEvent> engineEvents = new ArrayList<>();
        engineEvents.add(new RuleResultDetailEventListener(kpi, tableDefine, ruleService.getFactType(kpi, tableDefine), task));

        Result result = extractData(ruleService, task, fetchUtil, kpi, tableDefine, paramTableList, engineEvents);
        fetchUtil.destroy();
        if (null != result) {
            //异步方法
            Future<List<Long>> mixResult = ruleKpiRunStatusHandler.executeBase(result.futureList, task, kpi, result.logRowId);
            waitKpiDone(mixResult, ruleService);
        }
    }


    @Async("groupTaskExecutor")
    private void waitKpiDone(Future<List<Long>> mixResult, RuleBaseServiceImpl service) {
        while (!mixResult.isDone()) {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            List<Long> kpiId = mixResult.get();
            if (null != kpiId) {
                service.runMixKpi(kpiId);
            }
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Inject
    private RuleKpiResultSummaryService summaryService;

    @Async("groupTaskExecutor")
    public void executeMixKpi(RuleKpiDefine kpi, RuleBaseServiceImpl service) {
        RuleTableDefine tableDefine = kpi.getRuleTableDefine();
        List<RuleTableDefine> paramTableList = service.getParamTableList(tableDefine);
        RuleGroupTask task = service.getRuleGroupTask();

        //清空该KPI改月汇总数据
        summaryService.delete(kpi.getKpiCode(), task.getDateCd(), null);

        JDBCFetchUtil fetchUtil = new JDBCFetchUtil();

        //数据处理器
        List<RuleEngineEvent> engineEvents = new ArrayList<>();
        engineEvents.add(new RuleResultSummaryEventListener(kpi, tableDefine, service.getFactType(kpi, tableDefine), task));

        Result result = extractData(service, task, fetchUtil, kpi, tableDefine, paramTableList, engineEvents);
        fetchUtil.destroy();
        if (null != result) {
            //异步方法
            Future<List<Long>> mixResult = ruleKpiRunStatusHandler.executeMix(result.futureList, task, kpi, result.logRowId);
            waitKpiDone(mixResult, service);
        }
    }

    private Result extractData(RuleBaseServiceImpl ruleService, RuleGroupTask task, JDBCFetchUtil fetchUtil, RuleKpiDefine kpi, RuleTableDefine tableDefine, List<RuleTableDefine> paramTableList, List<RuleEngineEvent> engineEvents) {
        //todo  参数是否Check?
        int index = 1;
        int total = 0;
        Long logRowId = LogUtil.createLog(kpi, task);
        String ruleScript = kpi.getScriptRule();

        for (RuleEngineEvent event : engineEvents) {
            event.setLogId(logRowId);
        }

        //获取参数列表
        List paramList = ruleService.fetchParam(paramTableList, kpi, ruleService.getParaFetchParam());
        logger.debug("参数条数：{}", paramList.size());

        FactType factType = ruleService.getFactType(kpi, tableDefine);

        RowMapper rowMapper = FactRowMapper.create(factType);

        fetchUtil.setConnection(JDBCTemplateStore.getJDBCTemplate(tableDefine.getDsCode()));
        try {
            fetchUtil.createCursor(ruleService.getFetchSql(tableDefine, factType,kpi), ruleService.getDataFetchParam());
        } catch (Exception e) {
            //todo 处理异常
            LogUtil.changeStatus(logRowId, RuleConstants.Status.EXCEPTION, "数据模型数据获取游标" + e.toString(), true);
            return null;
        }

        List<Future<ResultMessage>> futureList = new ArrayList<>();
        try {
            while (true) {
                //使用JDBC 游标ResultSet
                List dataList = fetchUtil.next(10000, rowMapper);
                if (null == dataList || dataList.isEmpty()) {
                    break;
                }
                Future<ResultMessage> future = EngineUtil.exec(ruleScript, engineEvents, dataList, paramList, index, null);
                total += dataList.size();
                logger.debug("活动{} KPI {} 账期 {} 数据条数：{}/{} 已提交处理{}", task.getTaskName(), kpi.getKpiCode(), task.getDateCd(), dataList.size(), index++, total);
                futureList.add(future);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.changeStatus(logRowId, RuleConstants.Status.EXCEPTION, "KPI执行异常:" + e.toString(), true);
        }
        logger.debug("活动{} KPI {} 账期 {} 共提交处理了{}条数据", task.getTaskName(), kpi.getKpiCode(), task.getDateCd(), total);
        return new Result(futureList, logRowId);
    }

    @Inject
    private RuleKpiRunStatusHandler ruleKpiRunStatusHandler;

    @Inject
    private RuleGroupTaskResultHandler ruleGroupTaskResultHandler;

    private class Result {
        List<Future<ResultMessage>> futureList;
        Long logRowId;

        private Result(List<Future<ResultMessage>> futureList, Long logRowId) {
            this.futureList = futureList;
            this.logRowId = logRowId;
        }
    }
}
