package com.zjhcsoft.rule.result.dao;

import com.zjhcsoft.rule.result.entity.RuleKpiResultDetail;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-11  Time: 上午9:21
 */
public interface RuleKpiResultDetailDao {
    public int save(RuleKpiResultDetail t);
    public int [] save(List<RuleKpiResultDetail> list);
    public List<RuleKpiResultDetail> fetch(String kpiCode, String dateCd, String dimId);
    public void delete(String kpiCode,String dateCd,String dimId);
}
