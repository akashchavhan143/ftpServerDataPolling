package com.ftpdata.ftpserverdata.scheduler;

import com.ftpdata.ftpserverdata.entity.FailedFileLog;
import com.ftpdata.ftpserverdata.fileListner.CSVProcessor;
import com.ftpdata.ftpserverdata.fileListner.FileSystemWatcherService;
import com.ftpdata.ftpserverdata.repository.FailedFileLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class FailedFileRetryScheduler {
    private static final Logger logger = LoggerFactory.getLogger(FailedFileRetryScheduler.class);

    @Value("${app.failed-dir}")
    private String FAILED_DIR;

    @Value("${app.max-retries:3}")
    private int MAX_RETRIES;

    @Autowired
    private FailedFileLogRepository failedFileLogRepository;

    @Autowired
    private CSVProcessor processor;

    @Autowired
    private FileSystemWatcherService fileSystemWatcherService;

    @Scheduled(fixedDelay = 60000) // Every 60 seconds
    public void retryFailedFiles() {
        logger.info("🔄 Starting failed files retry process...");
        List<FailedFileLog> failedFiles = failedFileLogRepository.findByResolvedFalse();

        if (failedFiles.isEmpty()) {
            logger.info("✅ No failed files to retry");
            return;
        }

        logger.info("🔍 Found {} failed files to retry", failedFiles.size());

        for (FailedFileLog fileLog : failedFiles) {
            try {
                // Add small delay between retries
                Thread.sleep(500);
                if (fileLog.getRetryCount() >= MAX_RETRIES) {
                    logger.warn("⏭️ Skipping file {} - reached max retries ({})",
                            fileLog.getFileName(), MAX_RETRIES);
                    continue;
                }

                Path filePath = Paths.get(FAILED_DIR, fileLog.getFileName());
                if (!Files.exists(filePath)) {
                    logger.error("📄 File not found: {}", filePath);
                    fileLog.setFailureReason("File not found during retry");
                    fileLog.setRetryCount(fileLog.getRetryCount() + 1);
                    failedFileLogRepository.save(fileLog);
                    continue;
                }

                logger.info("🔄 Retrying file: {} (attempt {}/{})",
                        fileLog.getFileName(), fileLog.getRetryCount() + 1, MAX_RETRIES);

                boolean success = processor.parse(Files.newInputStream(filePath));

                if (success) {
                    fileLog.setResolved(true);
                    fileLog.setResolvedTime(LocalDateTime.now());
                    failedFileLogRepository.save(fileLog);
                    logger.info("✅ Successfully processed file: {}", filePath);

                    // Optionally move to success directory
                    fileSystemWatcherService.moveToSuccess(filePath);
                } else {
                    fileLog.setRetryCount(fileLog.getRetryCount() + 1);
                    failedFileLogRepository.save(fileLog);
                    logger.warn("❌ Retry failed for file: {}", filePath);
                }
            } catch (IOException e) {
                handleRetryError(fileLog, "IO Error: " + e.getMessage(), e);
            } catch (Exception e) {
                handleRetryError(fileLog, "Unexpected Error: " + e.getMessage(), e);
            }
        }
    }


    private void handleRetryError(FailedFileLog fileLog, String errorMessage, Exception e) {
        logger.error("❌ Retry error for file {}: {}", fileLog.getFileName(), errorMessage, e);
        fileLog.setRetryCount(fileLog.getRetryCount() + 1);
        fileLog.setFailureReason(errorMessage);
        failedFileLogRepository.save(fileLog);
    }
}