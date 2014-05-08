package com.zjhcsoft.rule.result.service.impl;

import com.zjhcsoft.rule.result.dao.RuleKpiResultDetailDao;
import com.zjhcsoft.rule.result.entity.RuleKpiResultDetail;
import com.zjhcsoft.rule.result.service.RuleKpiResultDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-4  Time: 下午3:11
 */
@Service
public class RuleKpiResultDetailServiceImpl implements RuleKpiResultDetailService {

    @Inject
    private RuleKpiResultDetailDao ruleKpiResultDao;

    @Override
    @Transactional("jdbcTransactionManager")
    public int save(RuleKpiResultDetail t) {
        return ruleKpiResultDao.save(t);
    }

    @Override
    @Transactional("jdbcTransactionManager")
    public void save(List<RuleKpiResultDetail> list) {
        ruleKpiResultDao.save(list);
    }

    @Override
    @Transactional("jdbcTransactionManager")
    public void delete(String kpiCode, String dateCd, String dimId) {
        ruleKpiResultDao.delete(kpiCode, dateCd, dimId);
    }
}
