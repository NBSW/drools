package com.zjhcsoft.rule.datadispose.component;

import com.zjhcsoft.jaxrs.AuthService;
import com.zjhcsoft.shreport.ds.entity.DS;
import com.zjhcsoft.shreport.webservice.ShReportRest;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-5  Time: 下午4:37
 */
@Service
@Lazy(false)
public class JDBCTemplateStore {

    private static Logger logger = LoggerFactory.getLogger(JDBCTemplateStore.class);

    private static AuthService<ShReportRest> service;

    @Inject
    public void setService(AuthService<ShReportRest> service) {
        JDBCTemplateStore.service = service;
    }

    public static DS get(String dsCode) {
        try {
            DS ds = service.create().get(dsCode);
            logger.debug("数据源成功加载");
            cacheDs(ds);
            return ds;
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("数据源加载失败 重localCache 获取");
            return LOCAL_DS_CACHE.get(dsCode);
        }
    }

    private static void cacheDs(DS ds) {
        LOCAL_DS_CACHE.put(ds.getCode(), ds);
    }

    public static Collection<DS> listAllDs() {
        Collection<DS> dss;
        try {
            dss = service.create().getAll();
            flushDsCache(dss);
            logger.debug("数据源加载完成  {}个数据源", dss.size());
            return dss;
        } catch (Exception e) {
            e.printStackTrace();
            dss = new ArrayList<>();
            for (String key : LOCAL_DS_CACHE.keySet()) {
                DS ds = LOCAL_DS_CACHE.get(key);
                dss.add(ds);
            }
            logger.debug("数据源加载失败 从LocalCache获取  {}个数据源", dss.size());
            return dss;
        }
    }

    private static void flushDsCache(Collection<DS> dss) {
        if (null != dss) {
            LOCAL_DS_CACHE.clear();
            for (DS ds : dss) {
                LOCAL_DS_CACHE.put(ds.getCode(), ds);
            }
        }
    }

    private static JdbcTemplate createJDBCTemplate(DS ds1) {
        Assert.notNull(ds1);
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(ds1.getUrl());
        ds.setDriverClassName(ds1.getDriver());
        ds.setUsername(ds1.getUsername());
        ds.setPassword(ds1.getPassword());
        ds.setDefaultAutoCommit(true);
        ds.setInitialSize(ds1.getInitialSize());
        ds.setMaxActive(ds1.getMaxActive());
        ds.setMinIdle(ds1.getMinIdle());
        ds.setMaxIdle(ds1.getMaxIdle());
        ds.setMaxWait(ds1.getMaxWait());
        //check Connection Stable
        //ds.getConnection().close();
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setMaxRows(10000);
        jdbcTemplate.setFetchSize(5000);
        jdbcTemplate.setDataSource(ds);
        return jdbcTemplate;
    }

    public static JdbcTemplate getJDBCTemplate(String dsCode) {
        DS ds = get(dsCode);
        ds.getCode();
        return createJDBCTemplate(ds);
    }

    private static Map<String, DS> LOCAL_DS_CACHE = new HashMap<>();
}
