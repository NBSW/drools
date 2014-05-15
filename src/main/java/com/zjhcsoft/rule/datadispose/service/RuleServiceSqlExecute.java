package com.zjhcsoft.rule.datadispose.service;

import com.alibaba.fastjson.JSONObject;
import com.zjhcsoft.rule.common.RuleConstants;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.entity.RuleKpiDefine;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import com.zjhcsoft.rule.engine.util.FactTypeColumnUtil;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.definition.type.FactType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by XuanLubin on 2014/5/6. 13:43
 * 取数SQL处理
 */
public class RuleServiceSqlExecute {

    private static final Logger logger = LoggerFactory.getLogger(RuleServiceSqlExecute.class);

    protected String executeSqlStaticParam(String origSql, RuleGroupTask task, RuleTableDefine define) {
        Assert.hasText(origSql, "sql不能为空");
        Assert.notNull(task, "任务不能空");
        Assert.hasText(task.getDateCd(), "账期不能为空");
        //源SQL小写处理
        origSql = origSql.toLowerCase();
        String tSql = origSql;


        if (origSql.contains("[bil_month]")) {
            tSql = origSql.replaceAll("\\[bil_month\\]", task.getDateCd());
        } else {
            if (StringUtils.isNotBlank(define.getDateField())) {
                StringBuilder fetchSql = new StringBuilder(tSql);
                fetchSql.append("and ").append(define.getDateField()).append(" = '").append(task.getDateCd()).append("'");
            }
        }

        if (origSql.contains("[latn_id]")) {
            tSql = tSql.replaceAll("\\[latn_id\\]", task.getLatnId());
        }

        logger.debug("\n源SQL:\n{}   \n处理后SQL:\n{}", origSql, tSql);
        return tSql;
    }


    protected String executeColumn(String origSql, FactType factType) {
        Assert.hasText(origSql, "sql不能为空");
        String tSql = origSql;
        if (origSql.contains("[column]")) {
            List<String> column = FactTypeColumnUtil.columnList(factType);
            tSql = origSql.replace("[column]", StringUtils.join(column, ","));
        }
        logger.debug("源SQL:{}   处理后SQL:{}", origSql, tSql);
        return tSql;
    }


    protected String executeOtherParam(String origSql, RuleTableDefine define, RuleKpiDefine kpi) {
        Assert.hasText(origSql, "sql不能为空");
        origSql = origSql.toLowerCase();
        String tSql = origSql;
        if (!origSql.contains("[otherparam]")) {
            StringBuilder fetchSql = new StringBuilder(tSql);
            try {
                JSONObject object = JSONObject.parseObject(define.getFields());
                JSONObject otherParam = object.getJSONObject(RuleConstants.RuleJsonKey.OTHER_PARAM);
                if (null != otherParam) {
                    String stringSemicolon = !otherParam.getBoolean(RuleConstants.RuleJsonKey.Column.NUMBER) ? "'" : "";
                    fetchSql.append("and ").append(otherParam.getString(RuleConstants.RuleJsonKey.Column.NAME)).append("= ");
                    fetchSql.append(stringSemicolon).append(kpi.getOtherParam()).append(stringSemicolon).append(" ");
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("其他参数处理异常");
            }
            tSql = fetchSql.toString();
        } else {
            tSql = tSql.replaceAll("\\[otherparam\\]", kpi.getOtherParam());
        }
        logger.debug("源SQL:{}   处理后SQL:{}", origSql, tSql);
        return tSql;
    }
}
