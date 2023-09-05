package experiments.streaming.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public record Transaction(String id, String details, String contact, String location, double amount, Timestamp timestamp)
        implements Serializable {
}
