package com.zjhcsoft.rule.job;

import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import org.apache.commons.lang3.StringUtils;
import org.gearman.Gearman;
import org.gearman.GearmanClient;
import org.gearman.GearmanServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by XuanLubin on 2014/4/14. 9:29
 */
@Component
public class RuleClientImpl implements RuleClient {
    private GearmanClient client = null;

    private static Logger logger = LoggerFactory.getLogger(RuleClientImpl.class);

    public static String RULE_GROUP_CALCULATE_TASK = "RULE_GROUP_CALCULATE_TASK";

    public RuleClientImpl() {
    }

    public RuleClientImpl(String host, int port) throws IOException {
        init(host, port);
    }

    private void init(String host, int port) throws IOException {
        Assert.hasText(host);
        Assert.isTrue(port >= 0 && port < 65535);
        Gearman gearman = GearmanSystem.get();
        client = gearman.createGearmanClient();
        final GearmanServer server = gearman.createGearmanServer(host, port);
        client.addServer(server);
    }

    public void sendNewTask(RuleGroupTask task) {
        Assert.notNull(task);
        logger.debug("发布任务 任务:{}", task);
        client.submitBackgroundJob(RULE_GROUP_CALCULATE_TASK, task.getRuleGroupTaskRowId().toString().getBytes());
    }

    public void sendNewTask(Long id) {
        Assert.notNull(id);
        logger.debug("发布任务 任务ID:{}", id);
        client.submitBackgroundJob(RULE_GROUP_CALCULATE_TASK, id.toString().getBytes());
    }

    @PostConstruct
    private void _init() throws IOException {
        init(GearmanSystem.serverIp, GearmanSystem.serverPort);
    }
}
