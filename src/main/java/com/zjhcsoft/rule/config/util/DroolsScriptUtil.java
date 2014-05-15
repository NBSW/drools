package com.zjhcsoft.rule.config.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.config.service.RuleKpiDefineService;
import com.zjhcsoft.rule.config.service.RuleTableDefineService;
import com.zjhcsoft.rule.engine.parser.Parser;
import com.zjhcsoft.rule.engine.util.EngineUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XuanLubin on 2014/5/14. 20:08
 */
@Component
public class DroolsScriptUtil {
    @Inject
    private RuleTableDefineService tblService;

    @Inject
    private Parser drlParser;

    @Inject
    private RuleKpiDefineService kpiDefineService;


    /**
     * 生规则成脚本
     *
     * @param ruleKpiDefine
     * @param updateKpi
     * @return
     */
    public Set<String> generateRuleScript(RuleKpiDefine ruleKpiDefine, boolean updateKpi) {
        JSONObject $param = new JSONObject();
        JSONObject object = JSON.parseObject(ruleKpiDefine.getScriptData());
        JSONArray tables = object.getJSONArray(RuleConstants.RuleJsonKey.TABLE_MODEL);

        JSONObject prepared = prepareDeclare(tables);

        //数据模型ID
        String dataModelId = String.valueOf(prepared.remove(RuleConstants.RuleJsonKey.DATA_MODEL_ID));

        $param.putAll(prepared);

        $param.put(RuleConstants.RuleJsonKey.PACKAGE_NAME, EngineUtil.getPackageName(ruleKpiDefine.getRuleKpiDefineRowId()));
        JSONArray imports = new JSONArray();
        boolean isMixKpi = ruleKpiDefine.getType() == RuleConstants.Type.MIX_KPI;
        if (isMixKpi) {
            imports.add("function com.zjhcsoft.util.Value.SummaryValue");
        }
        $param.put(RuleConstants.RuleJsonKey.IMPORTS, imports);
        JSONArray globals = new JSONArray();
        Set<String> baseKpi = null;
        if (isMixKpi) {
            baseKpi = new HashSet<>();
            //组合指标添加DateCD
            JSONObject global = new JSONObject();
            global.put(RuleConstants.RuleJsonKey.GLOBAL_TYPE, RuleConstants.RuleJsonKey.DateCd.TYPE);
            global.put(RuleConstants.RuleJsonKey.GLOBAL_INSTANCE, RuleConstants.RuleJsonKey.DateCd.INSTANCE);
            globals.add(global);
        }
        $param.put(RuleConstants.RuleJsonKey.GLOBALS, globals);

        JSONArray ruleArr = object.getJSONArray(RuleConstants.RuleJsonKey.RULE_ARR);
        ruleKpiDefine.setScriptData(ruleArr.toJSONString());
        ruleArr = dealRuleArr(ruleArr, prepared.getJSONObject(RuleConstants.RuleJsonKey.COL_JOIN_PARAM), dataModelId, isMixKpi, baseKpi);
        $param.put(RuleConstants.RuleJsonKey.RULES_GROUP, ruleArr);
        String olb_script = ruleKpiDefine.getScriptRule();
        ruleKpiDefine.setScriptRule(drlParser.parse($param));

        if (!StringUtils.equals(olb_script, ruleKpiDefine.getScriptRule())) {
            EngineUtil.updateKnowledge(ruleKpiDefine.getScriptRule(), olb_script);
            if (updateKpi) {
                kpiDefineService.update(ruleKpiDefine);
            }
        }
        return baseKpi;
    }

    //获取表字段配置
    private JSONObject prepareDeclare(JSONArray tableDefineIds) {
        Long[] a = new Long[tableDefineIds.size()];
        for (int i = 0; i < tableDefineIds.size(); i++) {
            a[i] = tableDefineIds.getLong(i);
        }
        List<RuleTableDefine> tableDefineList = tblService.findByPkIds(a);
        JSONObject $pp = new JSONObject();
        JSONArray tables = new JSONArray();
        JSONObject joinColumn = new JSONObject();
        for (RuleTableDefine define : tableDefineList) {
            JSONObject t = JSON.parseObject(define.getFields());
            t.put(RuleConstants.RuleJsonKey.FACT_NAME, EngineUtil.getTypeName(define.getTableDefineRowId()));
            if (RuleConstants.Type.DATA_MODEL == define.getType()) {
                $pp.put(RuleConstants.RuleJsonKey.DATA_MODEL_ID, define.getTableDefineRowId());
                JSONObject join = JSON.parseObject(define.getRelField());
                if (null != join) {
                    for (String k : join.keySet()) {
                        JSONObject $$jo = join.getJSONObject(k);
                        String fact = $$jo.getString(RuleConstants.RuleJsonKey.ID_KEY);
                        if (!joinColumn.containsKey(fact)) {
                            joinColumn.put(fact, new JSONArray());
                        }
                        JSONObject $$jp = new JSONObject();
                        $$jp.put(RuleConstants.RuleJsonKey.ID_KEY, define.getTableDefineRowId());
                        $$jp.put(RuleConstants.RuleJsonKey.RULE_FIELD, $$jo.getString(RuleConstants.RuleJsonKey.COL_T));
                        $$jp.put(RuleConstants.RuleJsonKey.RULE_OP, "==");
                        $$jp.put(RuleConstants.RuleJsonKey.RULE_VALUE, k);
                        joinColumn.getJSONArray(fact).add($$jp);
                    }
                }
            }
            tables.add(t);
        }
        $pp.put(RuleConstants.RuleJsonKey.DECLARE, tables);
        $pp.put(RuleConstants.RuleJsonKey.COL_JOIN_PARAM, joinColumn);
        return $pp;
    }

