package com.zjhcsoft.rule.config.vo;

import com.zjhcsoft.rule.config.entity.RuleKpiDefine;

import java.io.Serializable;

/**
 * Created by XuanLubin on 2014/3/31.
 */
public class RuleKpiDefineVo implements Serializable{
    private RuleKpiDefine ruleKpiDefine;
    private Object ruleGroup;

    public RuleKpiDefineVo() {
    }

    public RuleKpiDefineVo(RuleKpiDefine ruleKpiDefine, Object ruleGroup) {
        this.ruleKpiDefine = ruleKpiDefine;
        this.ruleGroup = ruleGroup;
    }

    public RuleKpiDefine getRuleKpiDefine() {
        return ruleKpiDefine;
    }

    public void setRuleKpiDefine(RuleKpiDefine ruleKpiDefine) {
        this.ruleKpiDefine = ruleKpiDefine;
    }

    public Object getRuleGroup() {
        return ruleGroup;
    }

    public void setRuleGroup(Object ruleGroup) {
        this.ruleGroup = ruleGroup;
    }
}
