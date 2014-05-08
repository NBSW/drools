package com.zjhcsoft.rule.config.repository;

import com.zjhcsoft.rule.common.repository.LatnIdRepository;
import com.zjhcsoft.rule.config.entity.RuleGroup;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-5  Time: 上午9:58
 */
public interface RuleGroupRepository extends LatnIdRepository<RuleGroup, Long> {
    public List<RuleGroup> findByStatus(int status);
    public List<RuleGroup> findByStatusAndLatnId(int status,String latnId);
}
