package com.zjhcsoft.qin.exchange.controller;


import com.zjhcsoft.qin.exchange.dto.MessageTracker;
import com.zjhcsoft.qin.exchange.dto.PageDTO;
import com.zjhcsoft.qin.exchange.dto.ResponseVO;
import com.zjhcsoft.qin.exchange.entity.PKEntity;
import com.zjhcsoft.qin.exchange.service.ServiceHelper;
import com.zjhcsoft.qin.exchange.service.SimpleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>CRUD 控制器 辅助类</h1>
 */
public class CRUDControllerHelper {

    /**
     * 查找
     *
     * @param service
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T extends SimpleService, E extends PKEntity> ResponseVO findAll(T service) {
        MessageTracker messageTracker = new MessageTracker();
        List<E> result;
        try {
            result = service.findAll(messageTracker);
        } catch (Exception e) {
            logger.error("Find all error:{}", e);
            return ControllerHelper.unknownError();
        }
        if (null != result) {
            return ControllerHelper.success(result);
        } else {
            return ControllerHelper.badRequest(messageTracker);
        }
    }

    /**
     * 查找，带分页
     *
     * @param httpServletRequest
     * @param service
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T extends SimpleService, E extends PKEntity> ResponseVO findAll(HttpServletRequest httpServletRequest, T service) {
        MessageTracker messageTracker = new MessageTracker();
        int[] page = ControllerHelper.getPageParameters(httpServletRequest);
        Object result;
        try {
            if (null != page) {
                result = service.findAll(page[0], page[1], messageTracker);
            } else {
                result = service.findAll(messageTracker);
            }
        } catch (Exception e) {
            logger.error("Find all error:{}", e);
            return ControllerHelper.unknownError();
        }
        if (null != result) {
            return ControllerHelper.success(result);
        } else {
            return ControllerHelper.badRequest(messageTracker);
        }
    }

    /**
     * 查找，带VO转换
     *
     * @param controller
     * @param service
     * @param voClass
     * @param entityClass
     * @param <C>
     * @param <T>
     * @param <V>
     * @param <E>
     * @return
     */
    public static <C extends VOAssembler, T extends SimpleService, V extends Object, E extends PKEntity> ResponseVO findAll(C controller, T service, Class<V> voClass, Class<E> entityClass) {
        MessageTracker messageTracker = new MessageTracker();
        List<E> result;
        try {
            result = service.findAll(messageTracker);
        } catch (Exception e) {
            logger.error("Find all error:{}", e);
            return ControllerHelper.unknownError();
        }
        if (null != result) {
            try {
                return ControllerHelper.success(entitiesToVOs(result, controller, voClass, entityClass));
            } catch (Exception e) {
                logger.error("Find all error:{}", e);
                return ControllerHelper.unknownError("VO Convert Error.");
            }
        } else {
            return ControllerHelper.badRequest(messageTracker);
        }
    }

    /**
     * 查找，带VO转换及分页
     *
     * @param httpServletRequest
     * @param controller
     * @param service
     * @param voClass
     * @param entityClass
     * @param <C>
     * @param <T>
     * @param <V>
     * @param <E>
     * @return
     */
    public static <C extends VOAssembler, T extends SimpleService, V extends Object, E extends PKEntity> ResponseVO findAll(HttpServletRequest httpServletRequest, C controller, T service, Class<V> voClass, Class<E> entityClass) {
        MessageTracker messageTracker = new MessageTracker();
        int[] page = ControllerHelper.getPageParameters(httpServletRequest);
        try {
            if (null != page) {
                PageDTO result = service.findAll(page[0], page[1], messageTracker);
                if (null != result) {
                    result.setObjects(entitiesToVOs(result.getObjects(), controller, voClass, entityClass));
                    return ControllerHelper.success(result);
                } else {
                    return ControllerHelper.badRequest(messageTracker);
                }
            } else {
                List<E> result = service.findAll(messageTracker);
                if (null != result) {
                    return ControllerHelper.success(entitiesToVOs(result, controller, voClass, entityClass));
                } else {
                    return ControllerHelper.badRequest(messageTracker);
                }
            }
        } catch (Exception e) {
            logger.error("Find all error:{}", e);
            return ControllerHelper.unknownError();
        }
    }

