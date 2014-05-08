package com.zjhcsoft.rule.result.entity;

import java.io.Serializable;

/**
 * Created by XuanLubin on 2014/4/2. 14:27
 */
public class RuleKpiResultSummary implements Serializable {
    /*CREATE TABLE RULE_KPI_RESULT_SUMMARY  (
            KPI_CODE             VARCHAR2(50),
            KPI_NAME             VARCHAR2(200),
            DATE_CD              VARCHAR2(20),
            CLASS_CODE           VARCHAR2(50),
            SUM_VALUE            NUMBER(10,2),
            LATN_ID              VARCHAR2(2),
            COMM_ID              VARCHAR2(20),
            DIM_ID               VARCHAR2(20)
    )*/
    private Long ruleGroupTaskRowId;
    private String kpiCode;
    private String kpiName;
    private String dateCd;
    private String classCode;
    private float sumValue;
    private String latnId;
    private String commId;
    private String dimId;

    private String remark;

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

    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getDateCd() {
        return dateCd;
    }

    public void setDateCd(String dateCd) {
        this.dateCd = dateCd;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public float getSumValue() {
        return sumValue;
    }

    public void setSumValue(float sumValue) {
        this.sumValue = sumValue;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "RuleKpiResultSummary{" +
                "ruleGroupTaskRowId=" + ruleGroupTaskRowId +
                ", kpiCode='" + kpiCode + '\'' +
                ", kpiName='" + kpiName + '\'' +
                ", dateCd='" + dateCd + '\'' +
                ", classCode='" + classCode + '\'' +
                ", sumValue=" + sumValue +
                ", latnId='" + latnId + '\'' +
                ", commId='" + commId + '\'' +
                ", dimId='" + dimId + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
