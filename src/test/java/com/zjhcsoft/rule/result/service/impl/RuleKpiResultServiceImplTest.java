package com.zjhcsoft.rule.result.service.impl;

import com.zjhcsoft.rule.result.entity.RuleKpiResultDetail;
import com.zjhcsoft.rule.result.service.RuleKpiResultDetailService;
import com.zjhcsoft.test.BaseTest2;
import org.junit.Test;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-12  Time: 下午5:52
 */
public class RuleKpiResultServiceImplTest extends BaseTest2 {

    @Inject
    RuleKpiResultDetailService service;

    @Test
    public void testCreate() throws Exception {
        RuleKpiResultDetail result = new RuleKpiResultDetail();
        result.setDateCd("1111");
        //service.create(result);
    }
}
