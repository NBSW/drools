package com.zjhcsoft.rule.job;

import com.zjhcsoft.rule.config.entity.RuleGroupTask;

/**
 * Created by XuanLubin on 2014/5/5. 9:26
 */
public interface RuleClient {

    public void sendNewTask(RuleGroupTask task);

    public void sendNewTask(Long id);

}
