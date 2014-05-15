package com.zjhcsoft.rule.schedule;

import com.zjhcsoft.qin.exchange.utils.PropertyHelper;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.service.RuleGroupTaskService;
import com.zjhcsoft.rule.job.RuleClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by XuanLubin on 2014/5/13. 9:39
 */
@Component
@Lazy(false)
public class TaskPublisher {

    private static Logger logger = LoggerFactory.getLogger(TaskPublisher.class);

    @Inject
    private RuleClient ruleClient;

    @Inject
    private RuleGroupTaskService service;

    //任务生产主机？
    private boolean isMaster = Boolean.parseBoolean(PropertyHelper.get("ruleGroup.master"));

    @Scheduled(cron = "0/10 * * * * ?")
    public void scanAvailableTask(){

        //非任务发布主机不发布任务
        if (!isMaster) {
            return;
        }

        List<RuleGroupTask> taskList = service.queryByStatus(RuleConstants.Status.READY,null);

        logger.debug("可以发布的主题记录数:{}",taskList.size());

        for(RuleGroupTask task:taskList) {
            //发布任务
            ruleClient.sendNewTask(task);
            logger.debug("发布主题:{}",task);
        }
    }
}