    /**
     * 获取单个对象
     *
     * @param pk
     * @param service
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T extends SimpleService, E extends PKEntity> ResponseVO get(String pk, T service) {
        MessageTracker messageTracker = new MessageTracker();
        E result;
        try {
            result = (E) service.get(pk, messageTracker);
        } catch (Exception e) {
            logger.error("Get error:{}", e);
            return ControllerHelper.unknownError();
        }
        if (null != result) {
            return ControllerHelper.success(result);
        } else {
            return ControllerHelper.badRequest(messageTracker);
        }
    }

    /**
     * 获取单个对象，带VO转换
     *
     * @param pk
     * @param controller
     * @param service
     * @param voClass
     * @param entityClass
     * @param <C>
     * @param <T>
     * @param <V>
     * @param <E>
     * @return
     */
    public static <C extends VOAssembler, T extends SimpleService, V extends Object, E extends PKEntity> ResponseVO get(String pk, C controller, T service, Class<V> voClass, Class<E> entityClass) {
        MessageTracker messageTracker = new MessageTracker();
        E result;
        try {
            result = (E) service.get(pk, messageTracker);
        } catch (Exception e) {
            logger.error("Get error:{}", e);
            return ControllerHelper.unknownError();
        }
        if (null != result) {
            try {
                return ControllerHelper.success(entityToVO(result, controller, voClass, entityClass));
            } catch (Exception e) {
                logger.error("Get error:{}", e);
                return ControllerHelper.unknownError("VO Convert Error.");
            }
        } else {
            return ControllerHelper.badRequest(messageTracker);
        }
    }

    /**
     * 保存
     *
     * @param entity
     * @param service
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T extends SimpleService, E extends PKEntity> ResponseVO save(E entity, T service) {
        MessageTracker messageTracker = new MessageTracker();
        E result;
        try {
            ServiceHelper.assembleEntity(entity);
            result = (E) service.save(entity, messageTracker);
        } catch (Exception e) {
            logger.error("Save error:{}", e);
            return ControllerHelper.unknownError();
        }
        if (null != result) {
            return ControllerHelper.success(result);
        } else {
            return ControllerHelper.badRequest(messageTracker);
        }
    }

    /**
     * 保存，带VO转换
     *
     * @param vo
     * @param controller
     * @param service
     * @param voClass
     * @param entityClass
     * @param <C>
     * @param <T>
     * @param <V>
     * @param <E>
     * @return
     */
    public static <C extends VOAssembler, T extends SimpleService, V extends Object, E extends PKEntity> ResponseVO save(V vo, C controller, T service, Class<V> voClass, Class<E> entityClass) {
        MessageTracker messageTracker = new MessageTracker();
        E result;
        try {
            result = (E) service.save(voToEntity(vo, controller, voClass, entityClass), messageTracker);
        } catch (Exception e) {
            logger.error("Save error:{}", e);
            return ControllerHelper.unknownError();
        }
        if (null != result) {
            try {
                return ControllerHelper.success(entityToVO(result, controller, voClass, entityClass));
            } catch (Exception e) {
                logger.error("Save error:{}", e);
                return ControllerHelper.unknownError("VO Convert Error.");
            }
        } else {
            return ControllerHelper.badRequest(messageTracker);
        }
    }