    //处理规则When部分
    //添加默认条件或默认关联
    private JSONArray dealRuleArr(JSONArray ruleArr, JSONObject joinColumn, String dataModelId, boolean isMixKpi, Set<String> baseKpi) {
        Set<String> tableUsed = new HashSet<>();
        for (int i = 0; i < ruleArr.size(); i++) {
            JSONObject ruleItem = ruleArr.getJSONObject(i);
            JSONArray whens = ruleItem.getJSONArray(RuleConstants.RuleJsonKey.RULE_WHEN);
            ruleItem.put(RuleConstants.RuleJsonKey.RULE_THEN, dealRuleThenPart(ruleItem, dataModelId, isMixKpi, baseKpi));
            //处理前台配置的条件选项
            for (int j = 0; j < whens.size(); j++) {
                String id = whens.getJSONObject(j).getString(RuleConstants.RuleJsonKey.ID_KEY);
                if (joinColumn.containsKey(id)) {
                    JSONArray $$rules = whens.getJSONObject(j).getJSONArray(RuleConstants.RuleJsonKey.RULE_OPS);
                    $$rules.addAll(joinColumn.getJSONArray(id));
                }
                tableUsed.add(id);
            }
            //处理关联信息
            for (String k : joinColumn.keySet()) {
                if (!tableUsed.contains(k)) {
                    JSONObject $$p = new JSONObject();
                    $$p.put(RuleConstants.RuleJsonKey.ID_KEY, k);
                    $$p.put(RuleConstants.RuleJsonKey.RULE_OPS, joinColumn.get(k));
                    whens.add($$p);
                    tableUsed.add(k);
                    JSONArray joinTable = joinColumn.getJSONArray(k);
                    for (int t = 0; t < joinTable.size(); t++) {
                        JSONObject _ta = joinTable.getJSONObject(t);
                        String _key = _ta.getString(RuleConstants.RuleJsonKey.ID_KEY);
                        if (!tableUsed.contains(_key)) {
                            $$p = new JSONObject();
                            $$p.put(RuleConstants.RuleJsonKey.ID_KEY, _key);
                            $$p.put(RuleConstants.RuleJsonKey.RULE_OPS, new JSONArray());
                            whens.add($$p);
                            tableUsed.add(_key);
                        }
                    }
                }
            }
            //确认数据模型是否使用
            if (!tableUsed.contains(dataModelId)) {
                JSONObject $$p = new JSONObject();
                $$p.put(RuleConstants.RuleJsonKey.ID_KEY, dataModelId);
                $$p.put(RuleConstants.RuleJsonKey.RULE_OPS, new JSONArray());
                whens.add($$p);
            }
            tableUsed.clear();
        }
        return ruleArr;
    }

    private JSONArray dealRuleThenPart(JSONObject ruleItem, String dataModelId, boolean isMixKpi, Set<String> baseKpi) {

        JSONArray jsonArray = new JSONArray();
        String dataFact = EngineUtil.getFactArgumentName(dataModelId);

        String ruleThen = ruleItem.getString(RuleConstants.RuleJsonKey.RULE_THEN);

        if (StringUtils.isBlank(ruleItem.getString(RuleConstants.RuleJsonKey.RULE_NAME))) {
            ruleItem.put(RuleConstants.RuleJsonKey.RULE_NAME, "Rule" + UUID.randomUUID().toString().replaceAll("-", ""));
        }

        if (isMixKpi) {
            RuleTableDefine tblDefine = tblService.get(Long.parseLong(dataModelId));
            Pattern pattern = Pattern.compile("\\[(.+?)\\]");
            Matcher matcher = pattern.matcher(ruleThen);
            while (matcher.find()) {
                baseKpi.add(matcher.group(1));
            }
            ruleThen = ruleThen.replaceAll("\\[(.+?)\\]", "SummaryValue(\"$1\"," + RuleConstants.RuleJsonKey.DateCd.INSTANCE + ",\\" + dataFact + "." + tblDefine.getDimField() + ")");
        }

        //数据计算部分
        jsonArray.add(dataFact + "." + RuleConstants.RuleColumn.Value.RULE_VALUE + "=" + ruleThen);
        //文字表达式部分
        StringBuilder r_expr = new StringBuilder();
        r_expr.append(dataFact).append(".").append(RuleConstants.RuleColumn.Value.RULE_EXPR).append("=");
        r_expr.append("\"").append(ruleItem.getString(RuleConstants.RuleJsonKey.RULE_THEN_STR)).append("\"");
        jsonArray.add(r_expr.toString());
        //从WorkMemory 移除该Fact
        jsonArray.add("retract(" + dataFact + ")");
        return jsonArray;
    }
}
