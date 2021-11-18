package org.nmcpye.activitiesmanagement.extended.supportexternalmodule.config;

import org.nmcpye.activitiesmanagement.extended.external.location.LocationManager;
import org.nmcpye.activitiesmanagement.extended.supportexternalmodule.location.DefaultLocationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Hamza on 18/11/2021.
 */
@Configuration("externalServiceConfig")
@EnableAsync
@EnableScheduling
public class ServiceConfig {
    @Bean
    public LocationManager locationManager() {
        return DefaultLocationManager.getDefault();
    }
}
