package com.zjhcsoft.rule.config.repository.impl;

import com.zjhcsoft.rule.config.dto.RuleTableDefineQuery;
import com.zjhcsoft.rule.config.repository.RuleTableDefineRepository;
import com.zjhcsoft.test.BaseTest2;
import org.junit.Test;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 2014/3/24  Time: 15:19
 */
public class RuleTableDefineRepositoryImplTest extends BaseTest2 {

    @Inject
    RuleTableDefineRepository repository;

    @Test
    public void testFindBy() throws Exception {
        RuleTableDefineQuery query = new RuleTableDefineQuery();
        query.setTableCode("%SALES%");
        repository.findBy(query);
    }
}
