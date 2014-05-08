package com.zjhcsoft.qin.inner.security;

import com.zjhcsoft.qin.exchange.security.BaseAuthedInfo;
import com.zjhcsoft.qin.exchange.service.ServiceHelper;
import com.zjhcsoft.qin.inner.ConfigContainer;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Repository
public class SessionContainer {

    public <E extends BaseAuthedInfo> E getAuthedInfo(String token, HttpSession session) {
        Object obj = session.getAttribute(ConfigContainer.SESSION_LOGIN_NAME);
        E result = null;
        if (null != obj && obj instanceof String && SESSION_CONTENT.containsKey(obj)) {
            result = (E) SESSION_CONTENT.get(obj);
        }
        if (null == result && null != token && SESSION_CONTENT.containsKey(token)) {
            result = (E) SESSION_CONTENT.get(token);
        }
        if (null == result) {
            ServiceHelper.removeCurrentAuthedInfo();
        }
        return result;
    }

    public <E extends BaseAuthedInfo> String addAuthedInfo(E authedInfo, HttpSession session) {
        String token = System.nanoTime() + "";
        session.setAttribute(ConfigContainer.SESSION_LOGIN_NAME, token);
        SESSION_CONTENT.put(token,authedInfo);
        return token;
    }

    public void removeAuthedInfo(String token, HttpSession session) {
        Object obj = session.getAttribute(ConfigContainer.SESSION_LOGIN_NAME);
        session.removeAttribute(ConfigContainer.SESSION_LOGIN_NAME);
        if(null != obj && obj instanceof String &&SESSION_CONTENT.containsKey(obj)){
            SESSION_CONTENT.remove(obj);
        }
        if (null != token&&SESSION_CONTENT.containsKey(token)) {
            SESSION_CONTENT.remove(token);
        }
        ServiceHelper.removeCurrentAuthedInfo();
    }

    public static int getActiveSessions() {
        return SESSION_CONTENT.size();
    }

    private static Map<String, BaseAuthedInfo> SESSION_CONTENT;

    static {
        //TODO IoC
        SESSION_CONTENT=new HashMap<>();
    }

}
