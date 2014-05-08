/**
 * Copyright(c) 2005 Ceno Techonologies, Ltd.
 *
 * History:
 *   14-2-21 下午4:48 Created by Tiwen
 */
package com.zjhcsoft.rule.engine.component;

import java.util.List;

/**
 * 规则引擎事件接口
 *
 * @author <a href="mailto:tiwen@qq.com">Tiwen</a> 
 * @version 1.0 14-2-21 下午4:48
 */
public interface RuleEngineEvent<T>
{

    /**
     * 执行之前事件
     * @param list 计算对象集合
     */
    void beforeExecute(List<T> list);
    /**
     * 异常之后事件
     * @param ex 错误异常
     */
    void onException(Exception ex);
    /**
     * 执行之后事件
     * @param list 计算对象集合
     */
    void afterExecute(List<T> list);

    abstract void setLogId(Long id);
}
