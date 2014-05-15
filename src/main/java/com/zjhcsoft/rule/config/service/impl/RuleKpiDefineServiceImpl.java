package com.zjhcsoft.rule.config.service.impl;

import com.zjhcsoft.rule.common.service.impl.BaseServiceImpl;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.repository.RuleKpiDefineRepository;
import com.zjhcsoft.rule.config.service.RuleKpiDefineService;
import com.zjhcsoft.rule.config.util.RuleKpiCache;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-4  Time: 下午4:26
 */
@Service
public class RuleKpiDefineServiceImpl extends BaseServiceImpl<RuleKpiDefine, RuleKpiDefineRepository> implements RuleKpiDefineService {
    @Override
    public RuleKpiDefine findByKpiCode(String kpiCode) {
        RuleKpiDefine kpiDefine = repository.findByKpiCode(kpiCode);
        RuleKpiCache.update(kpiCode,kpiDefine);
        return kpiDefine;
    }

    @Override
    public RuleKpiDefine update(RuleKpiDefine kpiDefine) {
        kpiDefine = super.update(kpiDefine);
        RuleKpiCache.update(kpiDefine.getKpiCode(),kpiDefine);
        return kpiDefine;
    }

    @Override
    public void delete(RuleKpiDefine kpiDefine) {
        RuleKpiCache.update(kpiDefine.getKpiCode(),null);
        super.delete(kpiDefine);
    }

    @Override
    public List<RuleKpiDefine> findByIdsWithSort(List<Long> rowIds) {
        if(null==rowIds||rowIds.size()<1)
            return Collections.emptyList();
        Sort sort = new Sort("type","ruleKpiDefineRowId");
        return repository.findByRuleKpiDefineRowIdIn(rowIds,sort);
    }


}
