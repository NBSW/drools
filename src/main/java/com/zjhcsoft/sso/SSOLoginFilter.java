package com.zjhcsoft.sso;

import com.zjhcsoft.biap.adaptor.service.ObjectAccessor;
import com.zjhcsoft.qin.exchange.security.BaseAuthedInfo;
import com.zjhcsoft.qin.inner.ConfigContainer;
import com.zjhcsoft.qin.inner.security.SessionContainer;
import com.zjhcsoft.rule.common.entity.BIAPAuthedInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by XuanLubin on 2014/4/13. 13:26
 */
@Component
@Lazy(false)
public class SSOLoginFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(SSOLoginFilter.class);

    private static ObjectAccessor objectAccessor;

    private static SessionContainer sessionContainer;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession(false) == null ? request.getSession() : request.getSession(false);
        String loginName = (String) session.getAttribute(WebConstants.CUSTOMER_NAME);
        logger.info("进入SSOLoginFilter   LoginName:{}",loginName);
        if (null == sessionContainer.getAuthedInfo(request.getParameter(ConfigContainer.FLAG_TOKEN), session)) {
            BaseAuthedInfo biapAuthedInfo = new BIAPAuthedInfo(objectAccessor.getUserByLoginName(loginName));
            sessionContainer.addAuthedInfo(biapAuthedInfo, session);
            logger.info("恢复Session :{}",biapAuthedInfo);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    @Inject
    public void setObjectAccessor(ObjectAccessor objectAccessor) {
        SSOLoginFilter.objectAccessor = objectAccessor;
    }

    @Inject
    public void setSessionContainer(SessionContainer sessionContainer) {
        SSOLoginFilter.sessionContainer = sessionContainer;
    }
}
