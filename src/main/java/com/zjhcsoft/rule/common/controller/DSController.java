package com.zjhcsoft.rule.common.controller;

import com.zjhcsoft.qin.exchange.controller.ControllerHelper;
import com.zjhcsoft.qin.exchange.dto.ResponseVO;
import com.zjhcsoft.rule.datadispose.component.JDBCTemplateStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by XuanLubin on 2014/4/16. 9:30
 */
@RestController
@RequestMapping("ds")
public class DSController {
    @RequestMapping("")
    public ResponseVO findAll() {
        return ControllerHelper.success(JDBCTemplateStore.listAllDs());
    }
}
