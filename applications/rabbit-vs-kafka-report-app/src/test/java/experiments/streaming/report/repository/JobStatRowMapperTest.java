package experiments.streaming.report.repository;

import experiments.streaming.report.domain.JobStats;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobStatRowMapperTest {

    @Mock
    private ResultSet rs;
    private int rowNum = 1;
    private String jobName = "job1";
    private int totalCount = 1;
    private String timeMin = "2";
    private String timeMax = "4";
    private String timeAvg = "3";
    private String time90th = "2";
    private String time99th = "23";
    private String time99_99th = "5";
    private String tpsMin = "6";
    private String tpsMax = "7";
    private String tpsAvg = "8";
    private String tps90th = "9";
    private String tps99th = "10";
    private String tps99_99th = "11";

    @SneakyThrows
    @Test
    void mapRow() {
        var expected = new JobStats(
                jobName,
        totalCount,
        timeMin,
        timeMax,
        timeAvg,
        time90th,
        time99th,
        time99_99th,
        tpsMin,
        tpsMax,
        tpsAvg,
        tps90th,
        tps99th,
        tps99_99th
        );


        var col = 1;
        when(rs.getString(col++)).thenReturn(jobName);
        when(rs.getInt(col++)).thenReturn(totalCount);
        when(rs.getString(col++)).thenReturn(timeMin);
        when(rs.getString(col++)).thenReturn(timeMax);
        when(rs.getString(col++)).thenReturn(timeAvg);
        when(rs.getString(col++)).thenReturn(time90th);
        when(rs.getString(col++)).thenReturn(time99th);
        when(rs.getString(col++)).thenReturn(time99_99th);
        when(rs.getString(col++)).thenReturn(tpsMin);
        when(rs.getString(col++)).thenReturn(tpsMax);
        when(rs.getString(col++)).thenReturn(tpsAvg);
        when(rs.getString(col++)).thenReturn(tps90th);
        when(rs.getString(col++)).thenReturn(tps99th);
        when(rs.getString(col++)).thenReturn(tps99_99th);

        var subject = new JobStatRowMapper();

        var actual = subject.mapRow(rs,rowNum);

        assertEquals(expected, actual);
    }
}