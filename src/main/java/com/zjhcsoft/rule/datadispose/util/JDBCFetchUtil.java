package com.zjhcsoft.rule.datadispose.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Created by XuanLubin on 2014/4/9. 8:49
 */
public class JDBCFetchUtil {

    private static Logger logger = LoggerFactory.getLogger(JDBCFetchUtil.class);

    private Connection connection;

    private PreparedStatement psmt;

    private ResultSet rst;

    private List<String> header = new ArrayList<>();

    private static final int DEFAULT_FETCH_SIZE = 10000;

    private int fetch_size = DEFAULT_FETCH_SIZE;

    public JDBCFetchUtil() {
    }

    public JDBCFetchUtil(JdbcTemplate jdbcTemplate) {
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JDBCFetchUtil(String userName, String passwd, String url, String driverClass) {
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, userName, passwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConnection(String userName, String passwd, String url, String driverClass) {
        destroy();
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, userName, passwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConnection(JdbcTemplate jdbcTemplate) {
        destroy();
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConnection(DataSource dataSource) {
        destroy();
        try {
            connection = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConnection(Connection connection) {
        destroy();
        this.connection = connection;
    }

    public void setFetchSize(int fetch_size) {
        this.fetch_size = fetch_size;
    }

    public void createCursor(String sql, Object[] arguments) throws Exception {
        logger.debug("SQL:{} , Args:{}",sql,arguments.toString());
        Assert.notNull(connection);
        try {
            psmt = connection.prepareStatement(sql);
            if (null != arguments && sql.contains("?")) {
                int paramCount = StringUtils.countOccurrencesOf(sql,"?");
                for (int i = 0; i < paramCount; i++) {
                    psmt.setObject(i + 1, arguments[i]);
                }
            }
            rst = psmt.executeQuery();
            rst.setFetchSize(fetch_size);
            ResultSetMetaData resultSetMetaData = rst.getMetaData();
            for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                header.add(resultSetMetaData.getColumnLabel(i + 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public void destroy() {
        JdbcUtils.closeResultSet(rst);
        JdbcUtils.closeStatement(psmt);
        JdbcUtils.closeConnection(connection);
    }

    private boolean hasNext() {
        try {
            return rst.next();
        } catch (Exception e) {
            return false;
        }
    }

    public Object next(RowMapper<Object> rowMapper, int... index) throws SQLException {
        if (hasNext()) {
            if (null == rowMapper) {
                Map<String, Object> row = new HashMap<>();
                for (String column : header) {
                    row.put(column, rst.getObject(column));
                }
                return row;
            } else {
                return rowMapper.mapRow(rst, index.length > 0 ? index[0] : 0);
            }
        } else {
            return null;
        }
    }

    public List<Object> next(int rowCount, RowMapper<Object> rowMapper) throws SQLException {
        if (rowCount < 1) {
            return Collections.emptyList();
        }
        List<Object> resultList = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            Object o = next(rowMapper, i + 1);
            if (null != o) {
                resultList.add(o);
            } else {
                break;
            }
        }
        return resultList;
    }


    //Demo
    public static void main(String[] args) {
        try {
            JDBCFetchUtil fetchUtil = new JDBCFetchUtil("xtest", "xtest", "jdbc:oracle:thin:@134.96.82.212:1521:orcl", "oracle.jdbc.driver.OracleDriver");
            try {
                fetchUtil.createCursor("select * from t_sales_detail t where t.STAT_CYCLE_ID = ?", new Object[]{"201401"});
            } catch (Exception e) {
                System.out.println(e);
                System.exit(-1);
            }
            int total = 0;
            long s = System.currentTimeMillis();
            while (true) {
                List<Object> resultList = fetchUtil.next(10000, null);
                total += resultList.size();
                if (resultList.size() < 1) {
                    break;
                }
                System.out.print(total);
                System.out.println("---" + (System.currentTimeMillis() - s) / 1000);
            }
            System.out.println(total);
            fetchUtil.destroy();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
