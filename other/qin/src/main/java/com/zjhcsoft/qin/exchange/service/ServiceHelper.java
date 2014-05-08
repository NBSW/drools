package com.zjhcsoft.qin.exchange.service;


import com.zjhcsoft.qin.exchange.entity.PKEntity;
import com.zjhcsoft.qin.exchange.entity.SecurityEntity;
import com.zjhcsoft.qin.exchange.security.BaseAuthedInfo;
import com.zjhcsoft.qin.inner.security.CurrentAuthedInfoContainer;
import com.zjhcsoft.qin.inner.security.SessionContainer;

/**
 * <h1>服务辅助类</h1>
 */
public class ServiceHelper {

    /**
     * 获取当前登录对象，要求在同一线程中
     *
     * @param <E>
     * @return
     */
    public static <E extends BaseAuthedInfo> E getCurrentAuthedInfo() {
        return CurrentAuthedInfoContainer.get();
    }

    public static void removeCurrentAuthedInfo() {
        CurrentAuthedInfoContainer.remove();
    }

    /**
     * 实体组装
     *
     * @param entity
     * @param <E>
     */
    public static <E extends PKEntity> void assembleEntity(E entity) {
        if (entity instanceof SecurityEntity) {
            SecurityEntity securityEntity = (SecurityEntity) entity;
            long now = System.nanoTime();
            securityEntity.setCreateTime(now);
            securityEntity.setUpdateTime(now);
            BaseAuthedInfo authedInfo = getCurrentAuthedInfo();
            if (null != authedInfo) {
                securityEntity.setCreateUser(authedInfo.PK);
                securityEntity.setUpdateUser(authedInfo.PK);
            } else {
                securityEntity.setCreateUser("");
                securityEntity.setUpdateUser("");
            }
        }
    }

    /**
     * 实体组装，将persistenceEntity中的信息组装到entity
     *
     * @param persistenceEntity
     * @param entity
     * @param <E>
     */
    public static <E extends PKEntity> void assembleEntity(E persistenceEntity, E entity) {
        entity.setCode(persistenceEntity.getCode());
        if (entity instanceof SecurityEntity) {
            SecurityEntity securityEntity = (SecurityEntity) entity;
            securityEntity.setCreateTime(((SecurityEntity) persistenceEntity).getCreateTime());
            securityEntity.setUpdateTime(System.nanoTime());
            securityEntity.setCreateUser(((SecurityEntity) persistenceEntity).getCreateUser());
            BaseAuthedInfo authedInfo = getCurrentAuthedInfo();
            if (null != authedInfo) {
                securityEntity.setUpdateUser(authedInfo.PK);
            } else {
                securityEntity.setUpdateUser("");
            }
        }
    }

    /**
     * 获取在线人数
     * @return
     */
    public static int getActiveSessions()
    {
        return SessionContainer.getActiveSessions();
    }

}
