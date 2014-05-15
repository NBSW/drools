package com.zjhcsoft.rule.job;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.service.RuleGroupTaskService;
import com.zjhcsoft.rule.datadispose.service.RuleService;
import com.zjhcsoft.rule.datadispose.util.RuleServiceFactory;
import org.gearman.GearmanFunction;
import org.gearman.GearmanFunctionCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Calendar;

/**
 * Created by XuanLubin on 2014/4/14. 11:12
 */
@Component
@Lazy(false)
public class RuleWorkerExecutor implements GearmanFunction {

    private static Logger logger = LoggerFactory.getLogger(RuleWorkerExecutor.class);

    public RuleWorkerExecutor() {
    }

    public static RuleGroupTaskService service;

    @Override
    public byte[] work(String s, byte[] bytes, GearmanFunctionCallback gearmanFunctionCallback) throws Exception {
        Long taskId = Long.parseLong(new String(bytes));
        logger.debug("任务处理 taskId:{} @  {}", taskId, Calendar.getInstance().getTime());
        executeBis(taskId);
        return new byte[0];
    }

    //业务逻辑
    @Async
    private void executeBis(Long taskId) {
        RuleGroupTask newTask = service.get(taskId);
        if (null != newTask) {
            //创建新活动
            RuleService ruleService = RuleServiceFactory.createBaseKpiService(newTask);
            if (null != ruleService) {
                ruleService.start();
            } else {
                newTask.setStatus(RuleConstants.Status.EXCEPTION);
                service.update(newTask);
            }
        }
    }

    @Inject
    public void setService(RuleGroupTaskService service) {
        RuleWorkerExecutor.service = service;
    }
}
