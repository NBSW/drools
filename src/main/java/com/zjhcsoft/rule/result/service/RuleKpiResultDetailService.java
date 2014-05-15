package com.zjhcsoft.rule.result.service;

import com.zjhcsoft.rule.result.entity.RuleKpiResultDetail;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-4  Time: 下午3:10
 */
public interface RuleKpiResultDetailService extends RuleKpiResultBaseService<RuleKpiResultDetail> {
    public RuleKpiResultDetail fetchDetail(String kpiCode, String dateCd, String dimId);
}
