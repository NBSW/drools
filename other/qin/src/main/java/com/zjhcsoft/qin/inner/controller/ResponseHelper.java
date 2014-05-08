package com.zjhcsoft.qin.inner.controller;

import com.alibaba.fastjson.JSON;
import com.zjhcsoft.qin.inner.ConfigContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseHelper {

    public static void writeValue(HttpServletRequest request, HttpServletResponse response, Object value) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("pragma", "no-cache");
        response.setHeader("cache-control", "no-cache");
        PrintWriter out = response.getWriter();
        if (null == request.getContentType() || -1 != request.getContentType().indexOf("application/jsonp")) {
            //request.getContentType() is null when  request content-type is 'application/jsonp'
            out.write(ConfigContainer.JSONP_CALLBACK + "(" + JSON.toJSONString(value) + ")");
        } else {
            out.write(JSON.toJSONString(value));
        }
        out.flush();
        out.close();
    }

}
