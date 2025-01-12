/**
 * Projct: Banking System - EOM-Scheduler
 * This class contains configuration for scheduler. 
 * Author: Arockiam Joseph
 * Created Date:11-01-2025
 * Version: 1.0
 */
package com.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfig {
	
	@Value("${task.eod.cron.expression}")
	private String eodCronExpression;
	
	@Value("${task.eom.cron.expression}")
	private String eomCronExpression;
	
	@Bean
	public String eodCronExpression() {
	    return eodCronExpression;
	}
		
	@Bean
	public String eomCronExpression() {
	    return eomCronExpression;
	}
}
