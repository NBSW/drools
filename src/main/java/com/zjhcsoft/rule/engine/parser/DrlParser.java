package com.zjhcsoft.rule.engine.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjhcsoft.rule.common.RuleConstants.RuleJsonKey;
import com.zjhcsoft.rule.engine.util.EngineUtil;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-7  Time: 上午8:59
 */
@Component
public class DrlParser implements Parser {

    @Inject
    Parser declareParser;

    @Inject
    Parser ruleParser;

    @Override
    public String parse(JSONObject jsonObject) {
        JSONArray declares = jsonObject.getJSONArray(RuleJsonKey.DECLARE);
        StringBuilder drlStr = new StringBuilder("package ").append(jsonObject.getString(RuleJsonKey.PACKAGE_NAME)).append("\n");
        JSONArray imports = jsonObject.getJSONArray(RuleJsonKey.IMPORTS);
        for (int i = 0; i < imports.size(); i++) {
            drlStr.append("import ").append(imports.getString(i)).append(";\n");
        }
        if (declares.size() > 0) {
            StringBuilder declareStr = new StringBuilder("\n");
            for (int i = 0; i < declares.size(); i++) {
                declareStr.append(declareParser.parse(declares.getJSONObject(i)));
            }
            drlStr.append(declareStr);
        }
        drlStr.append("dialect \"mvel\"\n\n");
        JSONArray globals = jsonObject.getJSONArray(RuleJsonKey.GLOBALS);
        for (int i = 0; i < globals.size(); i++) {
            JSONObject global = globals.getJSONObject(i);
            drlStr.append("global ").append(global.getString(RuleJsonKey.GLOBAL_TYPE));
            drlStr.append("\t").append(global.getString(RuleJsonKey.GLOBAL_INSTANCE)).append(";\n");
        }
        JSONArray rules = jsonObject.getJSONArray(RuleJsonKey.RULES_GROUP);
        JSONObject joinColumn = jsonObject.getJSONObject(RuleJsonKey.COL_JOIN_PARAM);
        for (int i = 0; i < rules.size(); i++) {
            JSONObject $$i = rules.getJSONObject(i);
            $$i.put(RuleJsonKey.COL_JOIN_PARAM, joinColumn);
            drlStr.append(ruleParser.parse($$i));
        }
        return drlStr.toString();
    }
}
