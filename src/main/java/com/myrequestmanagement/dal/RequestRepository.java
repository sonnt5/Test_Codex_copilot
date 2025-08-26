package com.myrequestmanagement.dal;

import java.util.List;

import com.myrequestmanagement.entity.Request;
import com.myrequestmanagement.entity.RequestStatus;

import jakarta.persistence.TypedQuery;

public class RequestRepository extends GenericRepositoryImpl<Request, Long> {
    
    /**
     * Find requests by status
     */
    public List<Request> findByStatus(RequestStatus status) {
        return executeReadOnly(em -> {
            TypedQuery<Request> query = em.createQuery(
                "SELECT r FROM Request r WHERE r.status = :status ORDER BY r.createdAt DESC", 
                Request.class);
            query.setParameter("status", status);
            return query.getResultList();
        });
    }
    
    /**
     * Find requests by requester email
     */
    public List<Request> findByRequesterEmail(String email) {
        return executeReadOnly(em -> {
            TypedQuery<Request> query = em.createQuery(
                "SELECT r FROM Request r WHERE r.requesterEmail = :email ORDER BY r.createdAt DESC", 
                Request.class);
            query.setParameter("email", email);
            return query.getResultList();
        });
    }
    
    /**
     * Search requests by title containing keyword
     */
    public List<Request> searchByTitle(String keyword) {
        return executeReadOnly(em -> {
            TypedQuery<Request> query = em.createQuery(
                "SELECT r FROM Request r WHERE LOWER(r.title) LIKE LOWER(:keyword) ORDER BY r.createdAt DESC", 
                Request.class);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        });
    }
    
    /**
     * Find recent requests (last 30 days)
     */
    public List<Request> findRecentRequests(int limit) {
        return executeReadOnly(em -> {
            TypedQuery<Request> query = em.createQuery(
                "SELECT r FROM Request r WHERE r.createdAt >= CURRENT_DATE - 30 ORDER BY r.createdAt DESC", 
                Request.class);
            query.setMaxResults(limit);
            return query.getResultList();
        });
    }
    
    /**
     * Count requests by status
     */
    public long countByStatus(RequestStatus status) {
        return executeReadOnly(em -> {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(r) FROM Request r WHERE r.status = :status", 
                Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        });
    }
    
    /**
     * Update request status
     */
    public void updateStatus(Long requestId, RequestStatus newStatus) {
        executeInTransaction(em -> {
            Request request = em.find(Request.class, requestId);
            if (request != null) {
                request.setStatus(newStatus);
                em.merge(request);
            }
        });
    }
}
