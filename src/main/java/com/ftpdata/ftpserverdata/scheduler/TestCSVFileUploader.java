//package com.ftpdata.ftpserverdata.scheduler;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//@Component
//public class TestCSVFileUploader {
//
//    private static final Logger logger = LoggerFactory.getLogger(TestCSVFileUploader.class);
//
//   // private static final String TARGET_DIR = "/home/ftpuser/ftpfiles";
//   private static final String TARGET_DIR = "C:/Users/DELL-PC/Desktop/ftpuser/ftpfiles";
//
//    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");
//
//    private static final String HEADER = "Sr.No,Gateway-ID,Sensor-ID,Timestamp,Irrad 1,Irrad 2,Temp Compen Irrad 1,Temp Compen Irrad 2,Int temp1,Int temp2,Ins soil ratio,D avg soil ratio,Ins soil lvl,D avg soil lvl,Ins soil lvl %,D avg soil lvl %,soil rate,Tank status";
//
//    @Scheduled(fixedRate = 15000) // every 15 seconds
//    public void uploadTestFiles() {
//        logger.info("Starting upload of test CSV files...");
//
//        for (int i = 1; i <= 3; i++) { // create 3 files
//            String fileName = "test_data_" + System.currentTimeMillis() + "_" + i + ".csv";
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(TARGET_DIR, fileName).toFile()))) {
//                writer.write(HEADER);
//                writer.newLine();
//                for (int j = 1; j <= 5; j++) {
//                    String row = generateFixedRow(j);
//                    writer.write(row);
//                    writer.newLine();
//                }
//                logger.info("✅ Uploaded test file: {}", fileName);
//            } catch (IOException e) {
//                logger.error("❌ Failed to write file: {} -> {}", fileName, e.getMessage(), e);
//            }
//        }
//
//        logger.info("Finished uploading test CSV files.");
//    }
//
//    private String generateFixedRow(int srNo) {
//        StringBuilder row = new StringBuilder();
//        row.append(srNo).append(",")          // Sr.No
//                .append("1").append(",")           // Gateway-ID
//                .append("2").append(",")           // Sensor-ID
//                .append(LocalDateTime.now().format(formatter)).append(","); // Timestamp
//
//        for (int i = 0; i < 14; i++) {
//            row.append("12").append(",");
//        }
//
//        row.append("12"); // Tank status
//        return row.toString();
//    }
//}


package com.ftpdata.ftpserverdata.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class TestCSVFileUploader {

    private static final Logger logger = LoggerFactory.getLogger(TestCSVFileUploader.class);

    @Value("${app.target-dir}")
    private String TARGET_DIR;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");

    private static final String HEADER = "Sr.No,Gateway-ID,Sensor-ID,Timestamp,Irrad 1,Irrad 2,Temp Compen Irrad 1,Temp Compen Irrad 2,Int temp1,Int temp2,Ins soil ratio,D avg soil ratio,Ins soil lvl,D avg soil lvl,Ins soil lvl %,D avg soil lvl %,soil rate,Tank status";
    private final Random random = new Random();

   // @Scheduled(fixedRate = 6000000) //
    public void uploadTestFiles(Integer noOfFile,Integer noOfRow) {
        logger.info("Starting upload of test CSV files...");

        for (int i = 1; i <=noOfFile; i++) {
            String fileName = "test_data_" + System.currentTimeMillis() + "_" + i + ".csv";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(TARGET_DIR, fileName).toFile()))) {
                writer.write(HEADER);
                writer.newLine();
                for (int j = 1; j <= noOfRow; j++) {
                    String row = generateRandomRow(j);
                    writer.write(row);
                    writer.newLine();
                }
                logger.info("✅ Uploaded test file: {}", fileName);
            } catch (IOException e) {
                logger.error("❌ Failed to write file: {} -> {}", fileName, e.getMessage(), e);
            }
        }

        logger.info("Finished uploading test CSV files.");
    }

    private String generateRandomRow(int srNo) {
        return String.format(
                "%d,%d,%d,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%s",
                srNo, // Sr.No
                random.nextInt(5) + 1,  // Gateway-ID (1-5)
                random.nextInt(10) + 1, // Sensor-ID (1-10)
                LocalDateTime.now().format(formatter), // Timestamp
                randomDouble(500, 1000), // Irrad 1
                randomDouble(500, 1000), // Irrad 2
                randomDouble(400, 950),  // Temp Compen Irrad 1
                randomDouble(400, 950),  // Temp Compen Irrad 2
                randomDouble(20, 40),    // Int temp1
                randomDouble(20, 40),    // Int temp2
                randomDouble(0.1, 1.0),  // Ins soil ratio
                randomDouble(0.1, 1.0),  // D avg soil ratio
                randomDouble(10, 100),   // Ins soil lvl
                randomDouble(10, 100),   // D avg soil lvl
                randomDouble(20, 100),   // Ins soil lvl %
                randomDouble(20, 100),   // D avg soil lvl %
                randomDouble(1, 10),     // soil rate
                random.nextBoolean() ? "FULL" : "EMPTY" // Tank status
        );
    }

    private double randomDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }
}
