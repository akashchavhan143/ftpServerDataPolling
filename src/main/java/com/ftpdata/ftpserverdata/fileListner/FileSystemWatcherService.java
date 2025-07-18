package com.ftpdata.ftpserverdata.fileListner;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class FileSystemWatcherService {
    private static final Logger logger = LoggerFactory.getLogger(FileSystemWatcherService.class);


    @Value("${app.watch-dir}")
    private String WATCH_DIR;
    @Value("${app.failed-dir:/tmp/failed}")
    private String FAILED_DIR;

    @Value("${app.success-dir:/tmp/success}")
    private String SUCCESS_DIR;

    @Autowired
    private CSVProcessor processor;

    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    @PostConstruct
    public void init() {
        Path dir = Paths.get(WATCH_DIR);
        if (!Files.exists(dir)) {
            logger.error("❌ Watch directory does not exist: {}", WATCH_DIR);
            return;
        }
        Thread thread = new Thread(this::watchDirectory);
        thread.setDaemon(true);
        thread.start();
        logger.info("📂 File watcher initialized for directory: {}", WATCH_DIR);
    }

    private void watchDirectory() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(WATCH_DIR);
            dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            logger.info("👀 Watching directory: {}", WATCH_DIR);

            while (true) {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        Path fileName = (Path) event.context();
                        if (fileName.toString().endsWith(".csv")) {
                            Path fullPath = dir.resolve(fileName);
                            logger.info("🆕 New CSV file detected: {}", fullPath.getFileName());

                            executor.submit(() -> {
                                try {
                                    boolean success = processFile(fullPath);
                                    if (success) {
                                        moveToSuccess(fullPath);
                                    } else {
                                        moveToFailed(fullPath);
                                    }
                                } catch (Exception e) {
                                    logger.error("Processing failed for file: {}", fullPath, e);
                                    moveToFailed(fullPath);
                                }
                            });
                        } else {
                            logger.debug("Ignored non-CSV file: {}", fileName);
                        }
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    logger.warn("WatchKey is no longer valid. Exiting watcher loop.");
                    break;
                }
            }

        } catch (Exception e) {
            logger.error("❌ Exception in watchDirectory: {}", e.getMessage(), e);
        }
    }

  /*  private Boolean processFile(Path filePath) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("📥 Processing file: {}", filePath.getFileName());
            Thread.sleep(1000); // slight delay to ensure file is completely written
            processor.parse(Files.newInputStream(filePath));
            //Files.deleteIfExists(filePath);
            //logger.info("🗑️ Deleted file after processing: {}", filePath.getFileName());
            return true;
        } catch (IOException e) {
            logger.error("❌ Error processing file {}: {}", filePath.getFileName(), e.getMessage(), e);
            return false;
        }catch (Exception e) {
            logger.error("❌ Error processing file {}: {}", filePath.getFileName(), e.getMessage(), e);
            return false;
        }finally {
            long endTime = System.currentTimeMillis();
            long durationMs = endTime - startTime;

            long seconds = durationMs / 1000;
            long minutes = seconds / 60;
            long remainingSeconds = seconds % 60;

            logger.info("📊 Total Time Taken for file {}: {} ms ({} min {} sec)",
                    filePath.getFileName(), durationMs, minutes, remainingSeconds);
        }
    }
*/

    private Boolean processFile(Path filePath) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("📥 Processing file: {}", filePath.getFileName());
            Thread.sleep(1000); // slight delay to ensure file is completely written

            boolean parseSuccess = processor.parse(Files.newInputStream(filePath));

            return parseSuccess;
        } catch (IOException | RuntimeException | InterruptedException e) {
            logger.error("❌ Error processing file {}: {}", filePath.getFileName(), e.getMessage(), e);
            return false;
        } finally {
            long endTime = System.currentTimeMillis();
            long durationMs = endTime - startTime;
            long seconds = durationMs / 1000;
            long minutes = seconds / 60;
            long remainingSeconds = seconds % 60;

            logger.info("📊 Total Time Taken for file {}: {} ms ({} min {} sec)",
                    filePath.getFileName(), durationMs, minutes, remainingSeconds);
        }
    }

    private void moveToFailed(Path sourcePath) {
        try {
            Path targetDir = Paths.get(FAILED_DIR);
            Files.createDirectories(targetDir);
            String timestampedName = System.currentTimeMillis() + "_" + sourcePath.getFileName();
            Path targetPath = targetDir.resolve(timestampedName);

            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            logger.warn("Moved file to FAILED: {}", targetPath);
        } catch (IOException e) {
            logger.error("Failed to move file to FAILED directory: {}", e.getMessage());
        }
    }

    private void moveToSuccess(Path sourcePath) {
        try {
            Path targetDir = Paths.get(SUCCESS_DIR);
            Files.createDirectories(targetDir);
            // Use timestamp to avoid filename conflicts
            String timestampedName = System.currentTimeMillis() + "_" + sourcePath.getFileName();
            Path targetPath = targetDir.resolve(timestampedName);

            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Moved file to SUCCESS: {}", targetPath);
        } catch (IOException e) {
            logger.error("Failed to move file to SUCCESS directory: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void cleanup() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}



//
//package com.ftpdata.ftpserverdata.fileListner;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.nio.file.*;
//
//@Component
//public class FileSystemWatcherService {
//    private static final Logger logger = LoggerFactory.getLogger(FileSystemWatcherService.class);
//
//    @Value("${app.watch-dir}")
//    private String WATCH_DIR;
//
//    @Autowired
//    private CSVProcessor processor;
//
//    private int totalFilesProcessed = 0;
//    private long totalProcessingTime = 0;
//
//    @PostConstruct
//    public void init() {
//        Thread thread = new Thread(this::watchDirectory);
//        thread.setDaemon(true);
//        thread.start();
//        logger.info("✅ Started FileSystemWatcherService thread to monitor directory: {}", WATCH_DIR);
//    }
//
//    private void watchDirectory() {
//        try {
//            WatchService watchService = FileSystems.getDefault().newWatchService();
//            Path dir = Paths.get(WATCH_DIR);
//            dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
//
//            logger.info("👀 Watching directory: {}", WATCH_DIR);
//
//            while (true) {
//                WatchKey key = watchService.take();
//
//                for (WatchEvent<?> event : key.pollEvents()) {
//                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
//                        Path fileName = (Path) event.context();
//                        if (fileName.toString().endsWith(".csv")) {
//                            Path fullPath = dir.resolve(fileName);
//                            logger.info("🆕 New CSV file detected: {}", fullPath.getFileName());
//
//                            // Process file
//                            processFile(fullPath);
//                        } else {
//                            logger.debug("Ignored non-CSV file: {}", fileName);
//                        }
//                    }
//                }
//
//                boolean valid = key.reset();
//                if (!valid) {
//                    logger.warn("WatchKey is no longer valid. Exiting watcher loop.");
//                    break;
//                }
//            }
//
//        } catch (Exception e) {
//            logger.error("❌ Exception in watchDirectory: {}", e.getMessage(), e);
//        }
//    }
//
//    private void processFile(Path filePath) {
//        try {
//            logger.info("📥 Processing file: {}", filePath.getFileName());
//            Thread.sleep(1000); // wait for file to be fully written
//
//            long startTime = System.currentTimeMillis();
//
//            processor.parse(Files.newInputStream(filePath));
//
//            long endTime = System.currentTimeMillis();
//            long timeTaken = endTime - startTime;
//
//            Files.deleteIfExists(filePath);
//
//            totalFilesProcessed++;
//            totalProcessingTime += timeTaken;
//
//            logger.info("✅ Processed and deleted file: {} in {} ms", filePath.getFileName(), timeTaken);
//            logger.info("📊 Total Files Processed: {}, Total Time Taken: {} ms",
//                    totalFilesProcessed, totalProcessingTime);
//
//        } catch (Exception e) {
//            logger.error("❌ Error processing file {}: {}", filePath.getFileName(), e.getMessage(), e);
//        }
//    }
//}
