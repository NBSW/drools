package com.zjhcsoft.rule.config.entity;

import com.zjhcsoft.rule.common.entity.Schema;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-4  Time: 下午4:15
 */
@Entity
@Table(name = "Rule_Kpi_Define")
public class RuleKpiDefine extends Schema implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_seq_generator")
    @SequenceGenerator(name = "rule_seq_generator", sequenceName = "rule_seq_row_id", allocationSize = 1)
    @Column(name = "rule_kpi_define_ROW_ID")
    private Long ruleKpiDefineRowId;
    @Column(name = "KPI_CODE", length = 50)
    private String kpiCode;
    @Column(name = "KPI_NAME", length = 200)
    private String kpiName;
    @Column(name = "CLASS_CODE", length = 50)
    private String classCode;
    @Column(name = "remark", length = 200)
    private String remark;
    @Column(name = "deadline")
    private Date deadline;
    @Column(name = "status", precision = 1)
    private Integer status;
    @Column(name = "latn_id", length = 10)
    private String latnId;
    @Column(name = "comm_name", length = 50)
    private String commName;
    @Column(name = "CREATE_USER_NAME", length = 50)
    private String createUserName;
    @Column(name = "CREATE_USER_ROW_ID")
    private Long createUserRowId;
    @Column(name = "create_Date")
    private Date createDate;
    @Column(name = "script_rule", columnDefinition = "clob")
    private String scriptRule;
    @Column(name = "script_data", columnDefinition = "clob")
    private String scriptData;
    @Column(name = "rule_Remark", length = 500)
    private String ruleRemark;

    @Column(name="type")
    private Integer type;

    @Column(name="other_param")
    private String otherParam;

    @ManyToOne(cascade = {},fetch = FetchType.EAGER)
    @JoinColumn(name = "table_define_ROW_ID")
    private RuleTableDefine ruleTableDefine;

    public Long getRuleKpiDefineRowId() {
        return ruleKpiDefineRowId;
    }

    public void setRuleKpiDefineRowId(Long ruleKpiDefineRowId) {
        this.ruleKpiDefineRowId = ruleKpiDefineRowId;
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

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLatnId() {
        return latnId;
    }

    public void setLatnId(String latnId) {
        this.latnId = latnId;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getCreateUserRowId() {
        return createUserRowId;
    }

    public void setCreateUserRowId(Long createUserRowId) {
        this.createUserRowId = createUserRowId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getScriptRule() {
        return scriptRule;
    }

    public void setScriptRule(String scriptRule) {
        this.scriptRule = scriptRule;
    }

    public String getScriptData() {
        return scriptData;
    }

    public void setScriptData(String scriptData) {
        this.scriptData = scriptData;
    }

    public String getRuleRemark() {
        return ruleRemark;
    }

    public void setRuleRemark(String ruleRemark) {
        this.ruleRemark = ruleRemark;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public RuleTableDefine getRuleTableDefine() {
        return ruleTableDefine;
    }

    public void setRuleTableDefine(RuleTableDefine ruleTableDefine) {
        this.ruleTableDefine = ruleTableDefine;
    }

    public String getOtherParam() {
        return otherParam;
    }

    public void setOtherParam(String otherParam) {
        this.otherParam = otherParam;
    }
}
