package com.zjhcsoft.rule.config.service;

import com.zjhcsoft.rule.common.service.BaseService;
import com.zjhcsoft.rule.config.dto.RuleTableDefineQuery;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.config.repository.RuleTableDefineRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-3  Time: 下午4:33
 */
public interface RuleTableDefineService extends BaseService<RuleTableDefine, RuleTableDefineRepository> {
    public List<RuleTableDefine> findAll(String latnId);
    public List<RuleTableDefine> find(RuleTableDefineQuery query);

    public List<RuleTableDefine> getParamTableList(Long ruleTableRowId);
}
