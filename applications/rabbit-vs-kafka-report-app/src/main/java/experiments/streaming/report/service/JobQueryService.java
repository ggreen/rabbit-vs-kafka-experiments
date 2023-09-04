package experiments.streaming.report.service;


import experiments.streaming.report.domain.JobStats;

import java.util.Collection;

public interface JobQueryService {
    Collection<JobStats> findJobs();
}
