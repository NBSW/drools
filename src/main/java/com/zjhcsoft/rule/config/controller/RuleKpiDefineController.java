package com.zjhcsoft.rule.config.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjhcsoft.qin.exchange.controller.ControllerHelper;
import com.zjhcsoft.qin.exchange.dto.ResponseVO;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.common.RuleConstants.RuleJsonKey;
import com.zjhcsoft.rule.common.RuleConstants.Type;
import com.zjhcsoft.rule.common.controller.BaseController;
import com.zjhcsoft.rule.config.entity.*;
import com.zjhcsoft.rule.config.service.RuleGroupService;
import com.zjhcsoft.rule.config.service.RuleKpiDefineService;
import com.zjhcsoft.rule.config.service.RuleRelationService;
import com.zjhcsoft.rule.config.service.RuleTableDefineService;
import com.zjhcsoft.rule.config.util.DroolsScriptUtil;
import com.zjhcsoft.rule.config.vo.RuleKpiDefineVo;
import com.zjhcsoft.rule.engine.parser.Parser;
import com.zjhcsoft.rule.engine.util.EngineUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-12  Time: 下午2:45
 */
@RestController
@RequestMapping("config/kpi")
public class RuleKpiDefineController extends BaseController<RuleKpiDefineService, RuleKpiDefine> {

    @Inject
    private RuleRelationService ruleRelationService;

    @Inject
    private RuleGroupService ruleGroupService;

    @Inject
    private DroolsScriptUtil scriptUtil;

    @Override
    public ResponseVO get(@PathVariable String pk) {
        ResponseVO<RuleKpiDefine> _super = super.get(pk);
        RuleKpiDefine define = _super.getBody();
        if (null != define) {
            String _type = define.getType() == Type.BASE_KPI ? Type.RULE_GROUP_BASE_RULE_KPI : Type.RULE_GROUP_MIX_RULE_KPI;
            List<Long> ruleRelationList = ruleRelationService.queryIdByTargetIdRelType(define.getRuleKpiDefineRowId(), new String[]{_type});
            List<RuleGroup> ruleGroupList = ruleGroupService.findByPkIds(ruleRelationList);
            return ControllerHelper.success(new RuleKpiDefineVo(define, ruleGroupList));
        } else {
            return _super;
        }
    }

    @RequestMapping("group/{groupId}")
    public ResponseVO save(@RequestBody RuleKpiDefine ruleKpiDefine, @PathVariable Long groupId) {
        if (null == ruleKpiDefine.getStatus()) {
            ruleKpiDefine.setStatus(RuleConstants.Status.NORMAL);
        }
        ResponseVO response = super.save(ruleKpiDefine);
        if (null != response.getBody() && null != groupId) {
            ruleKpiDefine = (RuleKpiDefine) response.getBody();
            Set<String> baseKpi = scriptUtil.generateRuleScript(ruleKpiDefine,false);
            dealMixKpiRelation(ruleKpiDefine, baseKpi, Type.MIX_BASE_KPI_REL);
            if (StringUtils.isNotBlank(ruleKpiDefine.getRelKPIs())) {
                dealMixKpiRelation(ruleKpiDefine, Arrays.asList(ruleKpiDefine.getRelKPIs().split(",")), Type.MIX_BASE_KPI_REL_2);
            }
            ruleKpiDefine = service.update(ruleKpiDefine);
            String _type;
            if (Type.MIX_KPI == ruleKpiDefine.getType()) {
                _type = Type.RULE_GROUP_MIX_RULE_KPI;
            } else {
                _type = Type.RULE_GROUP_BASE_RULE_KPI;
            }
            ruleRelationService.create(groupId, ruleKpiDefine.getRuleKpiDefineRowId(), _type);
        }
        return response;
    }

    @Override
    public ResponseVO update(@PathVariable String pk, @RequestBody RuleKpiDefine ruleKpiDefine) {
        if (null == ruleKpiDefine.getStatus()) {
            ruleKpiDefine.setStatus(RuleConstants.Status.NORMAL);
        }
        Long rowId = Long.valueOf(pk);
        RuleKpiDefine oldRuleKpiDefine = service.get(rowId);
        //处理组合基础变化
        dealGroupRel(ruleKpiDefine, rowId);
        EngineUtil.removeRuleBase(oldRuleKpiDefine.getScriptRule());
        Set<String> baseKpi = scriptUtil.generateRuleScript(ruleKpiDefine,false);
        dealMixKpiRelation(ruleKpiDefine, baseKpi, Type.MIX_BASE_KPI_REL);
        if (StringUtils.isBlank(ruleKpiDefine.getRelKPIs())) {
            ruleKpiDefine.setRelKPIs("");
        }
        dealMixKpiRelation(ruleKpiDefine, Arrays.asList(ruleKpiDefine.getRelKPIs().split(",")), Type.MIX_BASE_KPI_REL_2);
        return super.update(pk, ruleKpiDefine);

    }

