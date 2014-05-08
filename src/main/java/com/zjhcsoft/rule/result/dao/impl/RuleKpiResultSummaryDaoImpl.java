package com.zjhcsoft.rule.result.dao.impl;

import com.zjhcsoft.rule.common.util.SqlFromXml;
import com.zjhcsoft.rule.result.dao.RuleKpiResultSummaryDao;
import com.zjhcsoft.rule.result.entity.RuleKpiResultSummary;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.xpath.XPathExpressionException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XuanLubin on 2014/4/2. 16:03
 */
@Repository
public class RuleKpiResultSummaryDaoImpl implements RuleKpiResultSummaryDao {

    @Resource(name = "ruleResultJDBCTemplate")
    private JdbcTemplate jdbcTemplate;

    private Map<String, String> sqlMap = new HashMap<>();

    @PostConstruct
    private void init() {
        try {
            sqlMap.putAll(SqlFromXml.getSqlMap(RuleKpiResultSummary.class.getSimpleName()));
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<RuleKpiResultSummary> summary(String kpiCode, String dateCd) {
        return jdbcTemplate.query(sqlMap.get("summarySql"), new Object[]{dateCd, kpiCode}, new RuleKpiResultRowMapper());
    }

    @Override
    public int summaryDbSide(String kpiCode, String dateCd) {
        return jdbcTemplate.update(sqlMap.get("summaryDbSide"), new Object[]{dateCd, kpiCode});
    }

    @Override
    public RuleKpiResultSummary fetchSummary(String kpiCode, String dateCd, String dimId) {
        return jdbcTemplate.queryForObject(sqlMap.get("fetchSql"), new Object[]{dimId, kpiCode, dateCd}, new RuleKpiResultRowMapper());
    }

    @Override
    public int saveSummary(final RuleKpiResultSummary summary) {
        return jdbcTemplate.update(sqlMap.get("insertSql"), new RuleKpiResultStatementSetter(summary, null));
    }

    @Override
    public int[] save(List<RuleKpiResultSummary> summaryList) {
        return jdbcTemplate.batchUpdate(sqlMap.get("insertSql"), new RuleKpiResultStatementSetter(null, summaryList));
    }

    @Override
    public void delete(String kpiCode, String dateCd, String dimId) {
        if (StringUtils.isNotBlank(kpiCode) && StringUtils.isNotBlank(dateCd) && StringUtils.isNotBlank(dimId)) {
            jdbcTemplate.update(sqlMap.get("delete"), new Object[]{dimId, kpiCode, dateCd});
        } else if (StringUtils.isNotBlank(kpiCode) && StringUtils.isNotBlank(dateCd)) {
            jdbcTemplate.update(sqlMap.get("deleteWithDateCdKpiCode"), new Object[]{kpiCode, dateCd});
        } else if (StringUtils.isNotBlank(dateCd)) {
            jdbcTemplate.update(sqlMap.get("deleteWithDateCd"), new Object[]{dateCd});
        } else if (StringUtils.isNotBlank(kpiCode)) {
            jdbcTemplate.update(sqlMap.get("deleteWithKpiCode"), new Object[]{kpiCode});
        }
    }

    protected class RuleKpiResultRowMapper implements RowMapper<RuleKpiResultSummary> {
        @Override
        public RuleKpiResultSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
            RuleKpiResultSummary result = new RuleKpiResultSummary();
            result.setLatnId(rs.getString("LATN_ID"));
            result.setKpiName(rs.getString("KPI_NAME"));
            result.setKpiCode(rs.getString("KPI_CODE"));
            result.setSumValue(rs.getFloat("SUM_VALUE"));
            result.setClassCode(rs.getString("CLASS_CODE"));
            result.setCommId(rs.getString("COMM_ID"));
            result.setDateCd(rs.getString("DATE_CD"));
            result.setDimId(rs.getString("DIM_ID"));
            result.setRuleGroupTaskRowId(rs.getLong("RULE_GROUP_TASK_ROW_ID"));
            return result;
        }
    }

    protected class RuleKpiResultStatementSetter implements BatchPreparedStatementSetter, PreparedStatementSetter {

        private RuleKpiResultSummary summary;

        private List<RuleKpiResultSummary> summaryList;

        public RuleKpiResultStatementSetter(RuleKpiResultSummary summary, List<RuleKpiResultSummary> summaryList) {
            this.summary = summary;
            this.summaryList = summaryList;
        }

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
            summary = summaryList.get(i);
            itemSetter(ps);
        }

        @Override
        public int getBatchSize() {
            return summaryList.size();
        }

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
            itemSetter(ps);
        }

        private void itemSetter(PreparedStatement pss) throws SQLException {
            pss.setObject(1, summary.getKpiCode());
            pss.setObject(2, summary.getKpiName());
            pss.setObject(3, summary.getDateCd());
            pss.setObject(4, summary.getClassCode());
            pss.setObject(5, summary.getSumValue());
            pss.setObject(6, summary.getLatnId());
            pss.setObject(7, summary.getCommId());
            pss.setObject(8, summary.getDimId());
            pss.setObject(9, summary.getRuleGroupTaskRowId());
        }
    }
}
