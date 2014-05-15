package com.zjhcsoft.rule.result.event;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.common.util.DimFieldProcessor;
import com.zjhcsoft.rule.common.util.NaNNumberUtil;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.result.entity.RuleKpiResultSummary;
import com.zjhcsoft.rule.result.service.RuleKpiResultSummaryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.kie.api.definition.type.FactType;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-6  Time: 下午3:50
 */
@Component
@Lazy(false)
public class RuleResultSummaryEventListener extends RuleResultBaseEventListener<RuleKpiResultSummary, RuleKpiResultSummaryService> {

    public RuleResultSummaryEventListener() {
    }

    public RuleResultSummaryEventListener(RuleKpiDefine ruleKpiDefine, RuleTableDefine dataTableDefine, FactType dataFactType, RuleGroupTask ruleGroupTask) {
        super(ruleKpiDefine, dataTableDefine, dataFactType, ruleGroupTask);
    }

    @Override
    protected RuleKpiResultSummary extractItem(Map<String, Object> mapResult, RuleTableDefine dataTableDefine, RuleGroupTask ruleGroupTask, RuleKpiDefine ruleKpiDefine) {
        String dimValue = DimFieldProcessor.exec(mapResult.get(dataTableDefine.getDimField()));
        if (StringUtils.isBlank(dimValue)) {
            return null;
        }

        RuleKpiResultSummary ruleKpiResult = new RuleKpiResultSummary();
        //todo 设置结果值
        float sumValue = (float) mapResult.get(RuleConstants.RuleColumn.Value.RULE_VALUE);
        if (!NaNNumberUtil.isNaN(sumValue)) {
            ruleKpiResult.setSumValue(sumValue);
        } else {
            ruleKpiResult.setSumValue(0);
        }
        ruleKpiResult.setRemark(String.valueOf(mapResult.get(RuleConstants.RuleColumn.Value.RULE_EXPR)));
        ruleKpiResult.setDimId(dimValue);
        ruleKpiResult.setDateCd(ruleGroupTask.getDateCd());
        ruleKpiResult.setLatnId(dataTableDefine.getLatnId());
        ruleKpiResult.setKpiCode(ruleKpiDefine.getKpiCode());
        ruleKpiResult.setRuleGroupTaskRowId(ruleGroupTask.getRuleGroupTaskRowId());
        ruleKpiResult.setCommId(ruleGroupTask.getCommId());
        ruleKpiResult.setClassCode(ruleKpiDefine.getClassCode());
        ruleKpiResult.setKpiName(ruleKpiDefine.getKpiName());
        return ruleKpiResult;
    }

    private static RuleKpiResultSummaryService service;

    @Override
    protected RuleKpiResultSummaryService getService() {
        return service;
    }

    @Inject
    public void setService(RuleKpiResultSummaryService service) {
        RuleResultSummaryEventListener.service = service;
    }
}
