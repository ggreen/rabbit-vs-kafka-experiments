package experiments.streaming.report.service;


import experiments.streaming.report.domain.JobStats;

import java.util.Collection;
import java.util.List;

public interface JobQueryService {
    List<JobStats> findJobs();
}
