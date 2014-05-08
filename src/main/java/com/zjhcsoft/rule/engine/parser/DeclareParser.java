package com.zjhcsoft.rule.engine.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjhcsoft.qin.exchange.utils.StringUtils;
import com.zjhcsoft.rule.common.RuleConstants.RuleJsonKey;
import com.zjhcsoft.rule.common.RuleConstants.RuleColumn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-6  Time: 下午5:47
 */
@Component
public class DeclareParser implements Parser {
    private final static Map<String, String> basicFlied = new HashMap() {{
        put(RuleColumn.Rule.RULE_STEP, "int=0");
        put(RuleColumn.Value.RULE_EXPR, "String=\"\"");
        put(RuleColumn.Value.RULE_VALUE, RuleJsonKey.Column.NUMBER_TYPE + "=0");
    }};

    public String parse(JSONObject jsonObject) {
        JSONArray columns = jsonObject.getJSONArray(RuleJsonKey.COLUMNS);
        StringBuilder stringBuilder = new StringBuilder("declare  ").append(jsonObject.get(RuleJsonKey.FACT_NAME)).append("\n");
        Boolean isParam = jsonObject.getBoolean(RuleJsonKey.IS_PARAM);
        for (int i = 0; i < columns.size(); i++) {
            JSONObject object = columns.getJSONObject(i);
            //非维度度量字段不参与
            if (StringUtils.isBlank(object.getString(RuleJsonKey.Column.DIMENSION))) {
                continue;
            }
            stringBuilder.append("\t//").append(object.getString(RuleJsonKey.Column.LABEL)).append("\n");
            stringBuilder.append("\t").append(object.getString(RuleJsonKey.Column.NAME)).append(":");
            stringBuilder.append(object.getBoolean(RuleJsonKey.Column.NUMBER) ? RuleJsonKey.Column.NUMBER_TYPE : "String").append("\n");
        }
        //数据对象添加默认字段
        if (null != isParam && !isParam) {
            for (String key : basicFlied.keySet()) {
                stringBuilder.append("\t").append(key).append(":").append(basicFlied.get(key)).append("\n");
            }
            /*JSONObject tag = jsonObject.getJSONObject(RuleJsonKey.TAG_COLUMN);
            if (null != tag) {
                stringBuilder.append("\t").append(tag.getString(RuleJsonKey.Column.NAME)).append(":");
                stringBuilder.append(tag.getBoolean(RuleJsonKey.Column.NUMBER) ? RuleJsonKey.Column.NUMBER_TYPE : "String").append("\n");
            }*/
        }
        stringBuilder.append("end").append("\n\n");
        return stringBuilder.toString();
    }
}
