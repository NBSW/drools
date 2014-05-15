/**
 * Copyright(c) 2005 Ceno Techonologies, Ltd.
 *
 * History:
 *   14-2-21 上午10:57 Created by Tiwen
 */
package com.zjhcsoft.rule.engine.component;

import com.zjhcsoft.rule.engine.util.ResultMessage;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.definition.type.FactType;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.Future;

/**
 * 规则执行引擎
 *
 * @author <a href="mailto:tiwen@qq.com">Tiwen</a>
 * @version 1.0 14-2-21 上午10:57
 */
@Component("ruleEngine")
public class RuleEngine {

    private Logger logger = LoggerFactory.getLogger(RuleEngine.class);

    private static final RuleKnowledgeStore ruleKnowledgeStore = new RuleKnowledgeStore();

    private boolean executeStatefulKnowledgeSession(StatefulKnowledgeSession kSession, List list, List params, List<RuleEngineEvent> eventList, Map<String, Object> globalMap) {
        if (null == list || list.isEmpty()) {
            return false;
        }
        for (RuleEngineEvent event : eventList) {
            event.beforeExecute(list);
        }

        if (null != globalMap && globalMap.size() > 0) {
            for (String key : globalMap.keySet()) {
                kSession.setGlobal(key, globalMap.get(key));
            }
        }

        //WorkMemory 插入参数
        for (Object p : params) {
            kSession.insert(p);
        }

        for (Object o : list) {
            kSession.insert(o);
        }
        try {
            kSession.fireAllRules();
        } catch (Exception e) {
            e.printStackTrace();
            for (RuleEngineEvent event : eventList) {
                event.onException(e);
            }
            return false;
        }
        for (RuleEngineEvent event : eventList) {
            event.afterExecute(list);
        }
        return true;
    }


    /**
     * 异步方法延时返回处理结果状态
     */
    @Async("droolsTaskExecutor")
    public Future<ResultMessage> execute(String ruleScript, List<RuleEngineEvent> engineEventList, List facts, List params, int index, Map<String, Object> globalMap) {
        StatefulKnowledgeSession session = null;
        try {
            session = ruleKnowledgeStore.newStatefulKnowledgeSession(ruleScript);
            executeStatefulKnowledgeSession(session, facts, params, engineEventList, globalMap);
            return new AsyncResult<>(new ResultMessage(facts.size(), true, index));
        } catch (Exception e) {
            logger.error("规则执行中发生异常{}  规则脚本:{}", e.toString(), ruleScript);
            e.printStackTrace();
            return new AsyncResult<>(new ResultMessage(facts.size(), false, index));
        } finally {
            if (session != null) {
                session.dispose();
            }
        }
    }

    public FactType getFactType(String scriptContent, String packageName, String typeName) {
        return ruleKnowledgeStore.getFactType(scriptContent, packageName, typeName);
    }

    public void removeRuleBase(String ruleContent) {
        ruleKnowledgeStore.removeRuleBase(ruleContent);
    }

    public void updateKnowledge(String new_script, String old_script) {
        if (StringUtils.isBlank(new_script.trim())) {
            return;
        }
        try {
            ruleKnowledgeStore.updateKnowledgeBase(new_script, old_script);
        } catch (Exception e) {
            logger.error("规则更新异常:{}", e.toString());
        }
    }

    public boolean checkRuleValid(String ruleContent) {
        return ruleKnowledgeStore.checkRuleValid(ruleContent);
    }
}
