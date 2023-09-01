package experiments.streaming.batch;

import experiments.streaming.batch.rabbit.RabbitStreamPurger;
import nyla.solutions.core.util.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchRabbitStreamBatchApp {

	public static void main(String[] args) throws Exception {
		var settings = Config.loadArgs(args);

		if("rabbit".equals(settings.getProperty("spring.profiles.active")))
			RabbitStreamPurger.purgeStream(args);

		SpringApplication.exit(SpringApplication.run(BatchRabbitStreamBatchApp.class, args));
	}

}
