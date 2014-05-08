package com.zjhcsoft.rule.log.service.impl;

import com.zjhcsoft.qin.exchange.utils.StringUtils;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.common.service.impl.BaseServiceImpl;
import com.zjhcsoft.rule.log.entity.RuleKpiProcessLog;
import com.zjhcsoft.rule.log.repository.RuleKpiProcessLogRepository;
import com.zjhcsoft.rule.log.service.RuleKpiProcessLogService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * Created by XuanLubin on 2014/4/2.
 */
@Service
public class RuleKpiProcessLogServiceImpl extends BaseServiceImpl<RuleKpiProcessLog, RuleKpiProcessLogRepository> implements RuleKpiProcessLogService {
    @Override
    public void changeLogStatus(Long logRowId, Integer status, String message, boolean isEnd) {
        RuleKpiProcessLog log = get(logRowId);
        if (log.getStatus() != RuleConstants.Status.EXCEPTION) {
            log.setStatus(status);
        }
        if (StringUtils.isNotBlank(message)) {
            log.setMessage(log.getMessage() + message + ";");
        }
        if (isEnd) {
            log.setEndTime(Calendar.getInstance().getTime());
        }
        update(log);
    }

    private static Sort logSort;

    static {
        logSort = new Sort(Sort.Direction.DESC, "DateCd");
    }

    @Override
    public RuleKpiProcessLog findLatestLog(String kpiCode) {
        Pageable pageable = new PageRequest(0, 1, logSort);
        Page<RuleKpiProcessLog> logPage = repository.findByKpiCode(kpiCode, pageable);
        if (null != logPage && null != logPage.getContent() && logPage.getContent().size() > 0)
            return logPage.getContent().get(0);
        return null;
    }

    @Override
    public RuleKpiProcessLog findLatestLogWithDateCd(String kpiCode, String dateCd) {
        Pageable pageable = new PageRequest(0, 1, logSort);
        Page<RuleKpiProcessLog> logPage = repository.findByKpiCodeAndDateCd(kpiCode, dateCd, pageable);
        if (null != logPage && null != logPage.getContent() && logPage.getContent().size() > 0)
            return logPage.getContent().get(0);
        return null;
    }

    @Override
    public boolean isFinish(String kpiCode, String dateCd) {
        RuleKpiProcessLog log = findLatestLogWithDateCd(kpiCode, dateCd);
        return null != log && log.getStatus() != RuleConstants.Status.RUNNING;
    }

    @Override
    public boolean isRunning(String kpiCode, String dateCd) {
        RuleKpiProcessLog log = findLatestLogWithDateCd(kpiCode, dateCd);
        return null != log && log.getStatus() == RuleConstants.Status.RUNNING;
    }
}
