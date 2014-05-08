package com.zjhcsoft.rule.common.service.impl;


import com.zjhcsoft.rule.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-4  Time: 下午2:11
 */
@Scope("prototype")
public abstract class BaseServiceImpl<T,R extends PagingAndSortingRepository<T, Long>> implements BaseService<T,R> {

    @Autowired
    protected R repository;

    @Override
    @Transactional
    public T create(T t) {
        return repository.save(t);
    }

    @Override
    @Transactional
    public T update(T t) {
        return repository.save(t);
    }

    @Override
    public T get(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<T> findByPkIds(Long[] ids) {
        return findByPkIds(CollectionUtils.arrayToList(ids));
    }

    @Override
    public List<T> findByPkIds(List<Long> ids) {
        return (List<T>) repository.findAll(ids);
    }

    @Override
    @Transactional
    public void delete(T t) {
        repository.delete(t);
    }

    @Override
    public Page<T> findPageAble(int pageNum, int pageSize) {
        return repository.findAll(new PageRequest(pageNum,pageSize));
    }
}
