package com.example.simsmanagerclient.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties
public class Config {

    @Value("#{${scheduling.this.instance.active} == 1}")
    private boolean isSchedulingActive;

    @Value("${scheduling.before.birthdate.subject}")
    private String emailSubject;

    @Value("${scheduling.before.birthdate.message}")
    private String emailMessage;

    @Value("${scheduling.before.birthdate.days}")
    private Integer daysBeforeBirthDate;

    @Value("${scheduling.before.birthdate.discount}")
    private String discountPercentage;

    @Value("${scheduling.before.birthdate.placeholder.firstname}")
    private String firstNamePlaceholder;

    @Value("${scheduling.before.birthdate.placeholder.discount}")
    private String discountPlaceholder;

    @Value("${scheduling.at.birthdate.export.filename}")
    private String birthdayExportFilename;
}
