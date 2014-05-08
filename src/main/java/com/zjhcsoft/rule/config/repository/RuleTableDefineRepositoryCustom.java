package com.zjhcsoft.rule.config.repository;

import com.zjhcsoft.rule.config.dto.RuleTableDefineQuery;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 2014/3/24  Time: 14:41
 */
public interface RuleTableDefineRepositoryCustom {
    public List<RuleTableDefine> findBy(RuleTableDefineQuery query);
}
