package com.zjhcsoft.rule.config.service;

import com.zjhcsoft.rule.common.service.BaseService;
import com.zjhcsoft.rule.config.entity.RuleGroup;
import com.zjhcsoft.rule.config.repository.RuleGroupRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-5  Time: 上午10:05
 */
public interface RuleGroupService extends BaseService<RuleGroup, RuleGroupRepository> {
    public List<RuleGroup> findAll(String latnId);

    public List<RuleGroup> findAllByStatus(int status,String latnId);
}
