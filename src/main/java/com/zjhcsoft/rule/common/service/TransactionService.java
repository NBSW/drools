package com.zjhcsoft.rule.common.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-3-12  Time: 下午5:20
 */
public abstract class TransactionService {
    protected PlatformTransactionManager transactionManager;

    private static final DefaultTransactionDefinition definition = new DefaultTransactionDefinition();

    static {
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    }

    private TransactionStatus startTransaction() {
        return transactionManager.getTransaction(definition);
    }

    private void rollback(TransactionStatus status) {
        transactionManager.rollback(status);
    }

    private void commit(TransactionStatus status) {
        transactionManager.commit(status);
    }

    public void executeVoid(Object o, String mName, Object p, Class<?>... argClass) {
        TransactionStatus status = startTransaction();
        try {
            o.getClass().getMethod(mName, argClass).invoke(o, p);
            commit(status);
        } catch (Exception e) {
            rollback(status);
            e.printStackTrace();
        }
    }

    public Object executeObject(Object o, String mName, Object p, Class<?>... argClass) {
        TransactionStatus status = startTransaction();
        try {
            Object ret = o.getClass().getMethod(mName, argClass).invoke(o, p);
            commit(status);
            return ret;
        } catch (Exception e) {
            rollback(status);
            e.printStackTrace();
        }
        return null;
    }

    public abstract void setTransactionManager(PlatformTransactionManager transactionManager);
}
