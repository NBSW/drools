package com.zjhcsoft.rule.result.dao;

import com.zjhcsoft.rule.result.entity.RuleKpiResultSummary;

import java.util.List;

/**
 * Created by XuanLubin on 2014/4/2. 14:34
 */
public interface RuleKpiResultSummaryDao {

    public List<RuleKpiResultSummary> summary(String kpiCode, String dateCd);

    public int summaryDbSide(String kpiCode, String dateCd);

    public int saveSummary(RuleKpiResultSummary summary);

    public RuleKpiResultSummary fetchSummary(String kpiCode, String dateCd, String dimId);

    public int[] save(List<RuleKpiResultSummary> summaryList);

    public void delete(String kpiCode, String dateCd, String dimId);

}
