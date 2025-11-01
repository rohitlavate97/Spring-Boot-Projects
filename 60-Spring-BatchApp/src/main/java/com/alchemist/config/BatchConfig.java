package com.alchemist.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.alchemist.entity.Customer;
import com.alchemist.repository.CustomerRepository;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private CustomerRepository customerRepository;

    // --- Item Reader ---
    @Bean
    public FlatFileItemReader<Customer> customerReader() {   //Reads data line by line from a CSV file.Each line → converted into a Customer object using lineMapper().The first line (header) is skipped.
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        itemReader.setName("customer-item-reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Customer> lineMapper() {  //DelimitedLineTokenizer → splits each line into fields using commas.The fields are assigned by name (id, firstname, etc.).BeanWrapperFieldSetMapper → automatically binds those field names to your Customer entity’s properties.
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstname", "lastname", "email", "gender", "contactNum", "country", "dob");

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    // --- Item Processor ---
    @Bean
    public CustomerProcessor customerProcessor() {  //Used for:Cleaning data,Converting formats,Filtering invalid records
        return new CustomerProcessor();
    }

    // --- Item Writer ---
    @Bean
    public RepositoryItemWriter<Customer> customerWriter() {
        RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
        writer.setRepository(customerRepository);
        writer.setMethodName("save");
        return writer;
    }

    // --- Step ---
    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {  //Each chunk of 10 records is read → processed → written in one transaction.If any record in the chunk fails, the entire chunk rolls back.JobRepository and TransactionManager are provided by Spring Batch automatically.
        return new StepBuilder("step-1", jobRepository)
                .<Customer, Customer>chunk(10, transactionManager)
                .reader(customerReader())
                .processor(customerProcessor())
                .writer(customerWriter())
                .build();
    }

    // --- Job ---
    @Bean
    public Job job(JobRepository jobRepository, Step step) {  //Defines a Job named "customer-import".Contains a single Step (step-1).You can later add more steps (e.g., validation, reporting).
        return new JobBuilder("customer-import", jobRepository)
                .start(step)
                .build();
    }
}
