package com.zjhcsoft.rule.common.database;

import com.zjhcsoft.rule.datadispose.component.JDBCTemplateStore;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-13  Time: 上午8:54
 */
public abstract class TableColumn {
    protected JdbcTemplate jdbcTemplate;

    public abstract List<Column> getColumnInfo(String tableSpace, String tableName);

    public abstract boolean tableExist(String tableSpace, String tableName);

    public void setJDBCTemplate(String dsCode) {
        this.jdbcTemplate = JDBCTemplateStore.getJDBCTemplate(dsCode);
    }

    public static TableColumn get(String dsCode, String driver) {
        TableColumn tableColumn = null;
        if (null != driver) {
            if (driver.toUpperCase().contains("ORACLE")) {
                tableColumn = new OracleTableColumn();
            }
        }
        if (null != tableColumn) {
            tableColumn.setJDBCTemplate(dsCode);
        }
        return tableColumn;
    }
}
