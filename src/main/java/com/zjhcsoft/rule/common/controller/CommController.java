package com.zjhcsoft.rule.common.controller;

import com.zjhcsoft.qin.exchange.controller.ControllerHelper;
import com.zjhcsoft.qin.exchange.dto.ResponseVO;
import com.zjhcsoft.rule.common.util.LatnIdHelper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 2014/3/20  Time: 14:40
 */
@RestController
@RequestMapping("comm/")
public class CommController {

    @Inject
    private LatnIdHelper latnIdHelper;

    @RequestMapping("bdw")
    public ResponseVO getBdwInfo() {
        return ControllerHelper.success(latnIdHelper.getLatnInfo());
    }

    @RequestMapping("bdw/{code}")
    public ResponseVO getBdwInfo(@PathVariable String code) {
        return ControllerHelper.success(latnIdHelper.getLatnInfo(code));
    }

    @RequestMapping("user/bdw")
    public ResponseVO getUserBelongBdw(HttpServletRequest request) {
        return ControllerHelper.success(latnIdHelper.getLatnId(request.getSession()));
    }
}
