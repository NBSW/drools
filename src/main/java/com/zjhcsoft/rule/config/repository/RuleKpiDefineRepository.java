package com.zjhcsoft.rule.config.repository;

import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-4  Time: 下午4:24
 */
public interface RuleKpiDefineRepository extends PagingAndSortingRepository<RuleKpiDefine, Long> {
    public RuleKpiDefine findByKpiCode(String kpiCode);

    public List<RuleKpiDefine> findByRuleKpiDefineRowIdIn(List<Long> rowIds,Sort sort);
}
