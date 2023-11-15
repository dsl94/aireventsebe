package com.airevents.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StravaSync {
    @Scheduled(cron = "@midnight")
    public void trackOverduePayments() {
        System.out.println("Scheduled task running");
    }
}
