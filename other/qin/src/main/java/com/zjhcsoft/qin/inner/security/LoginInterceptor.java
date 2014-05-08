package com.zjhcsoft.qin.inner.security;

import com.zjhcsoft.qin.exchange.controller.ControllerHelper;
import com.zjhcsoft.qin.exchange.security.BaseAuthedInfo;
import com.zjhcsoft.qin.exchange.security.RestfulSecurityAdapter;
import com.zjhcsoft.qin.inner.controller.ResponseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        Map<String, String> authInfo = new HashMap<>();
        for (String key : request.getParameterMap().keySet()) {
            if (!key.startsWith("_")) {
                authInfo.put(key, request.getParameter(key));
            }
        }
        BaseAuthedInfo authedInfo = restfulSecurityAdapter.login(authInfo);
        if (null == authedInfo) {
            ResponseHelper.writeValue(request, response, ControllerHelper.unauthorized());
        } else {
            ResponseHelper.writeValue(request, response, ControllerHelper.success(sessionContainer.addAuthedInfo(authedInfo, request.getSession())));
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
    }

    @Inject
    private RestfulSecurityAdapter restfulSecurityAdapter;
    @Inject
    private SessionContainer sessionContainer;

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

}
