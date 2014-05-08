package com.zjhcsoft.rule.config.repository;

import com.zjhcsoft.rule.common.repository.LatnIdRepository;
import com.zjhcsoft.rule.config.entity.RuleTableDefine;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-3  Time: 下午7:05
 */
public interface RuleTableDefineRepository extends LatnIdRepository<RuleTableDefine,Long>,RuleTableDefineRepositoryCustom{
}
