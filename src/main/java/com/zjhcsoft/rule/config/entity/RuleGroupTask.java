package com.zjhcsoft.rule.config.entity;

import com.zjhcsoft.rule.common.entity.Schema;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-3  Time: 下午4:20
 */
@Entity
@Table(name = "rule_group_task")
public class RuleGroupTask extends Schema implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_seq_generator")
    @SequenceGenerator(name = "rule_seq_generator", sequenceName = "rule_seq_row_id", allocationSize = 1)
    @Column(name = "rule_Group_Task_Row_Id")
    private Long ruleGroupTaskRowId;
    @Column(name = "rule_Group_Row_Id", nullable = false, insertable = false, updatable = false)
    private Long ruleGroupRowId;
    @Column(name = "task_Code", nullable = false)
    private String taskCode;
    @Column(name = "task_Name", nullable = false)
    private String taskName;
    @Column(name = "date_Cd", nullable = false)
    private String dateCd;
    @Column(name = "cycle", nullable = false)
    private Integer cycle;
    @Column(name = "latn_Id", nullable = false)
    private String latnId;
    @Column(name = "comm_Id", nullable = false)
    private String commId;
    @Column(name = "comm_Name", nullable = false)
    private String commName;
    @Column(name = "status", nullable = false)
    private Integer status;
    @Column(name = "start_Time")
    private Date startTime;
    @Column(name = "finish_Time")
    private Date finishTime;

    @ManyToOne(cascade = {}, optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "rule_Group_Row_Id")
    private RuleGroup ruleGroup;

    public Long getRuleGroupTaskRowId() {
        return ruleGroupTaskRowId;
    }

    public void setRuleGroupTaskRowId(Long ruleGroupTaskRowId) {
        this.ruleGroupTaskRowId = ruleGroupTaskRowId;
    }

    public Long getRuleGroupRowId() {
        return ruleGroupRowId;
    }

    public void setRuleGroupRowId(Long ruleGroupRowId) {
        this.ruleGroupRowId = ruleGroupRowId;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDateCd() {
        return dateCd;
    }

    public void setDateCd(String dateCd) {
        this.dateCd = dateCd;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
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

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public RuleGroup getRuleGroup() {
        return ruleGroup;
    }

    public void setRuleGroup(RuleGroup ruleGroup) {
        this.ruleGroup = ruleGroup;
        this.setTaskName(ruleGroup.getTaskName());
        this.setTaskCode(ruleGroup.getTaskCode());
        this.setLatnId(ruleGroup.getLatnId());
        this.setCommId(ruleGroup.getCommId());
        this.setCommName(ruleGroup.getCommName());
        this.setCycle(ruleGroup.getCycle());
    }

    @Override
    public String toString() {
        return "RuleGroupTask{" +
                "ruleGroupRowId=" + ruleGroupRowId +
                ", taskCode='" + taskCode + '\'' +
                ", taskName='" + taskName + '\'' +
                ", latnId='" + latnId + '\'' +
                ", commId='" + commId + '\'' +
                ", commName='" + commName + '\'' +
                '}';
    }
}
