package com.zjhcsoft.rule.engine.util;

import com.zjhcsoft.rule.common.RuleConstants;
import org.kie.api.definition.type.FactField;
import org.kie.api.definition.type.FactType;

import java.util.*;

/**
 * Created by XuanLubin on 2014/4/3. 18:39
 */
public class FactTypeColumnUtil {
    public static List<String> columnList(FactType factType) {
        if (null == factType) {
            return Collections.emptyList();
        }
        List<FactField> factFieldList = factType.getFields();
        List<String> fieldList = new ArrayList<>();
        for (FactField field : factFieldList) {
            String fieldName = field.getName();
            if (!RuleConstants.RuleColumn.Rule.RULE_STEP.equals(fieldName) && !RuleConstants.RuleColumn.Value.RULE_EXPR.equals(fieldName) && !RuleConstants.RuleColumn.Value.RULE_VALUE.equals(fieldName)) {
                fieldList.add(fieldName);
            }
        }
        return fieldList;
    }

    public static Map<String, Class> columnMap(FactType factType) {
        List<FactField> factFieldList = factType.getFields();
        Map<String, Class> fieldMap = new HashMap<>();
        for (FactField field : factFieldList) {
            String fieldName = field.getName();
            if (!RuleConstants.RuleColumn.Rule.RULE_STEP.equals(fieldName) && !RuleConstants.RuleColumn.Value.RULE_EXPR.equals(fieldName) && !RuleConstants.RuleColumn.Value.RULE_VALUE.equals(fieldName)) {
                fieldMap.put(fieldName, field.getType());
            }
        }
        return fieldMap;
    }
}
