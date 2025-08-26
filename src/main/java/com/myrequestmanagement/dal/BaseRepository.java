package com.myrequestmanagement.dal;

import java.util.function.Consumer;
import java.util.function.Function;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class BaseRepository {
    
    protected EntityManager getEntityManager() {
        return DatabaseContext.getEntityManager();
    }
    
    /**
     * Execute operation within a transaction
     */
    protected void executeInTransaction(Consumer<EntityManager> operation) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            operation.accept(em);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Transaction failed", e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Execute operation within a transaction and return result
     */
    protected <T> T executeInTransactionWithResult(Function<EntityManager, T> operation) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            T result = operation.apply(em);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Transaction failed", e);
        } finally {
            em.close();
        }
    }
    
    /**
     * Execute read-only operation
     */
    protected <T> T executeReadOnly(Function<EntityManager, T> operation) {
        EntityManager em = getEntityManager();
        try {
            return operation.apply(em);
        } finally {
            em.close();
        }
    }
}
