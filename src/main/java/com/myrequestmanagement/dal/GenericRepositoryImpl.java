package com.myrequestmanagement.dal;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;

public abstract class GenericRepositoryImpl<T, ID> extends BaseRepository implements GenericRepository<T, ID> {
    
    private final Class<T> entityClass;
    
    @SuppressWarnings("unchecked")
    public GenericRepositoryImpl() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    @Override
    public T save(T entity) {
        return executeInTransactionWithResult(em -> {
            em.persist(entity);
            return entity;
        });
    }
    
    @Override
    public T update(T entity) {
        return executeInTransactionWithResult(em -> em.merge(entity));
    }
    
    @Override
    public void delete(T entity) {
        executeInTransaction(em -> {
            T managedEntity = em.contains(entity) ? entity : em.merge(entity);
            em.remove(managedEntity);
        });
    }
    
    @Override
    public void deleteById(ID id) {
        executeInTransaction(em -> {
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
        });
    }
    
    @Override
    public Optional<T> findById(ID id) {
        return executeReadOnly(em -> Optional.ofNullable(em.find(entityClass, id)));
    }
    
    @Override
    public List<T> findAll() {
        return executeReadOnly(em -> {
            TypedQuery<T> query = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
            return query.getResultList();
        });
    }
    
    @Override
    public List<T> findAll(int offset, int limit) {
        return executeReadOnly(em -> {
            TypedQuery<T> query = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        });
    }
    
    @Override
    public long count() {
        return executeReadOnly(em -> {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class);
            return query.getSingleResult();
        });
    }
    
    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }
}
