package com.ftpdata.ftpserverdata.controller;

import com.ftpdata.ftpserverdata.scheduler.TestCSVFileUploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*") // temporary test
@RequestMapping("/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private TestCSVFileUploader testCSVFileUploader;

    @PostMapping("/test")
    public String uploadFileTest(@RequestParam Integer noOfFile, @RequestParam Integer noOfRow) {
        logger.info("Received request to upload test file with noOfFile={} and noOfRow={}", noOfFile, noOfRow);
        try {
            testCSVFileUploader.uploadTestFiles(noOfFile, noOfRow);
            logger.info("Test file uploaded successfully.");
            return "Test file uploaded";
        } catch (Exception e) {
            logger.error("Error occurred while uploading test file", e);
            return "Error occurred while uploading test file: " + e.getMessage();
        }
    }
}