    private void dealGroupRel(RuleKpiDefine ruleKpiDefine, Long rowId) {
        List<RuleRelation> relationList = ruleRelationService.queryByTargetIdRelType(rowId, new String[]{Type.RULE_GROUP_MIX_RULE_KPI, Type.RULE_GROUP_BASE_RULE_KPI});
        ruleRelationService.deleteAllByTargetId(rowId, new String[]{Type.RULE_GROUP_MIX_RULE_KPI, Type.RULE_GROUP_BASE_RULE_KPI});
        RuleRelation relGroup = new RuleRelation(new RuleRelationPK());
        relGroup.getId().setFromId(relationList.get(0).getId().getFromId());
        relGroup.getId().setTargetId(rowId);
        if (Type.MIX_KPI == ruleKpiDefine.getType()) {
            relGroup.getId().setRelType(Type.RULE_GROUP_MIX_RULE_KPI);
        } else {
            relGroup.getId().setRelType(Type.RULE_GROUP_BASE_RULE_KPI);
        }
        ruleRelationService.create(relGroup);
    }

    @RequestMapping("rel/{rowId}")
    public ResponseVO getRelKpi(@PathVariable Long rowId) {
        List<Long> relKPIs = ruleRelationService.queryIdByFromIdRelType(rowId, new String[]{Type.MIX_BASE_KPI_REL_2});
        List<String> kpiCodes = new ArrayList<>();
        for (Long id : relKPIs) {
            RuleKpiDefine define = service.get(id);
            if (null != define) {
                kpiCodes.add(define.getKpiCode());
            }
        }
        return ControllerHelper.success(kpiCodes);
    }

    /**
     * 组合指标 与 基础指标关联关系处理
     *
     * @param ruleKpiDefine
     * @param baseKpi
     */
    private void dealMixKpiRelation(RuleKpiDefine ruleKpiDefine, Collection<String> baseKpi, String relType) {
        ruleRelationService.deleteAllByFromId(ruleKpiDefine.getRuleKpiDefineRowId(), new String[]{relType});
        if (null != baseKpi && baseKpi.size() > 0) {
            for (String kpiCode : baseKpi) {
                RuleKpiDefine kpiDefine = service.findByKpiCode(kpiCode);
                if (null != kpiDefine) {
                    ruleRelationService.create(ruleKpiDefine.getRuleKpiDefineRowId(), kpiDefine.getRuleKpiDefineRowId(), relType);
                }
            }
        }
    }

    @RequestMapping("{kpiCode}/check/")
    public ResponseVO checkKpiCodeRepeat(@PathVariable String kpiCode) {
        RuleKpiDefine ruleKpiDefine = service.findByKpiCode(kpiCode);
        return ControllerHelper.success(null == ruleKpiDefine);
    }

    @RequestMapping("exist/{groupId}/{currentId}")
    public ResponseVO queryExistExcludeCurrent(@PathVariable Long groupId, @PathVariable Long currentId) {
        try {
            List<Long> ids = ruleRelationService.queryIdByFromIdRelType(groupId, new String[]{Type.RULE_GROUP_BASE_RULE_KPI, Type.RULE_GROUP_MIX_RULE_KPI});
            if (null != currentId) {
                ids.remove(currentId);
            }
            return ControllerHelper.success(service.findByPkIds(ids));
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerHelper.unknownError(e.getMessage());
        }
    }

    @Override
    public ResponseVO delete(@PathVariable String pk) {
        if (StringUtils.isNumeric(pk)) {
            Long ruleId = Long.parseLong(pk);
            List<Long> beUsed = ruleRelationService.queryIdByTargetIdRelType(ruleId, new String[]{Type.MIX_BASE_KPI_REL, Type.MIX_BASE_KPI_REL_2});
            if (beUsed.isEmpty()) {
                RuleKpiDefine kpiDefine = service.get(ruleId);
                if (kpiDefine.getType() == Type.MIX_KPI) {
                    ruleRelationService.deleteAllByFromId(ruleId, new String[]{Type.MIX_BASE_KPI_REL, Type.MIX_BASE_KPI_REL_2});
                    ruleRelationService.deleteAllByTargetId(ruleId, new String[]{Type.RULE_GROUP_MIX_RULE_KPI});
                } else {
                    ruleRelationService.deleteAllByTargetId(ruleId, new String[]{Type.RULE_GROUP_BASE_RULE_KPI});
                }
                service.delete(kpiDefine);
                return ControllerHelper.success("规则定义成功删除");
            } else {
                return ControllerHelper.badRequest("有规则依赖该指标,不能删除");
            }
        }
        return ControllerHelper.badRequest("请求参数不正确");
    }
}
