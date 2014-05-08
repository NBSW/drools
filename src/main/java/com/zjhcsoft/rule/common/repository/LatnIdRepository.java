package com.zjhcsoft.rule.common.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by XuanLubin on 2014/4/4. 10:52
 */
@NoRepositoryBean
public interface LatnIdRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {
    List<T> findByLatnId(String latnId, Sort sort);

    List<T> findByLatnId(String latnId);
}
