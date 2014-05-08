package com.zjhcsoft.rule.common.adapter;

import com.zjhcsoft.biap.adaptor.objectProxy.UserInsideProxy;
import com.zjhcsoft.biap.adaptor.service.AuthAccessor;
import com.zjhcsoft.biap.adaptor.service.ObjectAccessor;
import com.zjhcsoft.biap.login.LoginFailedException;
import com.zjhcsoft.qin.exchange.security.RestfulSecurityAdapter;
import com.zjhcsoft.qin.exchange.utils.MD5;
import com.zjhcsoft.rule.common.entity.BIAPAuthedInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;

@Service
public class SecurityAdapterService implements RestfulSecurityAdapter<BIAPAuthedInfo> {

    @Override
    public BIAPAuthedInfo login(Map<String, String> authInfo) {
        String userName = authInfo.get("userName");
        String password = authInfo.get("password");
        if (null == userName || "".equalsIgnoreCase(userName.trim()) || null == password || "".equalsIgnoreCase(password.trim())) {
            return null;
        }
        try {
            UserInsideProxy userInsideProxy = authAccessor.authUser(userName.trim(), MD5.str2MD5(password.trim()), null);
            BIAPAuthedInfo biapAuthedInfo = new BIAPAuthedInfo(userInsideProxy);
            //TODO
            return biapAuthedInfo;
        } catch (LoginFailedException e) {
            logger.error("Login Error.", e);
        }
        return null;
    }

    @Override
    public BIAPAuthedInfo getAuthInfo(String loginName) {
        return null;
    }

    @Override
    public boolean logout(BIAPAuthedInfo authedInfo) {
        objectAccessor.updateAudLogin(BiapEntityUtils.getOperator(authedInfo));
        return true;
    }


    @Override
    public boolean auth(HttpMethod httpMethod, String uri, BIAPAuthedInfo authedInfo) {
        if (null == authedInfo) {
            return false;
        }
        return true;
    }

    @Inject
    private AuthAccessor authAccessor;
    @Inject
    private ObjectAccessor objectAccessor;

    private static final Logger logger = LoggerFactory.getLogger(SecurityAdapterService.class);

}
