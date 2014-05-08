package com.zjhcsoft.rule.engine.parser;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-6  Time: 下午6:28
 */
public interface Parser {
    public String parse(JSONObject jsonObject);
}
