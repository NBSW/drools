package com.zjhcsoft.qin.inner.controller;

import com.zjhcsoft.qin.inner.ConfigContainer;
import org.omg.CORBA.Request;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;


public class ServletHelper {

    public static HttpMethod getMethodType(HttpServletRequest req) {
        String type = req.getParameter(ConfigContainer.METHOD_TYPE);
        if (null == type || "".equalsIgnoreCase(type.trim())) {
            type = req.getMethod();
        }
        if (null != type && !"".equalsIgnoreCase(type.trim())) {
            if ("PUT".equalsIgnoreCase(type)) {
                return HttpMethod.PUT;
            } else if ("POST".equalsIgnoreCase(type)) {
                return HttpMethod.POST;
            } else if ("DELETE".equalsIgnoreCase(type)) {
                return HttpMethod.DELETE;
            } else if ("GET".equalsIgnoreCase(type)) {
                return HttpMethod.GET;
            }
        }
        return null;
    }
 }
