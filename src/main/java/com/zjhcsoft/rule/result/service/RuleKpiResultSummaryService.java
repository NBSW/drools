package com.zjhcsoft.rule.result.service;

import com.zjhcsoft.rule.result.entity.RuleKpiResultSummary;

/**
 * Created by XuanLubin on 2014/4/2. 14:35
 */
public interface RuleKpiResultSummaryService extends RuleKpiResultBaseService<RuleKpiResultSummary> {

    public void summary(String kpiCode, String dateCd);

    //数据库端汇总
    public void summaryDbSide(String kpiCode, String dateCd);

    public RuleKpiResultSummary fetchSummary(String kpiCode, String dateCd, String dimId);


}
