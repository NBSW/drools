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
@Table(name = "rule_group")
public class RuleGroup extends Schema implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_seq_generator")
    @SequenceGenerator(name = "rule_seq_generator", sequenceName = "rule_seq_row_id", allocationSize = 1)
    @Column(name = "rule_group_ROW_ID")
    private Long ruleGroupRowId;
    @Column(name = "task_Code", nullable = false)
    private String taskCode;
    @Column(name = "task_Name", nullable = false)
    private String taskName;
    @Column(name = "cycle", nullable = false)
    private Integer cycle;
    @Column(name = "status", nullable = false)
    private Integer status;
    @Column(name = "create_cron", nullable = false)
    private String createCron;
    @Column(name = "latn_Id", nullable = false)
    private String latnId;
    @Column(name = "comm_Id", nullable = false)
    private String commId;
    @Column(name = "comm_Name", nullable = false)
    private String commName;
    @Column(name = "create_User_Name")
    private String createUserName;
    @Column(name = "create_User_Row_Id")
    private Long createUserRowId;
    @Column(name = "create_Date")
    private Date createDate;

    /*@OneToMany(cascade = { CascadeType.ALL },mappedBy ="ruleGroup",fetch = FetchType.LAZY)
    private List<RuleGroupTask> ruleGroupTaskList = new ArrayList<RuleGroupTask>();*/

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

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateCron() {
        return createCron;
    }

    public void setCreateCron(String createCron) {
        this.createCron = createCron;
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

   /* public List<RuleGroupTask> getRuleGroupTaskList() {
        return ruleGroupTaskList;
    }

    public void setRuleGroupTaskList(List<RuleGroupTask> ruleGroupTaskList) {
        this.ruleGroupTaskList = ruleGroupTaskList;
    }*/
}
