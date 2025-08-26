package com.myrequestmanagement.service;

import java.util.List;
import java.util.Optional;

import com.myrequestmanagement.dal.RequestRepository;
import com.myrequestmanagement.entity.Request;
import com.myrequestmanagement.entity.RequestStatus;

public class RequestService {
    
    private final RequestRepository requestRepository;
    
    public RequestService() {
        this.requestRepository = new RequestRepository();
    }
    
    /**
     * Create a new request
     */
    public Request createRequest(String title, String description, String requesterName, String requesterEmail) {
        Request request = new Request(title, description, requesterName, requesterEmail);
        return requestRepository.save(request);
    }
    
    /**
     * Get request by ID
     */
    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }
    
    /**
     * Get all requests
     */
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }
    
    /**
     * Get requests by status
     */
    public List<Request> getRequestsByStatus(RequestStatus status) {
        return requestRepository.findByStatus(status);
    }
    
    /**
     * Get requests by requester email
     */
    public List<Request> getRequestsByRequesterEmail(String email) {
        return requestRepository.findByRequesterEmail(email);
    }
    
    /**
     * Search requests by title
     */
    public List<Request> searchRequestsByTitle(String keyword) {
        return requestRepository.searchByTitle(keyword);
    }
    
    /**
     * Update request status
     */
    public void updateRequestStatus(Long requestId, RequestStatus newStatus) {
        requestRepository.updateStatus(requestId, newStatus);
    }
    
    /**
     * Update request
     */
    public Request updateRequest(Request request) {
        return requestRepository.update(request);
    }
    
    /**
     * Delete request
     */
    public void deleteRequest(Long requestId) {
        requestRepository.deleteById(requestId);
    }
    
    /**
     * Get request statistics
     */
    public RequestStatistics getStatistics() {
        RequestStatistics stats = new RequestStatistics();
        stats.setTotalRequests(requestRepository.count());
        stats.setPendingRequests(requestRepository.countByStatus(RequestStatus.PENDING));
        stats.setInProgressRequests(requestRepository.countByStatus(RequestStatus.IN_PROGRESS));
        stats.setCompletedRequests(requestRepository.countByStatus(RequestStatus.COMPLETED));
        stats.setRejectedRequests(requestRepository.countByStatus(RequestStatus.REJECTED));
        return stats;
    }
    
    /**
     * Get recent requests
     */
    public List<Request> getRecentRequests(int limit) {
        return requestRepository.findRecentRequests(limit);
    }
    
    // Inner class for statistics
    public static class RequestStatistics {
        private long totalRequests;
        private long pendingRequests;
        private long inProgressRequests;
        private long completedRequests;
        private long rejectedRequests;
        
        // Getters and setters
        public long getTotalRequests() { return totalRequests; }
        public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
        
        public long getPendingRequests() { return pendingRequests; }
        public void setPendingRequests(long pendingRequests) { this.pendingRequests = pendingRequests; }
        
        public long getInProgressRequests() { return inProgressRequests; }
        public void setInProgressRequests(long inProgressRequests) { this.inProgressRequests = inProgressRequests; }
        
        public long getCompletedRequests() { return completedRequests; }
        public void setCompletedRequests(long completedRequests) { this.completedRequests = completedRequests; }
        
        public long getRejectedRequests() { return rejectedRequests; }
        public void setRejectedRequests(long rejectedRequests) { this.rejectedRequests = rejectedRequests; }
    }
}
