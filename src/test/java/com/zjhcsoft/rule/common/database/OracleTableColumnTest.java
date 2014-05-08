package com.zjhcsoft.rule.common.database;

import com.zjhcsoft.rule.datadispose.component.JDBCTemplateStore;
import com.zjhcsoft.shreport.ds.entity.DS;
import com.zjhcsoft.test.BaseTest2;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-13  Time: 上午9:05
 */
public class OracleTableColumnTest extends BaseTest2 {

    @Test
    public void testGetColumnInfo() throws Exception {
        DS ds = JDBCTemplateStore.get("8a8a8ada44b511ee0144b52654400000");
        TableColumn tableColumn = TableColumn.get(ds.getCode(),ds.getDriver());
        tableColumn.getColumnInfo(ds.getUsername(),"T_SALES_DETAIL");
    }
}
