package experiments.streaming.report.service;

import experiments.streaming.report.domain.JobStats;
import experiments.streaming.report.repository.JobStatsRepository;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobStatsJpaQueryServiceTest {

    @Mock
    private JobStatsRepository repository;
    private JobStatsJpaQueryService service;
    private List<JobStats> expected =
            new ArrayList<JobStats>(JavaBeanGeneratorCreator.of(JobStats.class).createCollection(2));

    @BeforeEach
    void setUp() {
        service = new JobStatsJpaQueryService(repository);
    }

    @Test
    void findJobs() {
        when(repository.findJobs()).thenReturn(expected);
        assertEquals(expected, service.findJobs());
    }
}