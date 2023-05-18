package org.alex.springcourse.app.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class MeasurementDTO {
    @NotNull(message = "Degree value shouldn't be empty!")
    @Range(min = -100, max = 100, message = "Name should be between -100 to 100")
    private double value;

    @NotNull(message = "Raining value shouldn't be empty!")
    private boolean raining;

    @NotNull(message = "Sensor data shouldn't be empty!")
    private SensorDTO sensor;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
