package com.zjhcsoft.rule.result.event;

import com.zjhcsoft.rule.result.service.RuleKpiResultBaseService;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.engine.component.RuleEngineEvent;
import org.kie.api.definition.type.FactType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-6  Time: 下午3:50
 */
public abstract class RuleResultBaseEventListener<T, S extends RuleKpiResultBaseService<T>> implements RuleEngineEvent {

    private static Logger logger = LoggerFactory.getLogger(RuleResultBaseEventListener.class);

    private RuleKpiDefine ruleKpiDefine;
    private RuleTableDefine dataTableDefine;
    private FactType dataFactType;
    private RuleGroupTask ruleGroupTask;

    private Long logRowId;

    protected RuleResultBaseEventListener() {
    }

    public RuleResultBaseEventListener(RuleKpiDefine ruleKpiDefine, RuleTableDefine dataTableDefine, FactType dataFactType, RuleGroupTask ruleGroupTask) {
        this.ruleKpiDefine = ruleKpiDefine;
        this.dataTableDefine = dataTableDefine;
        this.dataFactType = dataFactType;
        this.ruleGroupTask = ruleGroupTask;
    }

    /**
     * 将结果数据保存到结果表
     *
     * @param list Map or FactType
     */

    public void afterExecute(List list) {
        List<T> ruleKpiResults = new ArrayList<>();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Object o = iterator.next();
            Map<String, Object> mapResult;
            if (null != dataFactType) {
                mapResult = dataFactType.getAsMap(o);
            } else {
                mapResult = (Map<String, Object>) o;
            }
            ruleKpiResults.add(extractItem(mapResult, dataTableDefine, ruleGroupTask, ruleKpiDefine));
            if (!iterator.hasNext() || ruleKpiResults.size() == 3000) {
                try {
                    getService().save(ruleKpiResults);
                } catch (Exception e) {
                    logger.error("结果保存异常  {}", e);
                    e.printStackTrace();
                } finally {
                    ruleKpiResults.clear();
                }
            }
        }
    }

    protected abstract T extractItem(Map<String, Object> mapResult, RuleTableDefine dataTableDefine, RuleGroupTask ruleGroupTask, RuleKpiDefine ruleKpiDefine);

    protected abstract S getService();

    @Override
    public void beforeExecute(List list) {

    }

    @Override
    public void onException(Exception ex) {
    }

    @Override
    public void setLogId(Long id) {
        logRowId = id;
    }
}
