package com.zjhcsoft.rule.datadispose.service;

import com.zjhcsoft.rule.config.entity.RuleGroupTask;
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
public class RuleServiceSqlExecute{

    private static final Logger logger = LoggerFactory.getLogger(RuleServiceSqlExecute.class);

    protected String executeSqlStaticParam(String origSql,RuleGroupTask task){
        Assert.hasText(origSql,"sql不能为空");
        Assert.notNull(task,"任务不能空");
        Assert.hasText(task.getDateCd(),"账期不能为空");
        String tSql = origSql;

        if(origSql.contains("[bil_month]")){
            tSql = origSql.replaceAll("\\[bil_month\\]",task.getDateCd());
        }

        if(origSql.contains("[latn_id]")){
            tSql = tSql.replaceAll("\\[latn_id\\]",task.getLatnId());
        }

        logger.debug("源SQL:{}   处理后SQL:{}",origSql,tSql);
        return tSql;
    }


    protected String executeColumn(String origSql,FactType factType){
        Assert.hasText(origSql,"sql不能为空");
        String tSql = origSql;
        if (origSql.contains("[column]")) {
            List<String> column = FactTypeColumnUtil.columnList(factType);
            tSql = origSql.replace("[column]", StringUtils.join(column, ","));
        }
        logger.debug("源SQL:{}   处理后SQL:{}",origSql,tSql);
        return tSql;
    }
}
