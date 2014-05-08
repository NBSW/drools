package com.zjhcsoft.qin.exchange.controller;

import com.zjhcsoft.qin.exchange.entity.PKEntity;
import com.zjhcsoft.qin.exchange.service.SimpleServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 带CRUD的控制器
 * @param <T>
 * @param <E>
 */
public abstract class SimpleController<T extends SimpleServiceImpl, E extends PKEntity> extends SimpleVOController<T, E, E> {

    @Override
    public void entityToVO(E entity, E vo) throws Exception {

    }

    @Override
    public void voToEntity(E vo, E entity) throws Exception {

    }

    private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);
}
