package com.ftpdata.ftpserverdata.fileListner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CSVProcessor.class);

    @Autowired
    private SensorReadingRepository repository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");

  /*  public void parse(InputStream inputStream) throws IOException {
        logger.info("Starting CSV parsing...");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean isHeader = true;
            List<SensorReading> records = new ArrayList<>();
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (isHeader) {
                    logger.debug("Skipping header line");
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 18) {
                    logger.warn("Skipping invalid line #{}: not enough columns", lineNumber);
                    continue;
                }

                try {
                    SensorReading data = new SensorReading();
                    data.setGatewayId(parts[1].trim());
                    data.setSensorId(parts[2].trim());
                    data.setTimestamp(LocalDateTime.parse(parts[3].trim(), formatter));
                    data.setIrradiance1(Double.parseDouble(parts[4].trim()));
                    data.setIrradiance2(Double.parseDouble(parts[5].trim()));
                    data.setTempCompenIrrad1(Double.parseDouble(parts[6].trim()));
                    data.setTempCompenIrrad2(Double.parseDouble(parts[7].trim()));
                    data.setIntTemp1(Double.parseDouble(parts[8].trim()));
                    data.setIntTemp2(Double.parseDouble(parts[9].trim()));
                    data.setInsSoilRatio(Double.parseDouble(parts[10].trim()));
                    data.setdAvgSoilRatio(Double.parseDouble(parts[11].trim()));
                    data.setInsSoilLvl(Double.parseDouble(parts[12].trim()));
                    data.setdAvgSoilLvl(Double.parseDouble(parts[13].trim()));
                    data.setInsSoilLvlPercent(Double.parseDouble(parts[14].trim()));
                    data.setdAvgSoilLvlPercent(Double.parseDouble(parts[15].trim()));
                    data.setSoilRate(Double.parseDouble(parts[16].trim()));
                    data.setTankStatus(parts[17].trim());

                    records.add(data);
                    logger.debug("Parsed line #{} successfully", lineNumber);
                } catch (Exception ex) {
                    logger.error("Error parsing line #{}: {}", lineNumber, ex.getMessage());
                }
            }

            repository.saveAll(records);
            logger.info("CSV parsing completed. Total records saved: {}", records.size());

        } catch (IOException e) {
            logger.error("IOException while parsing CSV: {}", e.getMessage(), e);
            throw new IOException(e.getMessage());
        }
    }
*/

    public boolean parse(InputStream inputStream) throws IOException {
        logger.info("Starting CSV parsing...");
        List<SensorReading> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean isHeader = true;
            int lineNumber = 0;
            boolean hasError = false;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (isHeader) {
                    logger.debug("Skipping header line");
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 18) {
                    logger.warn("Skipping invalid line #{}: not enough columns", lineNumber);
                    hasError = true;
                    continue;
                }

                try {
                    SensorReading data = new SensorReading();
                    data.setGatewayId(parts[1].trim());
                    data.setSensorId(parts[2].trim());
                    data.setTimestamp(LocalDateTime.parse(parts[3].trim(), formatter));
                    data.setIrradiance1(Double.parseDouble(parts[4].trim()));
                    data.setIrradiance2(Double.parseDouble(parts[5].trim()));
                    data.setTempCompenIrrad1(Double.parseDouble(parts[6].trim()));
                    data.setTempCompenIrrad2(Double.parseDouble(parts[7].trim()));
                    data.setIntTemp1(Double.parseDouble(parts[8].trim()));
                    data.setIntTemp2(Double.parseDouble(parts[9].trim()));
                    data.setInsSoilRatio(Double.parseDouble(parts[10].trim()));
                    data.setdAvgSoilRatio(Double.parseDouble(parts[11].trim()));
                    data.setInsSoilLvl(Double.parseDouble(parts[12].trim()));
                    data.setdAvgSoilLvl(Double.parseDouble(parts[13].trim()));
                    data.setInsSoilLvlPercent(Double.parseDouble(parts[14].trim()));
                    data.setdAvgSoilLvlPercent(Double.parseDouble(parts[15].trim()));
                    data.setSoilRate(Double.parseDouble(parts[16].trim()));
                    data.setTankStatus(parts[17].trim());

                    records.add(data);
                    logger.debug("Parsed line #{} successfully", lineNumber);
                } catch (Exception ex) {
                    logger.error("Error parsing line #{}: {}", lineNumber, ex.getMessage());
                    hasError = true;
                }
            }

            try {
                repository.saveAll(records);
            } catch (Exception e) {
                logger.error("❌ DB save failed: {}", e.getMessage(), e);
                return false;
            }

            logger.info("CSV parsing completed. Total records saved: {}", records.size());
            return !hasError;

        } catch (IOException e) {
            logger.error("IOException while parsing CSV: {}", e.getMessage(), e);
            throw new IOException(e.getMessage());
        }
    }

}
