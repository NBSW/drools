package com.zjhcsoft.rule.config.service.impl;

import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleRelation;
import com.zjhcsoft.rule.config.entity.RuleRelationPK;
import com.zjhcsoft.rule.config.repository.RuleRelationRepository;
import com.zjhcsoft.rule.config.service.RuleRelationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-3  Time: 下午7:09
 */
@Service
public class RuleRelationServiceImpl implements RuleRelationService {

    @Inject
    private RuleRelationRepository repository;

    @Override
    public List<RuleRelation> queryByFromIdRelType(Long fromId, String[] type) {
        return repository.findByIdFromIdAndIdRelTypeIn(fromId, type);
    }

    @Override
    public List<RuleRelation> queryByTargetIdRelType(Long targetId, String[] type) {
        return repository.findByIdTargetIdAndIdRelTypeIn(targetId, type);
    }

    @Override
    public List<Long> queryIdByFromIdRelType(Long fromId, String[] types) {
        List<RuleRelation> relationList = queryByFromIdRelType(fromId, types);
        List<Long> ids = new ArrayList<>();
        for (RuleRelation relation : relationList) {
            ids.add(relation.getId().getTargetId());
        }
        return ids;
    }

    @Override
    public List<Long> queryIdByTargetIdRelType(Long targetId, String[] types) {
        List<RuleRelation> relationList = queryByTargetIdRelType(targetId, types);
        List<Long> ids = new ArrayList<>();
        for (RuleRelation relation : relationList) {
            ids.add(relation.getId().getFromId());
        }
        return ids;
    }

    @Override
    public List<RuleRelation> queryByFromId(Long fromId) {
        return repository.findByIdFromId(fromId);
    }

    @Override
    public List<RuleRelation> queryByTargetId(Long targetId) {
        return repository.findByIdTargetId(targetId);
    }

    @Override
    public Page<RuleRelation> findBy(int pageNum, int pageSize, Long fromId, String[] types) {
        return repository.findByIdFromIdAndIdRelTypeIn(fromId, types, new PageRequest(pageNum, pageSize));
    }

    @Override
    public com.ecfront.easybi.dbutils.exchange.Page<Long> findIdBy(int pageNum, int pageSize, Long fromId, String[] types) {
       final Page<RuleRelation> ruleRelationPage = findBy(pageNum, pageSize, fromId, types);
        List<Long> ids = new ArrayList<>();
        for (RuleRelation relation : ruleRelationPage.getContent()) {
            ids.add(relation.getId().getTargetId());
        }
        com.ecfront.easybi.dbutils.exchange.Page page = new com.ecfront.easybi.dbutils.exchange.Page();
        page.pageNumber = ruleRelationPage.getNumber();
        page.pageSize = ruleRelationPage.getSize();
        page.pageTotal = ruleRelationPage.getTotalPages();
        page.recordTotal = ruleRelationPage.getTotalElements();
        page.objects = ids;
        return page;
    }

    @Override
    @Transactional
    public RuleRelation create(RuleRelation relation) {
        return repository.save(relation);
    }

    @Override
    public RuleRelation create(Long fromId, Long targetId, String type) {
        RuleRelation relation = new RuleRelation(new RuleRelationPK(fromId, targetId, type));
        return this.create(relation);
    }

    @Override
    @Transactional
    public void delete(RuleRelation relation) {
        repository.delete(relation);
    }

    @Override
    @Transactional
    public void deleteAllByFromId(Long fromId, String[] type) {
        List rList = queryByFromIdRelType(fromId,type);
        repository.delete(rList);
    }

    @Override
    @Transactional
    public void deleteAllByTargetId(Long targetId, String[] type) {
        List rList = queryByTargetIdRelType(targetId,type);
        repository.delete(rList);
    }
}
