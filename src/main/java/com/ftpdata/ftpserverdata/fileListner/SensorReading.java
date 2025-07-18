// File: SensorData.java
package com.ftpdata.ftpserverdata.fileListner;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gatewayId;
    private String sensorId;
    private LocalDateTime timestamp;
    private double irradiance1;
    private double irradiance2;
    private double tempCompenIrrad1;
    private double tempCompenIrrad2;
    private double intTemp1;
    private double intTemp2;
    private double insSoilRatio;
    private double dAvgSoilRatio;
    private double insSoilLvl;
    private double dAvgSoilLvl;
    private double insSoilLvlPercent;
    private double dAvgSoilLvlPercent;
    private double soilRate;
    private String tankStatus;

    public SensorReading() {
    }

    public SensorReading(Long id, String gatewayId, String sensorId, LocalDateTime timestamp, double irradiance1, double irradiance2, double tempCompenIrrad1, double tempCompenIrrad2, double intTemp1, double intTemp2, double insSoilRatio, double dAvgSoilRatio, double insSoilLvl, double dAvgSoilLvl, double insSoilLvlPercent, double dAvgSoilLvlPercent, double soilRate, String tankStatus) {
        this.id = id;
        this.gatewayId = gatewayId;
        this.sensorId = sensorId;
        this.timestamp = timestamp;
        this.irradiance1 = irradiance1;
        this.irradiance2 = irradiance2;
        this.tempCompenIrrad1 = tempCompenIrrad1;
        this.tempCompenIrrad2 = tempCompenIrrad2;
        this.intTemp1 = intTemp1;
        this.intTemp2 = intTemp2;
        this.insSoilRatio = insSoilRatio;
        this.dAvgSoilRatio = dAvgSoilRatio;
        this.insSoilLvl = insSoilLvl;
        this.dAvgSoilLvl = dAvgSoilLvl;
        this.insSoilLvlPercent = insSoilLvlPercent;
        this.dAvgSoilLvlPercent = dAvgSoilLvlPercent;
        this.soilRate = soilRate;
        this.tankStatus = tankStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getIrradiance1() {
        return irradiance1;
    }

    public void setIrradiance1(double irradiance1) {
        this.irradiance1 = irradiance1;
    }

    public double getIrradiance2() {
        return irradiance2;
    }

    public void setIrradiance2(double irradiance2) {
        this.irradiance2 = irradiance2;
    }

    public double getTempCompenIrrad1() {
        return tempCompenIrrad1;
    }

    public void setTempCompenIrrad1(double tempCompenIrrad1) {
        this.tempCompenIrrad1 = tempCompenIrrad1;
    }

    public double getTempCompenIrrad2() {
        return tempCompenIrrad2;
    }

    public void setTempCompenIrrad2(double tempCompenIrrad2) {
        this.tempCompenIrrad2 = tempCompenIrrad2;
    }

    public double getIntTemp1() {
        return intTemp1;
    }

    public void setIntTemp1(double intTemp1) {
        this.intTemp1 = intTemp1;
    }

    public double getIntTemp2() {
        return intTemp2;
    }

    public void setIntTemp2(double intTemp2) {
        this.intTemp2 = intTemp2;
    }

    public double getInsSoilRatio() {
        return insSoilRatio;
    }

    public void setInsSoilRatio(double insSoilRatio) {
        this.insSoilRatio = insSoilRatio;
    }

    public double getdAvgSoilRatio() {
        return dAvgSoilRatio;
    }

    public void setdAvgSoilRatio(double dAvgSoilRatio) {
        this.dAvgSoilRatio = dAvgSoilRatio;
    }

    public double getInsSoilLvl() {
        return insSoilLvl;
    }

    public void setInsSoilLvl(double insSoilLvl) {
        this.insSoilLvl = insSoilLvl;
    }

    public double getdAvgSoilLvl() {
        return dAvgSoilLvl;
    }

    public void setdAvgSoilLvl(double dAvgSoilLvl) {
        this.dAvgSoilLvl = dAvgSoilLvl;
    }

    public double getInsSoilLvlPercent() {
        return insSoilLvlPercent;
    }

    public void setInsSoilLvlPercent(double insSoilLvlPercent) {
        this.insSoilLvlPercent = insSoilLvlPercent;
    }

    public double getdAvgSoilLvlPercent() {
        return dAvgSoilLvlPercent;
    }

    public void setdAvgSoilLvlPercent(double dAvgSoilLvlPercent) {
        this.dAvgSoilLvlPercent = dAvgSoilLvlPercent;
    }

    public double getSoilRate() {
        return soilRate;
    }

    public void setSoilRate(double soilRate) {
        this.soilRate = soilRate;
    }

    public String getTankStatus() {
        return tankStatus;
    }

    public void setTankStatus(String tankStatus) {
        this.tankStatus = tankStatus;
    }
}
