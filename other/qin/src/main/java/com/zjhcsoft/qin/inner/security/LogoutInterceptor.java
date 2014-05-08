package com.zjhcsoft.qin.inner.security;

import com.alibaba.fastjson.JSON;
import com.zjhcsoft.qin.exchange.controller.ControllerHelper;
import com.zjhcsoft.qin.exchange.security.BaseAuthedInfo;
import com.zjhcsoft.qin.exchange.security.RestfulSecurityAdapter;
import com.zjhcsoft.qin.inner.ConfigContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LogoutInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("pragma", "no-cache");
        response.setHeader("cache-control", "no-cache");

        String token = request.getParameter(ConfigContainer.FLAG_TOKEN);
        PrintWriter out = response.getWriter();
        if (null != token) {
            BaseAuthedInfo authedInfo = sessionContainer.getAuthedInfo(token,request.getSession());
            sessionContainer.removeAuthedInfo(token,request.getSession());
            boolean isLogout = restfulSecurityAdapter.logout(authedInfo);
            if (!isLogout) {
                out.write(JSON.toJSONString(ControllerHelper.unauthorized()));
            } else {
                out.write(JSON.toJSONString(ControllerHelper.success()));
            }
        } else {
            out.write(JSON.toJSONString(ControllerHelper.unavailable()));
        }
        out.flush();
        out.close();
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

    private static final Logger logger = LoggerFactory.getLogger(LogoutInterceptor.class);

}
