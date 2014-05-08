package com.zjhcsoft.rule.config.handler;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.engine.util.ResultMessage;
import com.zjhcsoft.rule.log.util.LogUtil;
import com.zjhcsoft.rule.result.handler.ResultSummaryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by XuanLubin on 2014/4/2. 11:01
 * 单个规则运算状态处理
 */
@Component
public class RuleKpiRunStatusHandler {

    private static Logger logger = LoggerFactory.getLogger(RuleKpiRunStatusHandler.class);


    private Result execute(List<Future<ResultMessage>> futureList, RuleGroupTask task, RuleKpiDefine kpi, Long logRowId) {
        boolean success = true;
        int total = 0;
        while (futureList.size() > 0) {
            for (Iterator<Future<ResultMessage>> ite = futureList.iterator(); ite.hasNext(); ) {
                Future<ResultMessage> future = ite.next();
                if (future.isDone()) {
                    try {
                        ResultMessage result = future.get();
                        success = success && result.isStatus();
                        total += result.getCount();
                        LogUtil.changeStatus(logRowId, RuleConstants.Status.RUNNING, "数据批次:" + result.getIndex() + "  数据量:" + result.getCount() + " 处理" + (success ? "完成" : "异常"), false);
                        logger.debug("活动{} KPI {} 账期 {} 数据批次 {} 数据量 {}  完成 完成状态： {}", task.getTaskName(), kpi.getKpiCode(), task.getDateCd(), result.getIndex(), result.getCount(), result.isStatus());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ite.remove();
                }
            }
            if (futureList.size() > 0) {
                try {
                    //等待5S继续
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.debug("活动{}  KPI {} 账期 {}  剩余任务 {}", task.getTaskName(), kpi.getClassCode(), task.getDateCd(), futureList.size());
        }
        LogUtil.changeStatus(logRowId, success ? RuleConstants.Status.RUNNINGENDING : RuleConstants.Status.EXCEPTION, "共处理数据量 " + total + " " + (success ? "成功完成" : "完成但有发生异常"), true);
        return new Result(success, total);
    }


    @Inject
    private RuleMixKpiHandler ruleMixKpiHandler;

    /**
     * 基础指标 异步结果处理
     *
     * @param futureList
     * @param task
     * @param kpi
     * @param logRowId
     *
     *  @return 依赖规则
     */
    @Async("kpiResultExecutor")
    public Future<List<Long>> executeBase(List<Future<ResultMessage>> futureList, RuleGroupTask task, RuleKpiDefine kpi, Long logRowId) {
        execute(futureList, task, kpi, logRowId);
        //todo  checkBefore summary 汇总前做什么确认
        //异步方法
        return resultSummaryHandler.execute(task, kpi);
    }

    /**
     * 组合指标 异步结果处理
     *
     * @param futureList
     * @param task
     * @param kpi
     * @param logRowId
     *
     * @return 依赖规则
     */
    @Async("kpiResultExecutor")
    public Future<List<Long>> executeMix(List<Future<ResultMessage>> futureList, RuleGroupTask task, RuleKpiDefine kpi, Long logRowId) {
        execute(futureList, task, kpi, logRowId);
        //查询依赖规则
        return ruleMixKpiHandler.fetchMixKpi(task, kpi);
    }


    @Inject
    private ResultSummaryHandler resultSummaryHandler;

    protected class Result {
        boolean success;
        int total;

        public Result(boolean success, int total) {
            this.success = success;
            this.total = total;
        }

    }

}
