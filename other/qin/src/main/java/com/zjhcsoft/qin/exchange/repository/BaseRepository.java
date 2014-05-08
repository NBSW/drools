package com.zjhcsoft.qin.exchange.repository;

import com.zjhcsoft.qin.exchange.entity.PKEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public abstract interface BaseRepository<E extends PKEntity> extends PagingAndSortingRepository<E, String> {

}
