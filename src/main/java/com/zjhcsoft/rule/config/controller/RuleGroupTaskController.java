package com.zjhcsoft.rule.config.controller;

import com.zjhcsoft.qin.exchange.controller.ControllerHelper;
import com.zjhcsoft.qin.exchange.dto.ResponseVO;
import com.zjhcsoft.rule.common.controller.BaseController;
import com.zjhcsoft.rule.common.util.LatnIdHelper;
import com.zjhcsoft.rule.config.entity.RuleGroupTask;
import com.zjhcsoft.rule.config.service.RuleGroupTaskService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-12  Time: 下午3:59
 */
@RestController
@RequestMapping("config/task")
public class RuleGroupTaskController extends BaseController<RuleGroupTaskService,RuleGroupTask>{
    @RequestMapping("")
    public ResponseVO findAll(HttpServletRequest request){
        return ControllerHelper.success(service.findAll(latnIdHelper.getLatnId(request.getSession())));
    }
}
