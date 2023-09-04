package experiments.streaming.report.service;

import experiments.streaming.report.domain.JobStats;
import experiments.streaming.report.repository.JobStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class JobStatsJpaQueryService implements JobQueryService{

    private final JobStatsRepository repository;

    @Override
    public Collection<JobStats> findJobs() {
        return repository.findJobs();
    }
}
