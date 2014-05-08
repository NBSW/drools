package com.zjhcsoft.rule.job;

import com.ecfront.easybi.base.utils.PropertyHelper;
import org.gearman.Gearman;
import org.gearman.GearmanServer;
import org.gearman.GearmanWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by XuanLubin on 2014/4/14. 9:29
 */
@Component
@Lazy(false)
public class RuleWorker {

    private static Logger logger = LoggerFactory.getLogger(RuleWorker.class);

    private static final int WORKER_COUNT = Integer.parseInt(PropertyHelper.get("WORKER_COUNT","10"));

    public RuleWorker() {
        try {
            init(GearmanSystem.serverIp, GearmanSystem.serverPort);
        } catch (IOException e) {
            logger.error("任务处理器初始化失败");
            e.printStackTrace();
        }
    }

    public RuleWorker(String host, int port) throws IOException {
        init(host, port);
    }

    private void init(String host, int port) throws IOException {
        Gearman gearman = GearmanSystem.get();
        final GearmanServer server = gearman.createGearmanServer(host, port);
        for (int i = 0; i < WORKER_COUNT; i++) {
            GearmanWorker worker = gearman.createGearmanWorker();
            if (worker.addServer(server)) {
                worker.addFunction(RuleClientImpl.RULE_GROUP_CALCULATE_TASK, new RuleWorkerExecutor());
            }
        }
        logger.debug("任务处理监听器启动完成");
    }

    public static void main(String[] args) {
        try {
            RuleClient client = new RuleClientImpl(GearmanSystem.serverIp, GearmanSystem.serverPort);
            new RuleWorker();
            for (int i = 0; i++ < 10000; ) {
                client.sendNewTask(new Long(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
