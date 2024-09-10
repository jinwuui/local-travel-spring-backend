package com.jinwuui.howdoilook.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
public class MaxFileCountValidator implements ConstraintValidator<MaxFileCount, List<MultipartFile>> {

    private int max;

    @Override
    public void initialize(MaxFileCount constraintAnnotation) {
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(List<MultipartFile> objects, ConstraintValidatorContext context) {
        return objects == null || objects.size() <= max;
    }
}

