package com.ftpdata.ftpserverdata.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "failed_file_logs")
public class FailedFileLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String failureReason;
    private int retryCount;

    private boolean resolved;

    private LocalDateTime failureTime;

    @Column(name = "resolved_time")
    private LocalDateTime resolvedTime;
    @Column(name = "last_retry_time")
    private LocalDateTime lastRetryTime;


    public LocalDateTime getResolvedTime() {
        return resolvedTime;
    }

    public void setResolvedTime(LocalDateTime resolvedTime) {
        this.resolvedTime = resolvedTime;
    }

    public LocalDateTime getLastRetryTime() {
        return lastRetryTime;
    }

    public void setLastRetryTime(LocalDateTime lastRetryTime) {
        this.lastRetryTime = lastRetryTime;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public LocalDateTime getFailureTime() {
        return failureTime;
    }

    public void setFailureTime(LocalDateTime failureTime) {
        this.failureTime = failureTime;
    }
}
