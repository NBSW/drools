package com.zjhcsoft.rule.config.vo;

import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.log.entity.RuleKpiProcessLog;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created by XuanLubin on 2014/4/8. 13:59
 */
public class RuleKpiLogVo {
    private Long ruleKpiDefineRowId;
    private String kpiCode;
    private String kpiName;
    private String classCode;
    private String dateCd;
    private Integer type;
    private Date startTime;
    private Date endTime;
    private String message;

    private Integer status;

    public RuleKpiLogVo(RuleKpiDefine define, RuleKpiProcessLog log) {
        BeanUtils.copyProperties(define, this);
        if (null != log) {
            BeanUtils.copyProperties(log, this);
        }else{
            this.status = null;
        }
    }

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

    public String getDateCd() {
        return dateCd;
    }

    public void setDateCd(String dateCd) {
        this.dateCd = dateCd;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
