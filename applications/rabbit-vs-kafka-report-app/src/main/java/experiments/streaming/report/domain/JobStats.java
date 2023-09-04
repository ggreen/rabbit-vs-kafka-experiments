package experiments.streaming.report.domain;

public record JobStats(
        String jobName,
        int totalCount,
        String timeMin,
        String timeMax,
        String timeAvg,
        String time90th,
        String time99th,
        String time99_99th,
        String tpsMin,
        String tpsMax,
        String tpsAvg,
        String tps90th,
        String tps99th,
        String tps99_99th) {

}