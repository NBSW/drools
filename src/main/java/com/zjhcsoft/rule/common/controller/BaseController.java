package com.zjhcsoft.rule.common.controller;

import com.zjhcsoft.qin.exchange.controller.ControllerHelper;
import com.zjhcsoft.qin.exchange.dto.ResponseVO;
import com.zjhcsoft.rule.common.service.BaseService;
import com.zjhcsoft.rule.common.util.LatnIdHelper;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-12  Time: 下午2:11
 */
@Scope("prototype")
public abstract class BaseController<S extends BaseService<E, ?>, E> {

    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected S service;

    @Inject
    protected LatnIdHelper latnIdHelper;

    @RequestMapping("/{pk}")
    public ResponseVO<E> get(@PathVariable String pk) {
        E e = service.get(NumberUtils.createLong(pk));
        if (null != e)
            return ControllerHelper.success(e);
        else
            return ControllerHelper.unknownError("Object not found by " + pk + " @ " + this.getClass().getSimpleName());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseVO save(@RequestBody E e) {
        try {
            e = service.create(e);
            return ControllerHelper.success(e);
        } catch (Exception ex) {
            logger.error("保存异常:{}", ex);
            ex.printStackTrace();
            return ControllerHelper.unknownError("Object save failed " + e + " @ " + this.getClass().getSimpleName());
        }
    }

    @RequestMapping(value = "/{pk}", method = RequestMethod.PUT)
    public ResponseVO update(@PathVariable String pk, @RequestBody E e) {
        try {
            E _e = service.get(NumberUtils.createLong(pk));
            BeanUtils.copyProperties(e, _e);
            _e = service.update(_e);
            return ControllerHelper.success(_e);
        } catch (Exception ex) {
            logger.error("更新异常:{}", ex);
            ex.printStackTrace();
            return ControllerHelper.unknownError("Object update failed " + e + " @ " + this.getClass().getSimpleName());
        }
    }

    @RequestMapping(value = "/{pk}", method = RequestMethod.DELETE)
    public ResponseVO delete(@PathVariable String pk) {
        E e = service.get(NumberUtils.createLong(pk));
        if (null != e) {
            service.delete(e);
            return ControllerHelper.success("delete success " + pk + " @ " + this.getClass().getSimpleName());
        } else {
            return ControllerHelper.unknownError("Object delete failed " + pk + " @ " + this.getClass().getSimpleName());
        }
    }

}
