package com.zjhcsoft.rule.config.service.impl;

import com.zjhcsoft.rule.common.service.impl.BaseServiceImpl;
import com.zjhcsoft.rule.config.entity.RuleGroup;
import com.zjhcsoft.rule.config.repository.RuleGroupRepository;
import com.zjhcsoft.rule.config.service.RuleGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-5  Time: 上午10:11
 */
@Service
public class RuleGroupServiceImpl extends BaseServiceImpl<RuleGroup, RuleGroupRepository> implements RuleGroupService {
    @Override
    public List<RuleGroup> findAll(String latnId) {
        if (StringUtils.isNotBlank(latnId)) {
            return repository.findByLatnId(latnId);
        } else {
            return (List<RuleGroup>) repository.findAll();
        }
    }

    @Override
    public List<RuleGroup> findAllByStatus(int status, String latnId) {
        if (StringUtils.isNoneBlank(latnId)) {
            return repository.findByStatusAndLatnId(status, latnId);
        } else {
            return repository.findByStatus(status);
        }
    }
}
