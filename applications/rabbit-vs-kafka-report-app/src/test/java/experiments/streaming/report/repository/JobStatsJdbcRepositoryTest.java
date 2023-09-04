package experiments.streaming.report.repository;

import experiments.streaming.report.domain.JobStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

import static nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobStatsJdbcRepositoryTest {

    private JobStatsJdbcRepository subject;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private RowMapper<JobStats> rowMapper;
    private List<JobStats> expected =
            new ArrayList<JobStats>(of(JobStats.class).createCollection(1));

    @BeforeEach
    void setUp() {
        subject = new JobStatsJdbcRepository(jdbcTemplate,rowMapper);
    }

    @Test
    void findJobs() {

        when(jdbcTemplate.query(anyString(),any(RowMapper.class))).thenReturn(expected);
        assertEquals(expected, subject.findJobs());
    }
}