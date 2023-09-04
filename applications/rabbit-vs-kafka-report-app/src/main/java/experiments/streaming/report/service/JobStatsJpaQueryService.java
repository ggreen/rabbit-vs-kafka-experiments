package experiments.streaming.report.service;

import experiments.streaming.report.domain.JobStats;
import experiments.streaming.report.repository.JobStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobStatsJpaQueryService implements JobQueryService{

    private final JobStatsRepository repository;

    @Override
    public List<JobStats> findJobs() {
        return repository.findJobs();
    }
}
