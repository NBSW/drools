package com.zjhcsoft.rule.datadispose.util;

import com.zjhcsoft.rule.engine.util.FactTypeColumnUtil;
import org.kie.api.definition.type.FactType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XuanLubin on 2014/4/2. 13:53
 * 根据FactType字段过滤数据字段
 */
public class FactRowMapper implements RowMapper<Object> {

    private List<String> fieldList;

    private FactType factType;

    private FactRowMapper(List<String> fieldList, FactType factType) {
        this.fieldList = fieldList;
        this.factType = factType;
    }

    public static FactRowMapper create(FactType factType) {
        if (null != factType) {
            List<String> fieldList = FactTypeColumnUtil.columnList(factType);
            return new FactRowMapper(fieldList, factType);
        } else {
            return null;
        }
    }

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            Object o = factType.newInstance();
            for (String field : fieldList) {
                try {
                    factType.set(o, field, rs.getObject(field));
                } catch (Exception e) {
                    //防止字段不存在于sql的错误
                    e.printStackTrace();
                }
            }
            return o;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
