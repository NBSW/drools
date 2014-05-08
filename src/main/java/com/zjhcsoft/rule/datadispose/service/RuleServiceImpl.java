package com.zjhcsoft.rule.datadispose.service;

import com.alibaba.fastjson.JSONObject;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.config.service.RuleKpiDefineService;
import com.zjhcsoft.rule.datadispose.util.FactRowMapper;
import com.zjhcsoft.rule.datadispose.util.JDBCFetchUtil;
import com.zjhcsoft.rule.engine.util.FactTypeColumnUtil;
import com.zjhcsoft.rule.log.service.RuleKpiProcessLogService;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.definition.type.FactType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-3  Time: 下午3:38
 */
@Component
@Lazy(false)
public class RuleServiceImpl extends RuleMixServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);

    @Override
    public List<Object> fetchParam(List paramTableList, RuleKpiDefine kpiDefine, Object[] args) {
        JDBCFetchUtil jdbcFetchUtil = new JDBCFetchUtil();
        List<Object> paramList = new ArrayList<>();
        for (int i = 0; i < paramTableList.size(); i++) {
            RuleTableDefine define = (RuleTableDefine) paramTableList.get(i);
            jdbcFetchUtil.setConnection(getJDBCTemplate(define.getDsCode()));
            FactType factType = getFactType(kpiDefine, define);
            RowMapper rowMapper = FactRowMapper.create(factType);
            try {
                jdbcFetchUtil.createCursor(getFetchSql(define, factType, kpiDefine), args);
                paramList.addAll(jdbcFetchUtil.next(100000, rowMapper));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return paramList;
    }

    private RuleServiceSqlExecute sqlExecute = new RuleServiceSqlExecute();

    @Override
    public String getFetchSql(RuleTableDefine define, FactType factType, RuleKpiDefine kpiDefine) {
        StringBuilder fetchSql = new StringBuilder(define.getSelectSql());
        if (!fetchSql.toString().contains("where")) {
            fetchSql.append(" where 1=1 ");
        }
        if (StringUtils.isNotBlank(define.getDateField())) {
            fetchSql.append("and ").append(define.getDateField()).append(" = ? ");
        }
       /* if (RuleConstants.Type.DATA_MODEL == define.getType()) {
            JSONObject object = JSONObject.parseObject(define.getFields());
            JSONObject tagColumn = object.getJSONObject(RuleConstants.RuleJsonKey.TAG_COLUMN);
            boolean isString = !tagColumn.getBoolean(RuleConstants.RuleJsonKey.Column.NUMBER);
            fetchSql.append("and (").append(tagColumn.getString("name")).append(" <> ").append(isString ? "'" : "").append(RuleConstants.Status.USED).append(isString ? "' " : " ");
            fetchSql.append("or ").append(tagColumn.getString("name")).append(" is null)");
        } else */

        try {
            JSONObject object = JSONObject.parseObject(define.getFields());
            JSONObject otherParam = object.getJSONObject(RuleConstants.RuleJsonKey.OTHER_PARAM);
            if (null != otherParam) {
                String stringSemicolon = !otherParam.getBoolean(RuleConstants.RuleJsonKey.Column.NUMBER) ? "'" : "";
                fetchSql.append("and ").append(otherParam.getString(RuleConstants.RuleJsonKey.Column.NAME)).append("= ");
                fetchSql.append(stringSemicolon).append(kpiDefine.getOtherParam()).append(stringSemicolon).append(" ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("其他参数处理异常");
        }


        if (RuleConstants.Type.PARAM_MODEL == define.getType() && StringUtils.isNotBlank(define.getDimField())) {
            fetchSql.append("and ").append(define.getDimField()).append(" = '").append(define.getLatnId()).append("'");
        }

        String _sql = fetchSql.toString().toLowerCase();
        _sql = sqlExecute.executeColumn(_sql, factType);
        _sql = sqlExecute.executeSqlStaticParam(_sql, getRuleGroupTask());

        logger.debug("{}模型数据获取SQL: {}", RuleConstants.Type.DATA_MODEL == define.getType() ? "数据" : "参数", _sql);
        return _sql;
    }

    //todo 参数设置
    @Override
    public Object[] getDataFetchParam() {
        return new Object[]{getRuleGroupTask().getDateCd()};
    }

    //todo 参数设置
    @Override
    public Object[] getParaFetchParam() {
        return new Object[]{getRuleGroupTask().getDateCd()};
    }
}
