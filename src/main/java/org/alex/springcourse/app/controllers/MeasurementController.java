package org.alex.springcourse.app.controllers;

import org.alex.springcourse.app.dto.MeasurementDTO;
import org.alex.springcourse.app.models.Measurement;
import org.alex.springcourse.app.services.MeasurementService;
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
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {
    private static final String ATTR_DAYS = "days";

    private final MeasurementService measurementService;
    private final ModelMapper modelMapper;
    private final MeasurementValidator measurementValidator;

    @Autowired
    public MeasurementController(MeasurementService measurementService, ModelMapper modelMapper, MeasurementValidator measurementValidator) {
        this.measurementService = measurementService;
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;
    }

    @GetMapping
    public List<MeasurementDTO> findAll() {
        return measurementService.findAll()
                .stream()
                .map(this::convertToMeasurementDTO)
                .collect(toList());
    }

    @GetMapping("/rainyDaysCount")
    public Map<String, Long> getRainyDaysCount() {
        long rainyDaysCount = measurementService.findAll().stream()
                .filter(Measurement::isRaining)
                .count();
        return Map.of(ATTR_DAYS, rainyDaysCount);
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult) {
        Measurement convertedMeasurement = convertToMeasurement(measurementDTO);
        measurementValidator.validate(convertedMeasurement, bindingResult);
        if (bindingResult.hasErrors()) {
            ErrorUtil.processFieldErrors(bindingResult.getFieldErrors());
        }
        measurementService.add(convertedMeasurement);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}
