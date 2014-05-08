package com.zjhcsoft.rule.common.database;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-13  Time: 上午8:57
 */
public class OracleTableColumn extends TableColumn {

    private static String columnSql = "select col.COLUMN_NAME as \"name\",col.DATA_TYPE  as \"type\", comm.COMMENTS  as \"label\" from user_col_comments comm right join user_tab_columns col on comm.TABLE_NAME = col.TABLE_NAME and comm.COLUMN_NAME = col.COLUMN_NAME where col.table_name = ? order by col.COLUMN_ID";

    @Override
    public List<Column> getColumnInfo(String tableSpace, String tableName) {
        List<Column> columnDesc = this.jdbcTemplate.query(columnSql, new Object[]{tableName.toUpperCase()}, new RowMapper<Column>() {
            @Override
            public Column mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Column(rs.getString(1), rs.getString(3), rs.getString(2));
            }
        });
        return columnDesc;
    }

    private static String tableCheck = "select count(1)  from user_tables t where t.TABLE_NAME = ?";

    @Override
    public boolean tableExist(String tableSpace, String tableName) {
        Integer count = this.jdbcTemplate.queryForObject(tableCheck, new Object[]{tableName.toUpperCase()}, Integer.class);
        return null != count && count == 1;
    }
}
