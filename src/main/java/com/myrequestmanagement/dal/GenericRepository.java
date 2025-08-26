package com.myrequestmanagement.dal;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {
    
    /**
     * Save entity
     */
    T save(T entity);
    
    /**
     * Update entity
     */
    T update(T entity);
    
    /**
     * Delete entity
     */
    void delete(T entity);
    
    /**
     * Delete by ID
     */
    void deleteById(ID id);
    
    /**
     * Find by ID
     */
    Optional<T> findById(ID id);
    
    /**
     * Find all entities
     */
    List<T> findAll();
    
    /**
     * Find all with pagination
     */
    List<T> findAll(int offset, int limit);
    
    /**
     * Count all entities
     */
    long count();
    
    /**
     * Check if entity exists by ID
     */
    boolean existsById(ID id);
}
