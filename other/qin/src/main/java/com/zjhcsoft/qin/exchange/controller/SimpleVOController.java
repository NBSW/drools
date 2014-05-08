package com.zjhcsoft.qin.exchange.controller;

import com.zjhcsoft.qin.exchange.dto.ResponseVO;
import com.zjhcsoft.qin.exchange.entity.PKEntity;
import com.zjhcsoft.qin.exchange.service.SimpleServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 带CRUD的控制器，存在VO转换
 *
 * @param <T>
 * @param <V>
 * @param <E>
 */
public abstract class SimpleVOController<T extends SimpleServiceImpl, V extends Object, E extends PKEntity> implements VOAssembler<V, E>, UploadAssembler {

    @RequestMapping("")
    public ResponseVO findAll(HttpServletRequest httpServletRequest) {
        return CRUDControllerHelper.findAll(httpServletRequest, this, simpleService, voClass, entityClass);
    }

    @RequestMapping("{pk}/")
    public ResponseVO get(@PathVariable String pk) {
        return CRUDControllerHelper.get(pk, this, simpleService, voClass, entityClass);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseVO save(@RequestBody V vo) {
        return CRUDControllerHelper.save(vo, this, simpleService, voClass, entityClass);
    }

    @RequestMapping(value = "{pk}/", method = RequestMethod.PUT)
    public ResponseVO update(@PathVariable String pk, @RequestBody V vo) {
        return CRUDControllerHelper.update(pk, vo, this, simpleService, voClass, entityClass);
    }

    @RequestMapping(value = "{pk}/", method = RequestMethod.DELETE)
    public ResponseVO delete(@PathVariable String pk) {
        return CRUDControllerHelper.delete(pk, simpleService);
    }

    @RequestMapping(value = "upload/", method = RequestMethod.POST)
    public ResponseVO upload(@RequestParam MultipartFile files, HttpServletRequest httpServletRequest) {
        return CRUDControllerHelper.upload(files, httpServletRequest.getSession().getServletContext().getRealPath(""), this);
    }

    @Override
    public String getUploadPath() {
        return "upload" + File.separatorChar + "help";
    }

    @Override
    public List<String> getUploadAllowType() {
        return null;
    }

    @Override
    public void onPostUpload(List<File> realFiles) {

    }

    @Override
    public boolean onPreUpload(MultipartFile[] files) {
        return true;
    }

    @Inject
    protected T simpleService;

    private Class<V> voClass;
    private Class<E> entityClass;

    public SimpleVOController() {
        Type[] type = ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments();
        if (type.length == 2) {
            //vo same as entity
            voClass = (Class<V>) type[1];
            entityClass = (Class<E>) type[1];
        } else {
            voClass = (Class<V>) type[1];
            entityClass = (Class<E>) type[2];
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(SimpleVOController.class);
}
