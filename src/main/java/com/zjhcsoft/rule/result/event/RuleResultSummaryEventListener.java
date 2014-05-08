package com.zjhcsoft.rule.result.event;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.common.util.DimFieldProcessor;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.engine.component.RuleEngineEvent;
import com.zjhcsoft.rule.result.entity.RuleKpiResultDetail;
import com.zjhcsoft.rule.result.entity.RuleKpiResultSummary;
import com.zjhcsoft.rule.result.service.RuleKpiResultDetailService;
import com.zjhcsoft.rule.result.service.RuleKpiResultSummaryService;
import org.kie.api.definition.type.FactType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        RuleKpiResultSummary ruleKpiResult = new RuleKpiResultSummary();
        //todo 设置结果值
        ruleKpiResult.setSumValue((Float) mapResult.get(RuleConstants.RuleColumn.Value.RULE_VALUE));
        ruleKpiResult.setRemark(String.valueOf(mapResult.get(RuleConstants.RuleColumn.Value.RULE_EXPR)));
        ruleKpiResult.setDimId(DimFieldProcessor.exec(mapResult.get(dataTableDefine.getDimField())));
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