    /**
     * 更新
     *
     * @param pk
     * @param entity
     * @param service
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T extends SimpleService, E extends PKEntity> ResponseVO update(String pk, E entity, T service) {
        MessageTracker messageTracker = new MessageTracker();
        E result;
        try {
            result = (E) service.update(pk, entity, messageTracker);
        } catch (Exception e) {
            logger.error("Update error:{}", e);
            return ControllerHelper.unknownError();
        }
        if (null != result) {
            return ControllerHelper.success(result);
        } else {
            return ControllerHelper.badRequest(messageTracker);
        }
    }

    /**
     * 更新，带VO转换
     *
     * @param pk
     * @param vo
     * @param controller
     * @param service
     * @param voClass
     * @param entityClass
     * @param <C>
     * @param <T>
     * @param <V>
     * @param <E>
     * @return
     */
    public static <C extends VOAssembler, T extends SimpleService, V extends Object, E extends PKEntity> ResponseVO update(String pk, V vo, C controller, T service, Class<V> voClass, Class<E> entityClass) {
        MessageTracker messageTracker = new MessageTracker();
        E result;
        try {
            result = (E) service.update(pk, voToEntity(vo, controller, voClass, entityClass), messageTracker);
        } catch (Exception e) {
            logger.error("Update error:{}", e);
            return ControllerHelper.unknownError();
        }
        if (null != result) {
            try {
                return ControllerHelper.success(entityToVO(result, controller, voClass, entityClass));
            } catch (Exception e) {
                logger.error("Update error:{}", e);
                return ControllerHelper.unknownError("VO Convert Error.");
            }
        } else {
            return ControllerHelper.badRequest(messageTracker);
        }
    }

    /**
     * 删除
     *
     * @param pk
     * @param service
     * @param <T>
     * @return
     */
    public static <T extends SimpleService> ResponseVO delete(String pk, T service) {
        MessageTracker messageTracker = new MessageTracker();
        String result;
        try {
            result = service.delete(pk, messageTracker);
        } catch (Exception e) {
            logger.error("Delete error:{}", e);
            return ControllerHelper.unknownError();
        }
        if (null != result) {
            return ControllerHelper.success(result);
        } else {
            return ControllerHelper.badRequest(messageTracker);
        }
    }

    /**
     * 上传
     *
     * @param files
     * @param controller
     * @param <C>
     * @return
     */
    public static <C extends UploadAssembler> ResponseVO upload(MultipartFile files,String realPath, C controller) {
        try {
            return ControllerHelper.success(ControllerHelper.upload(realPath, controller, true, files));
        } catch (Exception e) {
            logger.error("Upload error:{}", e);
            return ControllerHelper.unknownError();
        }
    }

    private static <C extends VOAssembler, V extends Object, E extends PKEntity> V entityToVO(E entity, C controller, Class<V> voClass, Class<E> entityClass) throws Exception {
        if (null == entity) {
            return null;
        }
        if (voClass.equals(entityClass)) {
            return (V) entity;
        }
        V vo = voClass.newInstance();
        BeanUtils.copyProperties(entity, vo);
        controller.entityToVO(entity, vo);
        return vo;
    }

    private static <C extends VOAssembler, V extends Object, E extends PKEntity> E voToEntity(V vo, C controller, Class<V> voClass, Class<E> entityClass) throws Exception {
        if (null == vo) {
            return null;
        }
        if (voClass.equals(entityClass)) {
            return (E) vo;
        }
        E entity = entityClass.newInstance();
        BeanUtils.copyProperties(vo, entity);
        controller.voToEntity(vo, entity);
        return entity;
    }

    private static <C extends VOAssembler, V extends Object, E extends PKEntity> List<V> entitiesToVOs(List<E> entities, C controller, Class<V> voClass, Class<E> entityClass) throws Exception {
        if (null == entities || entities.size() == 0) {
            return null;
        }
        List<V> vos = new ArrayList<>();
        for (E entity : entities) {
            vos.add(entityToVO(entity, controller, voClass, entityClass));
        }
        return vos;
    }

    private static final Logger logger = LoggerFactory.getLogger(CRUDControllerHelper.class);


}
