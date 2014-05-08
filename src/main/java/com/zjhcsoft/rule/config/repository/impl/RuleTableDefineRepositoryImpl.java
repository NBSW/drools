package com.zjhcsoft.rule.config.repository.impl;

import com.zjhcsoft.rule.config.dto.RuleTableDefineQuery;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.config.repository.RuleTableDefineRepositoryCustom;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 2014/3/24  Time: 14:42
 */
public class RuleTableDefineRepositoryImpl implements RuleTableDefineRepositoryCustom {

    @PersistenceContext(unitName = "defaultPU")
    private EntityManager entityManager;

    @Override
    public List<RuleTableDefine> findBy(RuleTableDefineQuery query) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(RuleTableDefine.class);
        Root<RuleTableDefine> root = criteriaQuery.from(RuleTableDefine.class);
        Predicate predicate = criteriaBuilder.and();

        if (StringUtils.isNotBlank(query.getTableCode())) {
            Path<String> p = root.get("tableCode");
            predicate = criteriaBuilder.and(predicate,criteriaBuilder.like(p, "%"+query.getTableCode()+"%"));
        }
        if (StringUtils.isNotBlank(query.getLatnId())) {
            predicate = criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get("latnId"),query.getLatnId()));
        }
        if (StringUtils.isNotBlank(query.getDsCode())) {
            predicate = criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get("dsCode"),query.getDsCode()));
        }
        if (null != query.getType()) {
            predicate = criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get("type"),query.getType()));
        }
        return entityManager.createQuery(criteriaQuery.where(predicate)).getResultList();
    }
}
