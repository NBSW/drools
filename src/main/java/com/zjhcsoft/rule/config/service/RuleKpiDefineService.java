package com.zjhcsoft.rule.config.service;

import com.zjhcsoft.rule.common.service.BaseService;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.repository.RuleKpiDefineRepository;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-4  Time: 下午4:25
 */
public interface RuleKpiDefineService extends BaseService<RuleKpiDefine, RuleKpiDefineRepository> {
    public RuleKpiDefine findByKpiCode(String kpiCode);

    public List<RuleKpiDefine> findByIdsWithSort(List<Long> rowIds);
}
