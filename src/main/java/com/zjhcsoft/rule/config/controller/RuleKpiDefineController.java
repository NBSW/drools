package com.zjhcsoft.rule.config.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjhcsoft.qin.exchange.controller.ControllerHelper;
import com.zjhcsoft.qin.exchange.dto.ResponseVO;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.common.RuleConstants.RuleJsonKey;
import com.zjhcsoft.rule.common.RuleConstants.Type;
import com.zjhcsoft.rule.common.controller.BaseController;
import com.zjhcsoft.rule.config.entity.RuleGroup;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleRelation;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.config.service.RuleGroupService;
import com.zjhcsoft.rule.config.service.RuleKpiDefineService;
import com.zjhcsoft.rule.config.service.RuleRelationService;
import com.zjhcsoft.rule.config.service.RuleTableDefineService;
import com.zjhcsoft.rule.config.vo.RuleKpiDefineVo;
import com.zjhcsoft.rule.engine.parser.Parser;
import com.zjhcsoft.rule.engine.util.EngineUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-12  Time: 下午2:45
 */
@RestController
@RequestMapping("config/kpi")
public class RuleKpiDefineController extends BaseController<RuleKpiDefineService, RuleKpiDefine> {

    @Inject
    private RuleRelationService ruleRelationService;

    @Inject
    private Parser drlParser;

    @Inject
    private RuleTableDefineService tblService;

    @Inject
    private RuleGroupService ruleGroupService;

    @Override
    public ResponseVO get(@PathVariable String pk) {
        ResponseVO<RuleKpiDefine> _super = super.get(pk);
        RuleKpiDefine define = _super.getBody();
        if (null != define) {
            String _type = define.getType() == Type.BASE_KPI ? Type.RULE_GROUP_BASE_RULE_KPI : Type.RULE_GROUP_MIX_RULE_KPI;
            List<Long> ruleRelationList = ruleRelationService.queryIdByTargetIdRelType(define.getRuleKpiDefineRowId(), new String[]{_type});
            List<RuleGroup> ruleGroupList = ruleGroupService.findByPkIds(ruleRelationList);
            return ControllerHelper.success(new RuleKpiDefineVo(define, ruleGroupList));
        } else {
            return _super;
        }
    }

    @RequestMapping("group/{groupId}")
    public ResponseVO save(@RequestBody RuleKpiDefine ruleKpiDefine, @PathVariable Long groupId) {
        if (null == ruleKpiDefine.getStatus()) {
            ruleKpiDefine.setStatus(RuleConstants.Status.NORMAL);
        }
        ResponseVO response = super.save(ruleKpiDefine);
        if (null != response.getBody() && null != groupId) {
            ruleKpiDefine = (RuleKpiDefine) response.getBody();
            Set<String> baseKpi = generateRuleScript(ruleKpiDefine);
            dealMixKpiRelation(ruleKpiDefine, baseKpi);
            ruleKpiDefine = service.update(ruleKpiDefine);
            String _type;
            if (Type.MIX_KPI == ruleKpiDefine.getType()) {
                _type = Type.RULE_GROUP_MIX_RULE_KPI;
            } else {
                _type = Type.RULE_GROUP_BASE_RULE_KPI;
            }
            ruleRelationService.create(groupId, ruleKpiDefine.getRuleKpiDefineRowId(), _type);
        }
        return response;
    }

    @Override
    public ResponseVO update(@PathVariable String pk, @RequestBody RuleKpiDefine ruleKpiDefine) {
        if (null == ruleKpiDefine.getStatus()) {
            ruleKpiDefine.setStatus(RuleConstants.Status.NORMAL);
        }
        RuleKpiDefine oldRuleKpiDefine = service.get(Long.valueOf(pk));
        EngineUtil.removeRuleBase(oldRuleKpiDefine.getScriptRule());
        Set<String> baseKpi = generateRuleScript(ruleKpiDefine);
        dealMixKpiRelation(ruleKpiDefine, baseKpi);
        return super.update(pk, ruleKpiDefine);

    }

    /**
     * 组合指标 与 基础指标关联关系处理
     *
     * @param ruleKpiDefine
     * @param baseKpi
     */
    private void dealMixKpiRelation(RuleKpiDefine ruleKpiDefine, Set<String> baseKpi) {
        ruleRelationService.deleteAllByFromId(ruleKpiDefine.getRuleKpiDefineRowId(), new String[]{Type.MIX_BASE_KPI_REL});
        if (null != baseKpi && baseKpi.size() > 0) {
            for (String kpiCode : baseKpi) {
                RuleKpiDefine kpiDefine = service.findByKpiCode(kpiCode);
                if (null != kpiDefine) {
                    ruleRelationService.create(ruleKpiDefine.getRuleKpiDefineRowId(), kpiDefine.getRuleKpiDefineRowId(), Type.MIX_BASE_KPI_REL);
                }
            }
        }
    }

    /**
     * 生规则成脚本
     *
     * @param ruleKpiDefine
     */

