package com.zjhcsoft.rule.log.service;

import com.zjhcsoft.rule.common.service.BaseService;
import com.zjhcsoft.rule.log.entity.RuleKpiProcessLog;
import com.zjhcsoft.rule.log.repository.RuleKpiProcessLogRepository;

/**
 * Created by XuanLubin on 2014/4/2.
 */
public interface RuleKpiProcessLogService extends BaseService<RuleKpiProcessLog,RuleKpiProcessLogRepository>{
    public void changeLogStatus(Long logRowId,Integer status,String message,boolean isEnd);

    public RuleKpiProcessLog findLatestLog(String kpiCode);

    public RuleKpiProcessLog findLatestLogWithDateCd(String kpiCode,String dateCd);

    public boolean isFinish(String kpiCode,String dateCd);

    public boolean isRunning(String kpiCode,String dateCd);
}
