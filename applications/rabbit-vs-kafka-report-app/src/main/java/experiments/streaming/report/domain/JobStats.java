package experiments.streaming.report.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobStats {
    private String jobName;
    private int totalCount;
    private String timeMin;
    private String timeMax;
    private String timeAvg;
    private String time90th;
    private String time99th;
    private String time99_99th;
    private String tpsMin;
    private String tpsMax;
    private String tpsAvg;
    private String tps90th;
    private String tps99th;
    private String tps99_99th;

}