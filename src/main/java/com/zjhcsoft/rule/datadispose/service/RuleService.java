package com.zjhcsoft.rule.datadispose.service;

import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.datadispose.util.JDBCFetchUtil;
import org.kie.api.definition.type.FactType;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-2-28  Time: 上午8:43
 */
public interface RuleService<T> extends RuleBaseService<T> {

    public List<Object> fetchParam(List paramTableList,RuleKpiDefine kpiDefine, Object[] args);

    public void start();

    public String getFetchSql(RuleTableDefine define, FactType factType,RuleKpiDefine kpiDefine);

    public Object[] getDataFetchParam();

    public Object[] getParaFetchParam();
}
