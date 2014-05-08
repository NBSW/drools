package com.zjhcsoft.rule.config.service;

import com.zjhcsoft.rule.common.service.BaseService;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.repository.RuleGroupTaskRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-3  Time: 下午7:07
 */
public interface RuleGroupTaskService extends BaseService<RuleGroupTask,RuleGroupTaskRepository> {
    public List<RuleGroupTask> queryByStatus(Integer status,String latnId);

    public List<RuleGroupTask> queryByRuleGroupRowId(Long ruleGroupRowId);

    public RuleGroupTask queryLastTaskByGroupId(Long ruleGroupRowId);

    public List<RuleGroupTask> findAll(String latnId);

    public boolean isExist(Long groupRowId,String dateCd);

    public void deleteByRuleGroupRowId(Long groupRowId);
}
