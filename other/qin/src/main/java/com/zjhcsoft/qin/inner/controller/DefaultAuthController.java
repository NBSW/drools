package com.zjhcsoft.qin.inner.controller;

import com.zjhcsoft.qin.exchange.controller.ControllerHelper;
import com.zjhcsoft.qin.exchange.dto.ResponseVO;
import com.zjhcsoft.qin.exchange.security.BaseAuthedInfo;
import com.zjhcsoft.qin.inner.ConfigContainer;
import com.zjhcsoft.qin.inner.security.SessionContainer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("qin/auth")
public class DefaultAuthController {

    @RequestMapping("login/")
    public void login() {
        //此方法不用实现，只用于权限拦截器调用
    }

    @RequestMapping("logout/")
    public void logout() {
        //此方法不用实现，只用于权限拦截器调用
    }

    @RequestMapping("loginInfo/")
    public ResponseVO loginInfo(HttpServletRequest request) {
        BaseAuthedInfo authedInfo = sessionContainer.getAuthedInfo(request.getParameter(ConfigContainer.FLAG_TOKEN), request.getSession());
        if (null != authedInfo) {
            return ControllerHelper.success(authedInfo);
        } else {
            return ControllerHelper.unauthorized();
        }
    }

    @Inject
    private SessionContainer sessionContainer;

}
