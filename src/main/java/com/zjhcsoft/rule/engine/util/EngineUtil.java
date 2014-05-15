package com.zjhcsoft.rule.engine.util;

import com.zjhcsoft.rule.engine.component.RuleEngine;
import com.zjhcsoft.rule.engine.component.RuleEngineEvent;
import org.kie.api.definition.type.FactType;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-2-28  Time: 上午9:52
 */
@Component
@Lazy(false)
public class EngineUtil {

    private static RuleEngine ruleEngine;

    public static Future<ResultMessage> exec(String ruleScript, List<RuleEngineEvent> engineEventList, List facts, List params, int index, Map<String, Object> globalMap) {
        return ruleEngine.execute(ruleScript, engineEventList, facts, params, index, globalMap);
    }

    public static void removeRuleBase(String ruleContent) {
        ruleEngine.removeRuleBase(ruleContent);
    }

    public static FactType getFactType(String ruleContent, Long pId, Long tId) {
        return ruleEngine.getFactType(ruleContent, getPackageName(pId), getTypeName(tId));
    }

    public static void updateKnowledge(String new_script, String old_script) {
        ruleEngine.updateKnowledge(new_script, old_script);
    }

    public static String getPackageName(Long id) {
        return "com.zjhcsoft.engine.pkg" + id;
    }

    public static String getTypeName(Long id) {
        return "FactType" + id;
    }

    public static String getFactArgumentName(Object id) {
        return "$p" + id;
    }

    @Resource(name = "ruleEngine")
    public void setRuleEngine(RuleEngine ruleEngine) {
        EngineUtil.ruleEngine = ruleEngine;
    }
}
