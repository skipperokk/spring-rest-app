package org.alex.springcourse.app.controllers;

import org.alex.springcourse.app.dto.SensorDTO;
import org.alex.springcourse.app.models.Sensor;
import org.alex.springcourse.app.services.SensorService;
import org.alex.springcourse.app.util.ErrorUtil;
import org.alex.springcourse.app.util.MeasurementErrorResponse;
import org.alex.springcourse.app.util.MeasurementException;
import org.alex.springcourse.app.util.MeasurementValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/sensors")
public class SensorController {
    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final MeasurementValidator measurementValidator;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper, MeasurementValidator measurementValidator) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult) {
        Sensor convertedSensor = convertToSensor(sensorDTO);
        measurementValidator.validate(convertedSensor, bindingResult);
        if (bindingResult.hasErrors()) {
            ErrorUtil.processFieldErrors(bindingResult.getFieldErrors());
        }
        sensorService.register(convertedSensor);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public List<SensorDTO> findAll() {
        return sensorService.findAll().stream()
                .map(this::convertToSensorDTO)
                .collect(toList());
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }
}
