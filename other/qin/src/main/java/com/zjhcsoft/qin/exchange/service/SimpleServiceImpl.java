package com.zjhcsoft.qin.exchange.service;

import com.zjhcsoft.qin.exchange.repository.BaseRepository;
import com.zjhcsoft.qin.exchange.dto.MessageTracker;
import com.zjhcsoft.qin.exchange.dto.PageDTO;
import com.zjhcsoft.qin.exchange.entity.PKEntity;
import com.zjhcsoft.qin.exchange.utils.LangUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;
import java.util.List;

public abstract class SimpleServiceImpl<T extends BaseRepository<E>, E extends PKEntity> implements SimpleService<T, E> {

    @Override
    public boolean preGet(String pk, MessageTracker messageTracker) throws Exception {
        return true;
    }

    @Override
    public E postGet(E entity, MessageTracker messageTracker) throws Exception {
        return entity;
    }

    @Override
    public boolean preSave(E entity, MessageTracker messageTracker) throws Exception {
        return true;
    }

    @Override
    public E postSave(E entity, MessageTracker messageTracker) throws Exception {
        return entity;
    }

    @Override
    public boolean preUpdate(String pk, E entity, MessageTracker messageTracker) throws Exception {
        return true;
    }

    @Override
    public E postUpdate(String pk, E entity, MessageTracker messageTracker) throws Exception {
        return entity;
    }

    @Override
    public boolean preDelete(String pk, MessageTracker messageTracker) throws Exception {
        return true;
    }

    @Override
    public String postDelete(String pk, MessageTracker messageTracker) throws Exception {
        return pk;
    }

    @Override
    public List<E> findAll() throws Exception {
        MessageTracker messageTracker = new MessageTracker();
        return findAll(messageTracker);
    }

    @Override
    public PageDTO<E> findAll(int pageNumber, int pageSize) throws Exception {
        MessageTracker messageTracker = new MessageTracker();
        return findAll(pageNumber, pageSize, messageTracker);
    }

    @Override
    public List<E> findAll(MessageTracker messageTracker) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("FindAll.");
        }
        return LangUtils.toList(baseRepository.findAll());
    }

    @Override
    public PageDTO<E> findAll(int pageNumber, int pageSize, MessageTracker messageTracker) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("FindAll.");
        }
        Page page = baseRepository.findAll(new PageRequest(pageNumber - 1, pageSize));
        PageDTO<E> pageDTO = new PageDTO<E>();
        if (null != page || 0 != page.getTotalElements()) {
            pageDTO.setObjects(page.getContent());
            pageDTO.setPageNumber(pageNumber);
            pageDTO.setPageSize(pageSize);
            pageDTO.setPageTotal(page.getTotalPages());
            pageDTO.setRecordTotal(page.getTotalElements());
        }
        return pageDTO;
    }

    @Override
    public E get(String pk) throws Exception {
        MessageTracker messageTracker = new MessageTracker();
        return get(pk, messageTracker);
    }

    @Override
    public E get(String pk, MessageTracker messageTracker) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Get:{}.", pk);
        }
        if (preGet(pk, messageTracker)) {
            return postGet(baseRepository.findOne(pk), messageTracker);
        }
        return null;
    }

    @Override
    public E save(E entity) throws Exception {
        MessageTracker messageTracker = new MessageTracker();
        return save(entity, messageTracker);
    }

    @Override
    @Transactional
    public E save(E entity, MessageTracker messageTracker) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Save.");
        }
        if (preSave(entity, messageTracker)) {
            ServiceHelper.assembleEntity(entity);
            return postSave(baseRepository.save(entity), messageTracker);
        }
        return null;
    }

    @Override
    public E update(String pk, E entity) throws Exception {
        MessageTracker messageTracker = new MessageTracker();
        return update(pk, entity, messageTracker);
    }

    @Override
    @Transactional
    public E update(String pk, E entity, MessageTracker messageTracker) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Update:{}.", pk);
        }
        if (preUpdate(pk, entity, messageTracker)) {
            ServiceHelper.assembleEntity(get(pk), entity);
            return postUpdate(pk, baseRepository.save(entity), messageTracker);
        }
        return null;
    }

    @Override
    public String delete(String pk) throws Exception {
        MessageTracker messageTracker = new MessageTracker();
        return delete(pk, messageTracker);
    }

    @Override
    @Transactional
    public String delete(String pk, MessageTracker messageTracker) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Delete:{}.", pk);
        }
        if (preDelete(pk, messageTracker)) {
            if (-1 != pk.indexOf(",")) {
                String[] pks = pk.split(",");
                for (String p : pks) {
                    baseRepository.delete(p);
                }
            } else {
                baseRepository.delete(pk);
            }
            return postDelete(pk, messageTracker);
        }
        return null;
    }

    @Autowired
    public T baseRepository;

    private static final Logger logger = LoggerFactory.getLogger(SimpleServiceImpl.class);


}
