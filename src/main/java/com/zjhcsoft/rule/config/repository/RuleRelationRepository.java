package com.zjhcsoft.rule.config.repository;

import com.zjhcsoft.rule.config.entity.RuleRelation;
import com.zjhcsoft.rule.config.entity.RuleRelationPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-3  Time: 下午7:04
 */
public interface RuleRelationRepository extends PagingAndSortingRepository<RuleRelation, RuleRelationPK> {
    public List<RuleRelation> findByIdFromIdAndIdRelTypeIn(Long fromId, String[] types);

    public List<RuleRelation> findByIdTargetIdAndIdRelTypeIn(Long targetId, String[] types);

    public List<RuleRelation> findByIdFromId(Long fromId);

    public List<RuleRelation> findByIdTargetId(Long targetId);

    public Page<RuleRelation> findByIdFromIdAndIdRelTypeIn(Long fromId, String[] type, Pageable pageable);
}
