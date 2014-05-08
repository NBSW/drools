package com.zjhcsoft.rule.config.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.common.service.impl.BaseServiceImpl;
import com.zjhcsoft.rule.config.dto.RuleTableDefineQuery;
import com.zjhcsoft.rule.config.entity.RuleRelation;
import com.zjhcsoft.rule.config.entity.RuleRelationPK;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.config.repository.RuleRelationRepository;
import com.zjhcsoft.rule.config.repository.RuleTableDefineRepository;
import com.zjhcsoft.rule.config.service.RuleTableDefineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-4  Time: 下午1:49
 */
@Service
public class RuleTableDefineServiceImpl extends BaseServiceImpl<RuleTableDefine, RuleTableDefineRepository> implements RuleTableDefineService {

    @Inject
    private RuleRelationRepository relationRepository;

    @Override
    public List<RuleTableDefine> findAll(String latnId) {
        return (List<RuleTableDefine>) repository.findAll();
    }

    @Override
    public List<RuleTableDefine> find(RuleTableDefineQuery query) {
        return repository.findBy(query);
    }

    @Override
    public List<RuleTableDefine> getParamTableList(Long ruleTableRowId) {
        List<RuleRelation> paramDefineList = relationRepository.findByIdFromIdAndIdRelTypeIn(ruleTableRowId, new String[]{RuleConstants.Type.DATA_PARAM});
        List<Long> ids = new ArrayList<>();
        for (RuleRelation relation : paramDefineList) {
            ids.add(relation.getId().getTargetId());
        }
        return findByPkIds(ids);
    }

    @Override
    public RuleTableDefine update(RuleTableDefine ruleTableDefine) {
        //处理别名字段别名
        dealOName(ruleTableDefine);
        //处理关联关系
        dealRelation(ruleTableDefine);
        return super.update(ruleTableDefine);
    }

    @Override
    public RuleTableDefine create(RuleTableDefine ruleTableDefine) {
        RuleTableDefine define = super.create(ruleTableDefine);
        return this.update(define);
    }

    @Transactional
    private void dealRelation(RuleTableDefine define) {
        List<RuleRelation> oList = relationRepository.findByIdFromIdAndIdRelTypeIn(define.getTableDefineRowId(), new String[]{RuleConstants.Type.DATA_PARAM});
        for (RuleRelation r : oList) {
            relationRepository.delete(r);
        }
        if (null != define.getType() && define.getType() == RuleConstants.Type.DATA_MODEL && StringUtils.isNotBlank(define.getRelField())) {
            JSONObject rel = JSON.parseObject(define.getRelField());
            List<RuleRelation> relationList = new ArrayList<>();
            for (String key : rel.keySet()) {
                RuleRelation $rel = new RuleRelation(new RuleRelationPK());
                $rel.getId().setFromId(define.getTableDefineRowId());
                $rel.getId().setRelType(RuleConstants.Type.DATA_PARAM);
                $rel.getId().setTargetId(rel.getJSONObject(key).getLong(RuleConstants.RuleJsonKey.ID_KEY));
                relationList.add($rel);
            }
            relationRepository.save(relationList);
        }
    }

    private void dealOName(RuleTableDefine define) {
        /*if (StringUtils.isNotBlank(define.getFields())) {
            JSONObject jsonObject = JSONObject.parseObject(define.getFields());
            JSONArray columns = jsonObject.getJSONArray(RuleConstants.RuleJsonKey.COLUMNS);
            for (int i = 0; i < columns.size(); i++) {
                JSONObject column = columns.getJSONObject(i);
                String name = column.getString(RuleConstants.RuleJsonKey.Column.NAME);
                name = "$" + name.replaceAll("_", "").toLowerCase() + define.getTableDefineRowId();
                column.put(RuleConstants.RuleJsonKey.Column.ONAME, name);
            }
            define.setFields(jsonObject.toJSONString());
        }*/
    }
}
