package com.zjhcsoft.qin.exchange.controller;

import com.zjhcsoft.qin.exchange.entity.PKEntity;

/**
 * VO组装器
 * @param <V>
 * @param <E>
 */
public interface VOAssembler<V extends Object, E extends PKEntity> {

    void entityToVO(E entity, V vo) throws Exception;

    void voToEntity(V vo, E entity) throws Exception;

}
