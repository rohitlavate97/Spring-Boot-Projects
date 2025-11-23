# Batch Processing and Scheduling

## Table of Contents
- [Spring Batch](#spring-batch)
- [Task Scheduling](#task-scheduling)
- [Best Practices](#best-practices)

---

## Spring Batch

### What is Spring Batch?

Spring Batch is a framework for batch processing, designed for:
- Processing large volumes of data
- ETL (Extract, Transform, Load) operations
- Scheduled jobs
- Transaction management

### Batch Architecture

```
Job
 └─ Step 1
     ├─ ItemReader (Read data)
     ├─ ItemProcessor (Transform data)
     └─ ItemWriter (Write data)
 └─ Step 2
     └─ Tasklet (Single operation)
```

### Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-batch</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Configuration

```yaml
spring:
  batch:
    job:
      enabled: false  # Disable auto-run on startup
    jdbc:
      initialize-schema: always
```

### Simple Batch Job

#### Entity

```java
@Data
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String department;
    private Double salary;
}
```

#### CSV Data (employees.csv)

```csv
name,department,salary
John Doe,IT,75000
Jane Smith,HR,65000
Bob Johnson,Finance,70000
```

#### Batch Configuration

```java
@Configuration
@EnableBatchProcessing
public class BatchConfig {
    
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    private EmployeeRepository repository;
    
    // Reader: Read from CSV
    @Bean
    public FlatFileItemReader<Employee> reader() {
        return new FlatFileItemReaderBuilder<Employee>()
                .name("employeeReader")
                .resource(new ClassPathResource("employees.csv"))
                .delimited()
                .names("name", "department", "salary")
                .targetType(Employee.class)
                .linesToSkip(1)  // Skip header
                .build();
    }
    
    // Processor: Transform data
    @Bean
    public ItemProcessor<Employee, Employee> processor() {
        return employee -> {
            // Business logic - e.g., apply bonus
            if (employee.getSalary() > 70000) {
                employee.setSalary(employee.getSalary() * 1.1);
            }
            return employee;
        };
    }
    
    // Writer: Save to database
    @Bean
    public RepositoryItemWriter<Employee> writer() {
        RepositoryItemWriter<Employee> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");
        return writer;
    }
    
    // Step: Combine reader, processor, writer
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Employee, Employee>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    
    // Job: Define the job
    @Bean
    public Job importEmployeeJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importEmployeeJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }
}
```

#### Job Listener

```java
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
    
    private static final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    
    @Autowired
    private EmployeeRepository repository;
    
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("Job completed successfully!");
            
            repository.findAll().forEach(employee -> 
                logger.info("Employee: {}, Salary: {}", employee.getName(), employee.getSalary())
            );
        }
    }
}
```

### Running Batch Job

```java
@RestController
@RequestMapping("/api/batch")
public class BatchController {
    
    @Autowired
    private JobLauncher jobLauncher;
    
    @Autowired
    private Job importEmployeeJob;
    
    @PostMapping("/run")
    public ResponseEntity<String> runJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("startTime", System.currentTimeMillis())
                .toJobParameters();
        
        JobExecution execution = jobLauncher.run(importEmployeeJob, params);
        
        return ResponseEntity.ok("Job Status: " + execution.getStatus());
    }
}
```

### Tasklet (Simple Task)

```java
@Bean
public Step cleanupStep() {
    return stepBuilderFactory.get("cleanupStep")
            .tasklet((contribution, chunkContext) -> {
                // Cleanup logic
                logger.info("Performing cleanup...");
                return RepeatStatus.FINISHED;
            })
            .build();
}

@Bean
public Job cleanupJob() {
    return jobBuilderFactory.get("cleanupJob")
            .start(cleanupStep())
            .build();
}
```

### Multi-Step Job

```java
@Bean
public Job multiStepJob() {
    return jobBuilderFactory.get("multiStepJob")
            .incrementer(new RunIdIncrementer())
            .start(step1())
            .next(step2())
            .next(step3())
            .build();
}
```

### Conditional Flow

```java
@Bean
public Job conditionalJob() {
    return jobBuilderFactory.get("conditionalJob")
            .start(step1())
            .on("COMPLETED").to(step2())
            .from(step1()).on("FAILED").to(errorHandlingStep())
            .end()
            .build();
}
```

---

## Task Scheduling

### Enable Scheduling

```java
@SpringBootApplication
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### Fixed Rate Scheduling

```java
@Component
public class ScheduledTasks {
    
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    // Execute every 5 seconds
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        logger.info("The time is now {}", dateFormat.format(new Date()));
    }
}
```

### Fixed Delay Scheduling

```java
// Execute 5 seconds after previous execution completes
@Scheduled(fixedDelay = 5000)
public void processData() {
    logger.info("Processing data at {}", dateFormat.format(new Date()));
    // Long-running task
}
```

### Initial Delay

```java
// Wait 10 seconds before first execution, then every 5 seconds
@Scheduled(initialDelay = 10000, fixedRate = 5000)
public void initializeData() {
    logger.info("Initializing data at {}", dateFormat.format(new Date()));
}
```

### Cron Expressions

```java
@Component
public class CronScheduledTasks {
    
    private static final Logger logger = LoggerFactory.getLogger(CronScheduledTasks.class);
    
    // Every day at 9 AM
    @Scheduled(cron = "0 0 9 * * *")
    public void dailyReport() {
        logger.info("Generating daily report at 9 AM");
    }
    
    // Every Monday at 10 AM
    @Scheduled(cron = "0 0 10 * * MON")
    public void weeklyReport() {
        logger.info("Generating weekly report");
    }
    
    // Every 30 minutes
    @Scheduled(cron = "0 */30 * * * *")
    public void halfHourlyTask() {
        logger.info("Running half-hourly task");
    }
    
    // Every hour on the hour
    @Scheduled(cron = "0 0 * * * *")
    public void hourlyTask() {
        logger.info("Running hourly task");
    }
    
    // First day of every month at midnight
    @Scheduled(cron = "0 0 0 1 * *")
    public void monthlyReport() {
        logger.info("Generating monthly report");
    }
}
```

### Cron Expression Format

```
┌───────────── second (0-59)
│ ┌───────────── minute (0-59)
│ │ ┌───────────── hour (0-23)
│ │ │ ┌───────────── day of month (1-31)
│ │ │ │ ┌───────────── month (1-12 or JAN-DEC)
│ │ │ │ │ ┌───────────── day of week (0-7 or SUN-SAT)
│ │ │ │ │ │
* * * * * *
```

### Common Cron Examples

| Expression | Description |
|-----------|-------------|
| `0 0 12 * * *` | Every day at noon |
| `0 15 10 * * *` | Every day at 10:15 AM |
| `0 0/5 * * * *` | Every 5 minutes |
| `0 0 8-18 * * *` | Every hour between 8 AM and 6 PM |
| `0 0 0 * * MON-FRI` | Midnight on weekdays |
| `0 0 0 1 1 *` | Midnight on January 1st |

### Async Scheduling

```java
@Configuration
@EnableAsync
@EnableScheduling
public class SchedulingConfig implements AsyncConfigurer {
    
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("scheduled-task-");
        scheduler.initialize();
        return scheduler;
    }
    
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }
}

