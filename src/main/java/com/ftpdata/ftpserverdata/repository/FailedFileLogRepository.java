package com.ftpdata.ftpserverdata.repository;

import com.ftpdata.ftpserverdata.entity.FailedFileLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FailedFileLogRepository extends JpaRepository<FailedFileLog, Long> {
    List<FailedFileLog> findByResolvedFalse();
}
