package com.zjhcsoft.rule.common.util;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 13-10-24  Time: 上午11:11
 * Important Notice : suit for quartz:2.2.1
 */
public abstract class BaseSchedule {

    private static Logger logger = LoggerFactory.getLogger(BaseSchedule.class);

    protected static String DEFAULT_CRON_DAY = "0 0 10 * * ?";//默认每天十点

    protected static SchedulerFactory scheduleFactory = new StdSchedulerFactory();

    protected static Scheduler scheduler;


    public abstract void roll();

    public static void deleteJob(Class<? extends Job> clazz, Object id) {
        JobKey jobKey = getJobKey(clazz, id);
        if (null == findJob(clazz, id)) {
            return;
        }
        try {
            scheduler.deleteJob(jobKey);
            logger.info("成功删除调度任务 {} ", jobKey);
        } catch (SchedulerException e) {
            logger.error("删除调度任务 {} 出现异常 {}", jobKey, e.getMessage());
        }
    }

    public static void create(Map<String, Object> model, String cronExpression, Class<? extends Job> clazz, Object id) {
        if (!reSchedule(clazz, id, cronExpression)) {
            return;
        }
        JobKey jobKey = getJobKey(clazz, id);
        try {
            JobDetailImpl jobDetail = new JobDetailImpl();
            jobDetail.setKey(jobKey);
            jobDetail.setJobClass(clazz);
            for (String key : model.keySet()) {
                jobDetail.getJobDataMap().put(key, model.get(key));
            }
            CronTriggerImpl trigger = new CronTriggerImpl();
            trigger.setCronExpression(cronExpression);
            trigger.setKey(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
            Date ft = scheduler.scheduleJob(jobDetail, trigger);
            logger.info("调度任务成功启动 {} class:{} 首次启动时间:{}", jobKey, clazz,ft);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建调度任务失败 {} ex:{}", jobKey, e.getMessage());
        }
    }

    public static JobDetail findJob(Class<? extends Job> clazz, Object id) {
        JobKey jobKey = getJobKey(clazz, id);
        try {
            JobDetail detail = scheduler.getJobDetail(jobKey);
            if (detail != null){
                logger.info("成功找到JOB {}", jobKey);
                return detail;
            }else
                logger.info("JOB不存在 {}", jobKey);
        } catch (SchedulerException e) {
            logger.error("未找到JOB {} ex:{}", jobKey, e.getMessage());
        }
        return null;
    }

    private static JobKey getJobKey(Class<? extends Job> clazz, Object id) {
        return JobKey.jobKey(clazz.getSimpleName() + id, clazz.getName());
    }

    private static boolean reSchedule(Class<? extends Job> clazz, Object id, String cronExpression) {
        JobDetail jobDetail = findJob(clazz, id);
        if (null != jobDetail) {
            try {
                CronTrigger trigger = (CronTrigger) scheduler.getTriggersOfJob(jobDetail.getKey()).get(0);
                if (!trigger.getCronExpression().equals(cronExpression)) {
                    deleteJob(clazz, id);
                } else {
                    return false;
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @PostConstruct
    public void init() {
        try {
            scheduler = scheduleFactory.getScheduler();
            scheduler.start();
            logger.info("调度器初始化并启动成功");
        } catch (SchedulerException e) {
            logger.error("调度器初始化出现异常  ex:{}", e.getMessage());
        }
    }
}
