package experiments.streaming.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchRabbitStreamBatchApp {

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(BatchRabbitStreamBatchApp.class, args)));
	}

}
