package com.alchemist.rest;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @GetMapping("/load")
    public String loadData() {
        try {
            // Build job parameters with unique timestamp (to prevent "Job already completed" errors)
            JobParameters jobParams = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(job, jobParams);
            return "✅ Batch Job Status: " + execution.getStatus();

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Job failed: " + e.getMessage();
        }
    }
}
