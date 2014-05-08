package com.zjhcsoft.qin.inner.security;

import com.zjhcsoft.qin.exchange.controller.ControllerHelper;
import com.zjhcsoft.qin.exchange.security.BaseAuthedInfo;
import com.zjhcsoft.qin.exchange.security.RestfulSecurityAdapter;
import com.zjhcsoft.qin.inner.ConfigContainer;
import com.zjhcsoft.qin.inner.controller.ResponseHelper;
import com.zjhcsoft.qin.inner.controller.ServletHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
       if(ConfigContainer.IS_CLOSE_AUTH){
           return true;
       }
        boolean isAuth = false;
        BaseAuthedInfo authedInfo = sessionContainer.getAuthedInfo(request.getParameter(ConfigContainer.FLAG_TOKEN), request.getSession());
        if (null != authedInfo) {
            CurrentAuthedInfoContainer.set(authedInfo);
            request.setCharacterEncoding("UTF-8");
            String pathInfo = URLDecoder.decode(request.getPathInfo(), "UTF-8");
            pathInfo = pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;
            isAuth = restfulSecurityAdapter.auth(ServletHelper.getMethodType(request), pathInfo, authedInfo);
        }
        if (!isAuth) {
            ResponseHelper.writeValue(request, response, ControllerHelper.unauthorized());
            return false;
        }
        return true;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

}
