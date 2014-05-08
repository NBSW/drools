package com.zjhcsoft.rule.engine.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjhcsoft.rule.common.RuleConstants.RuleJsonKey;
import com.zjhcsoft.rule.engine.util.EngineUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-6  Time: 下午6:25
 */
@Component
public class RuleParser implements Parser {

    private static String stringAttributes = "ruleflow-group|agenda-group|activation-group|dialect|date-effective|date-expires";

    @Override
    public String parse(JSONObject rule) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append("rule \"").append(rule.getString(RuleJsonKey.RULE_NAME)).append("\"\n");
        JSONArray attributes = rule.getJSONArray(RuleJsonKey.RULE_ATTRIBUTES);
        if (null != attributes)
            for (int j = 0; j < attributes.size(); j++) {
                JSONObject attribute = attributes.getJSONObject(j);
                String value = attribute.getString(RuleJsonKey.RULE_ATTRIBUTE_VALUE);
                boolean isString = stringAttributes.contains(attribute.getString(RuleJsonKey.RULE_ATTRIBUTE_NAME));
                sb.append("\t").append(attribute.getString(RuleJsonKey.RULE_ATTRIBUTE_NAME)).append(":");
                sb.append(isString ? "\"" : "").append(value).append(isString ? "\"" : "").append("\n");
            }

        sb.append("when").append("\n");
        //todo rule_ when
        JSONArray whens = rule.getJSONArray(RuleJsonKey.RULE_WHEN);
        Map<Long, String> factMap = new HashMap<>();
        for (int j = 0; j < whens.size(); j++) {
            JSONObject when = whens.getJSONObject(j);
            Long id = when.getLong(RuleJsonKey.ID_KEY);
            String fact = EngineUtil.getTypeName(id);
            factMap.put(id, fact);
            sb.append("\t").append(EngineUtil.getFactArgumentName(id)).append(":").append(fact).append("(");
            JSONArray ops = when.getJSONArray(RuleJsonKey.RULE_OPS);
            //if(joinColumn.get)
            //别名使用记录
            Set<String> oName = new HashSet<>();
            sb.append(fieldGenerate(ops, oName));
            sb.append(")\n");
        }
        sb.append("then").append("\n");
        //todo rule_ then
        JSONArray thens = rule.getJSONArray(RuleJsonKey.RULE_THEN);
        for (int j = 0; j < thens.size(); j++) {
            sb.append("\t").append(thens.getString(j)).append(";\n");
        }
        sb.append("end").append("\n\n");
        return sb.toString();
    }

    private StringBuilder fieldGenerate(JSONArray ops, Set<String> oName) {
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < ops.size(); k++) {
            JSONObject op_k = ops.getJSONObject(k);
            JSONArray _ops = op_k.getJSONArray(RuleJsonKey.RULES);
            String join = op_k.getString(RuleJsonKey.RULE_JOIN);
            if (k > 0 && StringUtils.isBlank(join)) {
                join = "&&";
            }
            if (StringUtils.isNoneBlank(join)) {
                sb.append(" ");
                sb.append(join);
                sb.append(" ");
            }
            if (null != _ops) {
                sb.append("(");
                sb.append(fieldGenerate(_ops, oName));
                sb.append(")");
            } else if (op_k.containsKey(RuleJsonKey.ID_KEY)) {
                sb.append(op_k.getString(RuleJsonKey.RULE_FIELD));
                sb.append(" ");
                sb.append(op_k.getString(RuleJsonKey.RULE_OP));
                sb.append(" ");
                sb.append(EngineUtil.getFactArgumentName(op_k.getString(RuleJsonKey.ID_KEY))).append(".");
                sb.append(op_k.getString(RuleJsonKey.RULE_VALUE));
            } else {
                if (StringUtils.isNotBlank(op_k.getString(RuleJsonKey.RULE_FIELD))) {
                    //todo 别名;
                    /*String oNameStr = op_k.getString(RuleJsonKey.Column.ONAME);
                    if (!oName.contains(oNameStr)) {
                        sb.append(oNameStr).append(":");
                    }*/
                    sb.append(op_k.getString(RuleJsonKey.RULE_FIELD));
                    sb.append(" ");
                    sb.append(op_k.getString(RuleJsonKey.RULE_OP));
                    sb.append(" ");
                    boolean isString = RuleJsonKey.STRING.equals(op_k.getString(RuleJsonKey.RULE_TYPE));
                    if (isString)
                        sb.append("\"");
                    String val = op_k.getString(RuleJsonKey.RULE_VALUE);
                    if (!isString && StringUtils.isBlank(val))
                        val = "";
                    sb.append(val);
                    if (isString)
                        sb.append("\"");
                }
            }
        }
        return sb;
    }
}