@Component
public class AsyncScheduledTasks {
    
    @Async
    @Scheduled(fixedRate = 10000)
    public void asyncTask() {
        // Runs in separate thread
        logger.info("Async task running in: {}", Thread.currentThread().getName());
    }
}
```

### Dynamic Scheduling

```java
@Service
public class DynamicSchedulingService {
    
    @Autowired
    private TaskScheduler taskScheduler;
    
    private ScheduledFuture<?> scheduledTask;
    
    public void startScheduling(String cronExpression) {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
        
        scheduledTask = taskScheduler.schedule(
            this::performTask,
            new CronTrigger(cronExpression)
        );
    }
    
    public void stopScheduling() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
    }
    
    private void performTask() {
        logger.info("Dynamic task executed at: {}", new Date());
    }
}
```

---

## Best Practices

### 1. Batch Processing

```java
@Bean
public Step optimizedStep() {
    return stepBuilderFactory.get("optimizedStep")
            .<Employee, Employee>chunk(100)  // Larger chunk size for better performance
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .faultTolerant()
            .skipLimit(10)
            .skip(Exception.class)
            .retryLimit(3)
            .retry(DeadlockLoserDataAccessException.class)
            .build();
}
```

### 2. Error Handling

```java
@Component
public class CustomItemWriteListener implements ItemWriteListener<Employee> {
    
    @Override
    public void onWriteError(Exception exception, List<? extends Employee> items) {
        logger.error("Error writing items: {}", exception.getMessage());
        items.forEach(item -> logger.error("Failed item: {}", item));
    }
}
```

### 3. Job Parameters

```java
@Bean
@StepScope
public FlatFileItemReader<Employee> reader(@Value("#{jobParameters['inputFile']}") String inputFile) {
    return new FlatFileItemReaderBuilder<Employee>()
            .name("employeeReader")
            .resource(new FileSystemResource(inputFile))
            .delimited()
            .names("name", "department", "salary")
            .targetType(Employee.class)
            .build();
}
```

### 4. Monitoring

```java
@RestController
@RequestMapping("/api/batch/jobs")
public class JobMonitoringController {
    
    @Autowired
    private JobExplorer jobExplorer;
    
    @GetMapping
    public List<String> getJobNames() {
        return jobExplorer.getJobNames();
    }
    
    @GetMapping("/{jobName}/instances")
    public List<JobInstance> getJobInstances(@PathVariable String jobName) {
        return jobExplorer.getJobInstances(jobName, 0, 10);
    }
    
    @GetMapping("/executions/{executionId}")
    public JobExecution getJobExecution(@PathVariable Long executionId) {
        return jobExplorer.getJobExecution(executionId);
    }
}
```

### 5. Scheduling Best Practices

- Use `fixedDelay` for dependent tasks
- Use `fixedRate` for independent tasks
- Use `@Async` for long-running tasks
- Configure thread pool size appropriately
- Handle exceptions properly
- Use externalized cron expressions

```yaml
# application.yml
scheduling:
  daily-report: "0 0 9 * * *"
  weekly-report: "0 0 10 * * MON"
```

```java
@Scheduled(cron = "${scheduling.daily-report}")
public void dailyReport() {
    // Task implementation
}
```

---

*Continue to [10-Testing.md](10-Testing.md) for testing strategies.*
