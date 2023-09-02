package experiments.streaming.batch;

import nyla.solutions.core.util.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchRabbitStreamBatchApp {

	public static void main(String[] args) throws Exception {
		Config.loadArgs(args);
		SpringApplication.exit(SpringApplication.run(BatchRabbitStreamBatchApp.class, args));
	}

}
