package com.zjhcsoft.rule.datadispose.service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-6  Time: 下午4:38
 */
public interface RuleBaseService<T> {
    public void runMixKpi(List<Long> mixKpiIds);
}
