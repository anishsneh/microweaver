package com.anishsneh.microweaver.service.core.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * The Class MicroweaverCoreServiceHealthIndicator.
 * 
 * @author Anish Sneh
 * 
 */
@Component
public class MicroweaverCoreServiceHealthIndicator implements HealthIndicator {

    /* (non-Javadoc)
     * @see org.springframework.boot.actuate.health.HealthIndicator#health()
     */
    @Override
    public Health health() {
        return Health.up().build();
    }
}