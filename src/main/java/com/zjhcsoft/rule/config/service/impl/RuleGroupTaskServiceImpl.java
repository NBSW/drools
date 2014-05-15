package com.zjhcsoft.rule.config.service.impl;

import com.zjhcsoft.rule.common.service.impl.BaseServiceImpl;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.repository.RuleGroupTaskRepository;
import com.zjhcsoft.rule.config.service.RuleGroupTaskService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-4  Time: 下午1:44
 */
@Service
public class RuleGroupTaskServiceImpl extends BaseServiceImpl<RuleGroupTask, RuleGroupTaskRepository> implements RuleGroupTaskService {

    private static Logger logger = LoggerFactory.getLogger(RuleGroupTaskServiceImpl.class);

    private static final Sort taskSort = new Sort("dateCd", "ruleGroupRowId");

    @Override
    public List<RuleGroupTask> queryByStatus(Integer status, String latnId) {
        if (StringUtils.isNotBlank(latnId)) {
            return repository.findByStatusAndLatnId(status, latnId, taskSort);
        } else {
            return repository.findByStatus(status, taskSort);
        }
    }

    @Override
    public List<RuleGroupTask> queryByRuleGroupRowId(Long ruleGroupRowId) {
        return repository.findByRuleGroupRowId(ruleGroupRowId, taskSort);
    }

    @Override
    public RuleGroupTask queryLastTaskByGroupId(Long ruleGroupRowId) {
        return repository.findLastTask(ruleGroupRowId);
    }

    @Override
    public List<RuleGroupTask> findAll(String latnId) {
        if (StringUtils.isNotBlank(latnId)) {
            return repository.findByLatnId(latnId);
        } else {
            return (List<RuleGroupTask>) repository.findAll();
        }
    }

    @Override
    public boolean isExist(Long groupRowId, String dateCd) {
        int existCount = repository.findByRuleGroupRowIdAndDateCd(groupRowId, dateCd).size();
        logger.debug("主题任务重复检测 存在{}",existCount);
        return existCount > 0;
    }

    @Override
    public void deleteByRuleGroupRowId(Long groupRowId) {
        repository.delete(repository.findByRuleGroupRowId(groupRowId,null));
    }
}
