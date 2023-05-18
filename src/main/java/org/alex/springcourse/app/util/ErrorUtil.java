package org.alex.springcourse.app.util;

import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorUtil {

    public static void processFieldErrors(List<FieldError> fieldErrors) {
        StringBuilder errMsg = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            errMsg.append(fieldError.getField())
                    .append(" - ")
                    .append(fieldError.getDefaultMessage())
                    .append(";");
        }
        throw new MeasurementException(errMsg.toString());
    }
}
