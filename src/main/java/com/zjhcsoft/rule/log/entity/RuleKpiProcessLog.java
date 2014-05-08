package com.zjhcsoft.rule.log.entity;

import com.zjhcsoft.rule.common.entity.Schema;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by XuanLubin on 2014/4/2.
 */
@Entity
@Table(name = "rule_kpi_process_log")
public class RuleKpiProcessLog extends Schema {
    /*CREATE TABLE RULE_KPI_PROCESS_LOG  (
            RULE_LOG_ROW_ID    INTEGER,
            RULE_GROUP_ROW_ID  INTEGER,
            RULE_GROUP_NAME   VARCHAR2(50),
            RULE_GROUP_TASK_ROW_ID INTEGER,
            KPI_CODE             VARCHAR2(50),
            KPI_NAME             VARCHAR2(200),
            DATE_CD              VARCHAR2(20),
            CLASS_CODE           VARCHAR2(50),
            STATUS             INTEGER,
            LATN_ID              VARCHAR2(2),
            COMM_ID              VARCHAR2(50),
            START_TIME         DATETIME,
            END_TIME           DATETIME
    );*/
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_seq_generator")
    @SequenceGenerator(name = "rule_seq_generator", sequenceName = "rule_seq_row_id", allocationSize = 1)
    @Column(name = "rule_log_row_id")
    private Long ruleLogRowId;
    @Column(name = "rule_group_row_id")
    private Long ruleGroupRowId;
    @Column(name = "rule_group_name")
    private String ruleGroupName;
    @Column(name = "rule_group_task_row_id")
    private Long ruleGroupTaskRowId;
    @Column(name = "kpi_code")
    private String kpiCode;
    @Column(name = "kpi_name")
    private String kpiName;
    @Column(name = "date_cd")
    private String dateCd;
    @Column(name = "class_code")
    private String classCode;
    @Column(name = "status")
    private Integer status;
    @Column(name = "latn_id")
    private String latnId;
    @Column(name = "comm_id")
    private String commId;
    @Column(name = "comm_name")
    private String commName;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @Column(name = "message", columnDefinition = "clob")
    private String message;

    public Long getRuleLogRowId() {
        return ruleLogRowId;
    }

    public void setRuleLogRowId(Long ruleLogRowId) {
        this.ruleLogRowId = ruleLogRowId;
    }

    public Long getRuleGroupRowId() {
        return ruleGroupRowId;
    }

    public void setRuleGroupRowId(Long ruleGroupRowId) {
        this.ruleGroupRowId = ruleGroupRowId;
    }

    public String getRuleGroupName() {
        return ruleGroupName;
    }

    public void setRuleGroupName(String ruleGroupName) {
        this.ruleGroupName = ruleGroupName;
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

    public String getCommId() {
        return commId;
    }

    public void setCommId(String commId) {
        this.commId = commId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RuleKpiProcessLog() {
    }
}