    private Set<String> generateRuleScript(RuleKpiDefine ruleKpiDefine) {
        JSONObject $param = new JSONObject();
        JSONObject object = JSON.parseObject(ruleKpiDefine.getScriptData());
        JSONArray tables = object.getJSONArray(RuleJsonKey.TABLE_MODEL);

        JSONObject prepared = prepareDeclare(tables);

        //数据模型ID
        String dataModelId = String.valueOf(prepared.remove(RuleJsonKey.DATA_MODEL_ID));

        $param.putAll(prepared);

        $param.put(RuleJsonKey.PACKAGE_NAME, EngineUtil.getPackageName(ruleKpiDefine.getRuleKpiDefineRowId()));
        JSONArray imports = new JSONArray();
        boolean isMixKpi = ruleKpiDefine.getType() == Type.MIX_KPI;
        if (isMixKpi) {
            imports.add("function com.zjhcsoft.util.Value.SummaryValue");
        }
        $param.put(RuleJsonKey.IMPORTS, imports);
        JSONArray globals = new JSONArray();
        Set<String> baseKpi = null;
        if (isMixKpi) {
            baseKpi = new HashSet<>();
            //组合指标添加DateCD
            JSONObject global = new JSONObject();
            global.put(RuleJsonKey.GLOBAL_TYPE, RuleJsonKey.DateCd.TYPE);
            global.put(RuleJsonKey.GLOBAL_INSTANCE, RuleJsonKey.DateCd.INSTANCE);
            globals.add(global);
        }
        $param.put(RuleJsonKey.GLOBALS, globals);

        JSONArray ruleArr = object.getJSONArray(RuleJsonKey.RULE_ARR);
        ruleKpiDefine.setScriptData(ruleArr.toJSONString());
        ruleArr = dealRuleArr(ruleArr, prepared.getJSONObject(RuleJsonKey.COL_JOIN_PARAM), dataModelId, isMixKpi, baseKpi);
        $param.put(RuleJsonKey.RULES_GROUP, ruleArr);
        ruleKpiDefine.setScriptRule(drlParser.parse($param));
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
            t.put(RuleJsonKey.FACT_NAME, EngineUtil.getTypeName(define.getTableDefineRowId()));
            if (Type.DATA_MODEL == define.getType()) {
                $pp.put(RuleJsonKey.DATA_MODEL_ID, define.getTableDefineRowId());
                JSONObject join = JSON.parseObject(define.getRelField());
                if (null != join) {
                    for (String k : join.keySet()) {
                        JSONObject $$jo = join.getJSONObject(k);
                        String fact = $$jo.getString(RuleJsonKey.ID_KEY);
                        if (!joinColumn.containsKey(fact)) {
                            joinColumn.put(fact, new JSONArray());
                        }
                        JSONObject $$jp = new JSONObject();
                        $$jp.put(RuleJsonKey.ID_KEY, define.getTableDefineRowId());
                        $$jp.put(RuleJsonKey.RULE_FIELD, $$jo.getString(RuleJsonKey.COL_T));
                        $$jp.put(RuleJsonKey.RULE_OP, "==");
                        $$jp.put(RuleJsonKey.RULE_VALUE, k);
                        joinColumn.getJSONArray(fact).add($$jp);
                    }
                }
            }
            tables.add(t);
        }
        $pp.put(RuleJsonKey.DECLARE, tables);
        $pp.put(RuleJsonKey.COL_JOIN_PARAM, joinColumn);
        return $pp;
    }

    //处理规则When部分
    //添加默认条件或默认关联
    private JSONArray dealRuleArr(JSONArray ruleArr, JSONObject joinColumn, String dataModelId, boolean isMixKpi, Set<String> baseKpi) {
        Set<String> tableUsed = new HashSet<>();
        for (int i = 0; i < ruleArr.size(); i++) {
            JSONObject ruleItem = ruleArr.getJSONObject(i);
            JSONArray whens = ruleItem.getJSONArray(RuleJsonKey.RULE_WHEN);
            ruleItem.put(RuleJsonKey.RULE_THEN, dealRuleThenPart(ruleItem, dataModelId, isMixKpi, baseKpi));
            //处理前台配置的条件选项
            for (int j = 0; j < whens.size(); j++) {
                String id = whens.getJSONObject(j).getString(RuleJsonKey.ID_KEY);
                if (joinColumn.containsKey(id)) {
                    JSONArray $$rules = whens.getJSONObject(j).getJSONArray(RuleJsonKey.RULE_OPS);
                    $$rules.addAll(joinColumn.getJSONArray(id));
                }
                tableUsed.add(id);
            }
            //处理关联信息
            for (String k : joinColumn.keySet()) {
                if (!tableUsed.contains(k)) {
                    JSONObject $$p = new JSONObject();
                    $$p.put(RuleJsonKey.ID_KEY, k);
                    $$p.put(RuleJsonKey.RULE_OPS, joinColumn.get(k));
                    whens.add($$p);
                    tableUsed.add(k);
                    JSONArray joinTable = joinColumn.getJSONArray(k);
                    for (int t = 0; t < joinTable.size(); t++) {
                        JSONObject _ta = joinTable.getJSONObject(t);
                        String _key = _ta.getString(RuleJsonKey.ID_KEY);
                        if (!tableUsed.contains(_key)) {
                            $$p = new JSONObject();
                            $$p.put(RuleJsonKey.ID_KEY, _key);
                            $$p.put(RuleJsonKey.RULE_OPS, new JSONArray());
                            whens.add($$p);
                            tableUsed.add(_key);
                        }
                    }
                }
            }
            //确认数据模型是否使用
            if(!tableUsed.contains(dataModelId)){
                JSONObject $$p = new JSONObject();
                $$p.put(RuleJsonKey.ID_KEY, dataModelId);
                $$p.put(RuleJsonKey.RULE_OPS, new JSONArray());
                whens.add($$p);
            }
            tableUsed.clear();
        }
        return ruleArr;
    }

    private JSONArray dealRuleThenPart(JSONObject ruleItem, String dataModelId, boolean isMixKpi, Set<String> baseKpi) {

        JSONArray jsonArray = new JSONArray();
        String dataFact = EngineUtil.getFactArgumentName(dataModelId);

        String ruleThen = ruleItem.getString(RuleJsonKey.RULE_THEN);

        if (StringUtils.isBlank(ruleItem.getString(RuleJsonKey.RULE_NAME))) {
            ruleItem.put(RuleJsonKey.RULE_NAME, "Rule" + UUID.randomUUID().toString().replaceAll("-", ""));
        }

        if (isMixKpi) {
            RuleTableDefine tblDefine = tblService.get(Long.parseLong(dataModelId));
            Pattern pattern = Pattern.compile("\\[(.+?)\\]");
            Matcher matcher = pattern.matcher(ruleThen);
            while (matcher.find()) {
                baseKpi.add(matcher.group(1));
            }
            ruleThen = ruleThen.replaceAll("\\[(.+?)\\]", "SummaryValue(\"$1\"," + RuleJsonKey.DateCd.INSTANCE + ",\\" + dataFact + "." + tblDefine.getDimField() + ")");
        }

        //数据计算部分
        jsonArray.add(dataFact + "." + RuleConstants.RuleColumn.Value.RULE_VALUE + "=" + ruleThen);
        //文字表达式部分
        StringBuilder r_expr = new StringBuilder();
        r_expr.append(dataFact).append(".").append(RuleConstants.RuleColumn.Value.RULE_EXPR).append("=");
        r_expr.append("\"").append(ruleItem.getString(RuleJsonKey.RULE_THEN_STR)).append("\"");
        jsonArray.add(r_expr.toString());
        //从WorkMemory 移除该Fact
        jsonArray.add("retract(" + dataFact + ")");
        return jsonArray;
    }

    @RequestMapping("{kpiCode}/check/")
    public ResponseVO checkKpiCodeRepeat(@PathVariable String kpiCode) {
        RuleKpiDefine ruleKpiDefine = service.findByKpiCode(kpiCode);
        return ControllerHelper.success(null == ruleKpiDefine);
    }

    @RequestMapping("exist/{groupId}/{currentId}")
    public ResponseVO queryExistExcludeCurrent(@PathVariable Long groupId, @PathVariable Long currentId) {
        try {
            List<Long> ids = ruleRelationService.queryIdByFromIdRelType(groupId, new String[]{Type.RULE_GROUP_BASE_RULE_KPI, Type.RULE_GROUP_MIX_RULE_KPI});
            if (null != currentId) {
                ids.remove(currentId);
            }
            return ControllerHelper.success(service.findByPkIds(ids));
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerHelper.unknownError(e.getMessage());
        }
    }

    @Override
    public ResponseVO delete(@PathVariable String pk) {
        if (StringUtils.isNumeric(pk)) {
            Long ruleId = Long.parseLong(pk);
            List<Long> beUsed = ruleRelationService.queryIdByTargetIdRelType(ruleId,new String[]{Type.MIX_BASE_KPI_REL});
            if(beUsed.isEmpty()) {
                RuleKpiDefine kpiDefine = service.get(ruleId);
                if (kpiDefine.getType() == Type.MIX_KPI) {
                    ruleRelationService.deleteAllByFromId(ruleId, new String[]{Type.MIX_BASE_KPI_REL});
                    ruleRelationService.deleteAllByTargetId(ruleId, new String[]{Type.RULE_GROUP_MIX_RULE_KPI});
                }else{
                    ruleRelationService.deleteAllByTargetId(ruleId, new String[]{Type.RULE_GROUP_BASE_RULE_KPI});
                }
                service.delete(kpiDefine);
                return ControllerHelper.success("规则定义成功删除");
            }else{
                return ControllerHelper.badRequest("有规则依赖该指标,不能删除");
            }
        }
        return ControllerHelper.badRequest("请求参数不正确");
    }
}
