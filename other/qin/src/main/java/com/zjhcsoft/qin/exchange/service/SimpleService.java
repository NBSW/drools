package com.zjhcsoft.qin.exchange.service;

import com.zjhcsoft.qin.exchange.repository.BaseRepository;
import com.zjhcsoft.qin.exchange.dto.MessageTracker;
import com.zjhcsoft.qin.exchange.dto.PageDTO;
import com.zjhcsoft.qin.exchange.entity.PKEntity;

import java.util.List;

public interface SimpleService<T extends BaseRepository<E>, E extends PKEntity> {

    boolean preGet(String pk, MessageTracker messageTracker) throws Exception;

    E postGet(E entity, MessageTracker messageTracker) throws Exception;

    boolean preSave(E entity, MessageTracker messageTracker) throws Exception;

    E postSave(E entity, MessageTracker messageTracker) throws Exception;

    boolean preUpdate(String pk, E entity, MessageTracker messageTracker) throws Exception;

    E postUpdate(String pk, E entity, MessageTracker messageTracker) throws Exception;

    boolean preDelete(String pk, MessageTracker messageTracker) throws Exception;

    String postDelete(String pk, MessageTracker messageTracker) throws Exception;

    E get(String pk) throws Exception;

    E save(E entity) throws Exception;

    E update(String pk, E entity) throws Exception;

    String delete(String pk) throws Exception;

    List<E> findAll() throws Exception;

    PageDTO<E> findAll(int pageNumber, int pageSize) throws Exception;

    List<E> findAll(MessageTracker messageTracker) throws Exception;

    /**
     * 获取所有对象
     * @param pageNumber 第几页（从1开始）
     * @param pageSize   每页多少条
     * @param messageTracker
     * @return
     * @throws Exception
     */
    PageDTO<E> findAll(int pageNumber, int pageSize, MessageTracker messageTracker) throws Exception;

    E get(String pk, MessageTracker messageTracker) throws Exception;

    E save(E entity, MessageTracker messageTracker) throws Exception;

    E update(String pk, E entity, MessageTracker messageTracker) throws Exception;

    String delete(String pk, MessageTracker messageTracker) throws Exception;
}
