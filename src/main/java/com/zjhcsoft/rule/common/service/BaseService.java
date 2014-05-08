package com.zjhcsoft.rule.common.service;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-4  Time: 下午2:07
 */
public interface BaseService<T, R extends PagingAndSortingRepository<T, Long>> {
    public T create(T t);

    public T update(T t);

    public T get(Long id);

    public List<T> findByPkIds(Long[] ids);

    public List<T> findByPkIds(List<Long> ids);

    public void delete(T t);

    public Page<T> findPageAble(int pageNum,int pageSize);
}
