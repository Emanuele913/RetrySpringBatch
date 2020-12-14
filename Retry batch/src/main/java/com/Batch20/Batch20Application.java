package com.Batch20;


import com.Batch20.Config.BatchConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
@Import(BatchConfig.class)
@EnableRetry //per abilitare il retry
public class Batch20Application extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	JobLauncher jobLauncher;
	@Autowired
	Job job;
	@Autowired
	BatchConfig batchConfig;

	public static void main(String[] args) {
		SpringApplication.run(Batch20Application.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

		JobParameters params = new JobParametersBuilder()
				.addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(job, params);
	}
}
