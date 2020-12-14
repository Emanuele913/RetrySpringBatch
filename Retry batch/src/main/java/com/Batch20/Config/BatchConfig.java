package com.Batch20.Config;

import com.Batch20.Model.User;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;


import javax.sql.DataSource;

//Esempio n1 , qui utilizziamo l'annotation @Retryable sullo step

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfig {

    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
    StepBuilderFactory stepBuilderFactory;
    @Autowired
    DataSource dataSource;

    @Bean
    public FlatFileItemReader<User> reader(){
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("users.csv"));
        reader.setLineMapper(new DefaultLineMapper<User>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"name"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
                setTargetType(User.class);
            }});
        }});
        return reader;
    }

    int count = 0;
    @Bean
    public FlatFileItemWriter<User> writerFallBack() throws Exception {
        //Create writer instance
        FlatFileItemWriter<User> writer = new FlatFileItemWriter<>();

        count++;
        System.out.println("try!"+""+count);
        if (count < 4)
            throw new Exception();

            System.out.println("SUCCESSS.................");

            //Set output file location
            writer.setName("userItemWriter");
            writer.setResource(new FileSystemResource("C:\\Users\\eiandolo\\Desktop\\test.txt"));

            //All job repetitions should "append" to same output file
            writer.setAppendAllowed(true);

            //Name field values sequence based on object properties
            writer.setLineAggregator(new DelimitedLineAggregator<User>() {
                {
                    setDelimiter(",");
                    setFieldExtractor(new BeanWrapperFieldExtractor<User>() {
                        {
                            setNames(new String[]{"id", "name"});
                        }
                    });
                }
            });

        return writer;
    }



    @Bean
    @Retryable(maxAttempts = 9, value = Exception.class, backoff = @Backoff(delay = 2000))
    public Step stepRetry() throws Exception {
        return stepBuilderFactory.get("step2").<User,User>chunk(1)
                .reader(reader())
                .writer(writerFallBack())
                .build();

    }

    @Bean
    public Job job(@Qualifier("stepRetry") Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .start(step1)
                .build();
    }


}

