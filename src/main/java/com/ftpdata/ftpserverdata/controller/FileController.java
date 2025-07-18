package com.ftpdata.ftpserverdata.controller;

import com.ftpdata.ftpserverdata.scheduler.TestCSVFileUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private TestCSVFileUploader testCSVFileUploader;
    @PostMapping("/test")
    public String uploadFileTest(@RequestParam Integer noOfFile,@RequestParam Integer noOfRow){
        try{
            testCSVFileUploader.uploadTestFiles(noOfFile,noOfRow);
        }
       catch (Exception e){

       }
        return "Test file uploaded " ;
    }
}
