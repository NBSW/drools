package com.zjhcsoft.util;

import com.zjhcsoft.rule.result.entity.RuleKpiResultSummary;
import com.zjhcsoft.rule.result.service.RuleKpiResultSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Created by XuanLubin on 2014/4/3. 22:01
 *
 */
@Component
@Lazy(false)
public class Value {

    private static RuleKpiResultSummaryService summaryService;

    public static float SummaryValue(String kpiCode, String dateCd, String dimId) {
        RuleKpiResultSummary summary = summaryService.fetchSummary(kpiCode, dateCd, dimId);
        if (null != summary) {
            return summary.getSumValue();
        } else {
            return 0f;
        }
    }

    @Autowired
    private void setSummaryService(RuleKpiResultSummaryService summaryService) {
        Value.summaryService = summaryService;
    }
}
