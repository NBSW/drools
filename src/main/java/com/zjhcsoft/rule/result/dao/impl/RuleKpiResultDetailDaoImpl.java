package com.zjhcsoft.rule.result.dao.impl;

import com.zjhcsoft.rule.common.util.SqlFromXml;
import com.zjhcsoft.rule.result.dao.RuleKpiResultDetailDao;
import com.zjhcsoft.rule.result.entity.RuleKpiResultDetail;
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
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-11  Time: 上午9:23
 */
@Repository
public class RuleKpiResultDetailDaoImpl implements RuleKpiResultDetailDao {

    @Resource(name = "ruleResultJDBCTemplate")
    private JdbcTemplate jdbcTemplate;

    private Map<String, String> sqlMap = new HashMap<>();

    @PostConstruct
    private void init() {
        try {
            sqlMap.putAll(SqlFromXml.getSqlMap(RuleKpiResultDetail.class.getSimpleName()));
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int save(final RuleKpiResultDetail t) {
        return jdbcTemplate.update(sqlMap.get("insertSql"), new RuleKpiResultStatementSetter(t, null));
    }

    @Override
    public int[] save(final List<RuleKpiResultDetail> list) {
        return jdbcTemplate.batchUpdate(sqlMap.get("insertSql"), new RuleKpiResultStatementSetter(null, list));
    }

    @Override
    public List<RuleKpiResultDetail> fetch(String kpiCode, String dateCd, String dimId) {
        return jdbcTemplate.query(sqlMap.get("fetchSql"), new Object[]{dimId, kpiCode, dateCd}, new RuleKpiResultRowMapper());
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

    protected class RuleKpiResultStatementSetter implements BatchPreparedStatementSetter, PreparedStatementSetter {

        private RuleKpiResultDetail t;

        private List<RuleKpiResultDetail> detailList;

        public RuleKpiResultStatementSetter(RuleKpiResultDetail t, List<RuleKpiResultDetail> detailList) {
            this.t = t;
            this.detailList = detailList;
        }

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
            t = detailList.get(i);
            itemSetter(ps);
        }

        @Override
        public int getBatchSize() {
            return detailList.size();
        }

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
            itemSetter(ps);
        }

        private void itemSetter(PreparedStatement ps) throws SQLException {
            ps.setObject(1, t.getClassCode());
            ps.setObject(2, t.getCommId());
            ps.setObject(3, t.getDateCd());
            ps.setObject(4, t.getDimId());
            ps.setObject(5, t.getExpr());
            ps.setObject(6, t.getKpiCode());
            ps.setObject(7, t.getKpiName());
            ps.setObject(8, t.getKpiValue());
            ps.setObject(9, t.getLatnId());
            ps.setObject(10, t.getRuleGroupTaskRowId());
        }
    }

    protected class RuleKpiResultRowMapper implements RowMapper<RuleKpiResultDetail> {
        @Override
        public RuleKpiResultDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
            RuleKpiResultDetail result = new RuleKpiResultDetail();
            result.setLatnId(rs.getString("LATN_ID"));
            result.setKpiName(rs.getString("KPI_NAME"));
            result.setKpiCode(rs.getString("KPI_CODE"));
            result.setKpiValue(rs.getFloat("KPI_VALUE"));
            result.setClassCode(rs.getString("CLASS_CODE"));
            result.setCommId(rs.getString("COMM_ID"));
            result.setDateCd(rs.getString("DATE_CD"));
            result.setDimId(rs.getString("DIM_ID"));
            result.setRuleGroupTaskRowId(rs.getLong("RULE_GROUP_TASK_ROW_ID"));
            result.setExpr(rs.getString("EXPR"));
            return result;
        }
    }
}
