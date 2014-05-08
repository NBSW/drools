package com.zjhcsoft.rule.job;

import com.zjhcsoft.qin.exchange.utils.PropertyHelper;
import org.gearman.Gearman;

import java.io.IOException;

/**
 * Created by XuanLubin on 2014/4/14. 9:57
 */
public class GearmanSystem {
    private static Gearman gearman;

    public static final String serverIp = PropertyHelper.get("gearman.server");
    public static final int serverPort = Integer.parseInt(PropertyHelper.get("gearman.port"));

    public static Gearman get() throws IOException {
        if (null == gearman) {
            gearman = Gearman.createGearman();
        }
        return gearman;
    }

    public static void close() {
        if (null != gearman && !gearman.isShutdown()) {
            gearman.shutdown();
        }
    }
}
