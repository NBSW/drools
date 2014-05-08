package com.zjhcsoft.rule.result.entity;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-4  Time: 下午2:51
 */
public class RuleKpiResultDetail implements Serializable {

    private String kpiName;
    private String classCode;
    private double kpiValue;
    private String expr;
    private Long ruleGroupTaskRowId;
    private String kpiCode;
    private String dateCd;
    private String latnId;
    private String commId;
    private String dimId;

    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public double getKpiValue() {
        return kpiValue;
    }

    public void setKpiValue(double kpiValue) {
        this.kpiValue = kpiValue;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public Long getRuleGroupTaskRowId() {
        return ruleGroupTaskRowId;
    }

    public void setRuleGroupTaskRowId(Long ruleGroupTaskRowId) {
        this.ruleGroupTaskRowId = ruleGroupTaskRowId;
    }

    public String getKpiCode() {
        return kpiCode;
    }

    public void setKpiCode(String kpiCode) {
        this.kpiCode = kpiCode;
    }

    public String getDateCd() {
        return dateCd;
    }

    public void setDateCd(String dateCd) {
        this.dateCd = dateCd;
    }

    public String getLatnId() {
        return latnId;
    }

    public void setLatnId(String latnId) {
        this.latnId = latnId;
    }

    public String getCommId() {
        return commId;
    }

    public void setCommId(String commId) {
        this.commId = commId;
    }

    public String getDimId() {
        return dimId;
    }

    public void setDimId(String dimId) {
        this.dimId = dimId;
    }
}
