package com.zjhcsoft.rule.result.service;

import java.util.List;

/**
 * Created by XuanLubin on 2014/4/9. 23:32
 */
public interface RuleKpiResultBaseService<T> {
    public int save(T t);
    public void save(List<T> list);
    public void delete(String kpiCode, String dateCd, String dimId);
}
