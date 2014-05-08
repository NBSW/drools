package com.zjhcsoft.rule.log.repository;

import com.zjhcsoft.rule.log.entity.RuleKpiProcessLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by XuanLubin on 2014/4/2.
 */
@Repository
public interface RuleKpiProcessLogRepository extends PagingAndSortingRepository<RuleKpiProcessLog, Long> {

    public Page<RuleKpiProcessLog> findByKpiCode(String kpiCode, Pageable pageable);

    public Page<RuleKpiProcessLog> findByKpiCodeAndDateCd(String kpiCode, String DateCd, Pageable pageable);

}
