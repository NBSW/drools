package com.zjhcsoft.rule.result.service.impl;

import com.zjhcsoft.rule.result.dao.RuleKpiResultSummaryDao;
import com.zjhcsoft.rule.result.entity.RuleKpiResultSummary;
import com.zjhcsoft.rule.result.service.RuleKpiResultSummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by XuanLubin on 2014/4/2. 16:02
 */
@Service
public class RuleKpiResultSummaryServiceImpl implements RuleKpiResultSummaryService {

    private static final Logger logger = LoggerFactory.getLogger(RuleKpiResultSummaryServiceImpl.class);

    @Inject
    private RuleKpiResultSummaryDao rkrDao;

    @Override
    @Transactional("jdbcTransactionManager")
    public void summary(String kpiCode, String dateCd) {
        logger.debug("汇总 KPICode:{} ,DateCd:{} ", kpiCode, dateCd);
        List<RuleKpiResultSummary> summaryList = rkrDao.summary(kpiCode, dateCd);
        logger.debug("汇总完成 KPICode:{} ,DateCd:{} 获得数据:{}", kpiCode, dateCd, summaryList.size());
        if (!summaryList.isEmpty()) {
            for (RuleKpiResultSummary summary : summaryList) {
                int insertResult = rkrDao.saveSummary(summary);
                logger.debug("汇总数据插入汇总表 插入状态{}", insertResult);
            }
        }
    }

    @Override
    @Transactional("jdbcTransactionManager")
    public void summaryDbSide(String kpiCode, String dateCd) {
        int summaryCount = rkrDao.summaryDbSide(kpiCode, dateCd);
        logger.debug("汇总 KPICode:{} ,DateCd:{} 生成汇总数据 {}条", kpiCode, dateCd, summaryCount);
    }

    @Override
    public RuleKpiResultSummary fetchSummary(String kpiCode, String dateCd, String dimId) {
        return rkrDao.fetchSummary(kpiCode, dateCd, dimId);
    }

    @Override
    @Transactional("jdbcTransactionManager")
    public void save(List<RuleKpiResultSummary> summaryList) {
        rkrDao.save(summaryList);
    }

    @Override
    @Transactional("jdbcTransactionManager")
    public int save(RuleKpiResultSummary summary) {
        return rkrDao.saveSummary(summary);
    }

    @Override
    @Transactional("jdbcTransactionManager")
    public void delete(String kpiCode, String dateCd, String dimId) {
        rkrDao.delete(kpiCode, dateCd, dimId);
    }
}
