package com.zjhcsoft.util;

import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.service.RuleKpiDefineService;
import com.zjhcsoft.rule.config.util.RuleKpiCache;
import com.zjhcsoft.rule.result.entity.RuleKpiResultDetail;
import com.zjhcsoft.rule.result.entity.RuleKpiResultSummary;
import com.zjhcsoft.rule.result.service.RuleKpiResultDetailService;
import com.zjhcsoft.rule.result.service.RuleKpiResultSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by XuanLubin on 2014/4/3. 22:01
 */
@Component
@Lazy(false)
public class Value {

    private static RuleKpiResultSummaryService summaryService;

    private static RuleKpiResultDetailService detailService;

    private static RuleKpiDefineService kpiDefineService;

    public static float SummaryValue(String kpiCode, String dateCd, String dimId) {

        RuleKpiDefine define = RuleKpiCache.get(kpiCode);

        if (null == define) {
            define = kpiDefineService.findByKpiCode(kpiCode);
        }

        if (null == define) {
            return 0f;
        } else if (define.isSummary()) {
            RuleKpiResultSummary summary = summaryService.fetchSummary(kpiCode, dateCd, dimId);
            if (null != summary) {
                return summary.getSumValue();
            }
        } else {
            RuleKpiResultDetail detail = detailService.fetchDetail(kpiCode, dateCd, dimId);
            if (null != detail) {
                return detail.getKpiValue();
            }
        }
        return 0f;
    }

    @Inject
    public void setKpiDefineService(RuleKpiDefineService kpiDefineService) {
        Value.kpiDefineService = kpiDefineService;
    }

    @Inject
    public void setDetailService(RuleKpiResultDetailService detailService) {
        Value.detailService = detailService;
    }

    @Inject
    private void setSummaryService(RuleKpiResultSummaryService summaryService) {
        Value.summaryService = summaryService;
    }
}
