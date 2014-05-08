package com.zjhcsoft.rule.config.service;

import com.zjhcsoft.rule.config.entity.RuleRelation;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-3  Time: 下午4:42
 */
public interface RuleRelationService {
    public List<RuleRelation> queryByFromIdRelType(Long fromId, String[] type);

    public List<RuleRelation> queryByTargetIdRelType(Long targetId, String[] type);

    public List<Long> queryIdByFromIdRelType(Long fromId, String[] type);

    public List<Long> queryIdByTargetIdRelType(Long targetId, String[] types);

    public List<RuleRelation> queryByFromId(Long fromId);

    public List<RuleRelation> queryByTargetId(Long targetId);

    public Page<RuleRelation> findBy(int pageNum, int pageSize, Long fromId, String[] types);

    public com.ecfront.easybi.dbutils.exchange.Page<Long> findIdBy(int pageNum, int pageSize, Long fromId, String[] types);

    public RuleRelation create(RuleRelation relation);

    public RuleRelation create(Long fromId, Long targetId, String type);

    public void delete(RuleRelation relation);

    public void deleteAllByFromId(Long fromId, String[] type);

    public void deleteAllByTargetId(Long targetId, String[] type);
}
