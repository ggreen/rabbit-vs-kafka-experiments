package showcase.high.throughput.microservices.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public record Payment(String id, String details, String contact, String location, double amount, Timestamp timestamp)
        implements Serializable {
}
