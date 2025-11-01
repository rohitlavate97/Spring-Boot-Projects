package com.alchemist.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alchemist.scheduler.TaskSchedulerService;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

    @Autowired
    private TaskSchedulerService schedulerService;

    // ✅ Manual trigger endpoint
    @GetMapping("/trigger-now")
    public String triggerNow() {
        schedulerService.runManualTask();
        return "✅ Manual scheduler triggered successfully!";
    }
}