package com.zjhcsoft.rule.result.event;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.common.util.DimFieldProcessor;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.engine.component.RuleEngineEvent;
import com.zjhcsoft.rule.result.entity.RuleKpiResultDetail;
import com.zjhcsoft.rule.result.service.RuleKpiResultDetailService;
import org.apache.commons.lang3.math.NumberUtils;
import org.kie.api.definition.type.FactType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigDecimal;
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
public class RuleResultDetailEventListener extends RuleResultBaseEventListener<RuleKpiResultDetail,RuleKpiResultDetailService>{

    public RuleResultDetailEventListener() {
    }

    public RuleResultDetailEventListener(RuleKpiDefine ruleKpiDefine, RuleTableDefine dataTableDefine, FactType dataFactType, RuleGroupTask ruleGroupTask) {
        super(ruleKpiDefine, dataTableDefine, dataFactType, ruleGroupTask);
    }

    @Override
    protected RuleKpiResultDetail extractItem(Map<String, Object> mapResult,RuleTableDefine dataTableDefine,RuleGroupTask ruleGroupTask,RuleKpiDefine ruleKpiDefine) {
        RuleKpiResultDetail ruleKpiResult = new RuleKpiResultDetail();
        //todo 设置结果值
        ruleKpiResult.setExpr((String) mapResult.get(RuleConstants.RuleColumn.Value.RULE_EXPR));
        ruleKpiResult.setKpiValue(new BigDecimal((float) mapResult.get(RuleConstants.RuleColumn.Value.RULE_VALUE)).doubleValue());
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

    private static RuleKpiResultDetailService service;

    @Override
    protected RuleKpiResultDetailService getService() {
        return service;
    }

    @Inject
    public void setService(RuleKpiResultDetailService service) {
        RuleResultDetailEventListener.service = service;
    }
}
