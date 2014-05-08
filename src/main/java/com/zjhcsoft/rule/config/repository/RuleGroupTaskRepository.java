package com.zjhcsoft.rule.config.repository;

import com.zjhcsoft.rule.common.repository.LatnIdRepository;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-3  Time: 下午6:45
 */
public interface RuleGroupTaskRepository extends LatnIdRepository<RuleGroupTask, Long> {
    public List<RuleGroupTask> findByStatus(Integer status,Sort sort);

    public List<RuleGroupTask> findByStatusAndLatnId(Integer status,String latnId,Sort sort);

    public List<RuleGroupTask> findByRuleGroupRowId(Long ruleGroupRowId,Sort sort);

    @Query("select t from RuleGroupTask t where t.ruleGroupRowId=:ruleGroupRowId and rownum = 1 order by t.ruleGroupTaskRowId desc")
    public RuleGroupTask findLastTask(@Param("ruleGroupRowId") Long ruleGroupRowId);

    public List<RuleGroupTask> findByRuleGroupRowIdAndDateCd(Long ruleGroupRowId,String dateCd);
}
