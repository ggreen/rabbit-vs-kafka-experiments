package experiments.streaming.batch;

import org.springframework.batch.core.launch.support.CommandLineJobRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchRabbitStreamBatchApp {

	public static void main(String[] args) throws Exception {
		System.exit(SpringApplication.exit(SpringApplication.run(BatchRabbitStreamBatchApp.class, args)));
//		CommandLineJobRunner.main(args);
	}

}
