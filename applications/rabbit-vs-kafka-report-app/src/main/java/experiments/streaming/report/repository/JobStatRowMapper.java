package experiments.streaming.report.repository;

import experiments.streaming.report.domain.JobStats;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class JobStatRowMapper implements RowMapper<JobStats> {
    @Override
    public JobStats mapRow(ResultSet rs, int rowNum) throws SQLException {
        int col =1;
        return new JobStats(
                rs.getString(col++),
                rs.getInt(col++),
                rs.getString(col++),
                rs.getString(col++),
                rs.getString(col++),
                rs.getString(col++),
                rs.getString(col++),
                rs.getString(col++),
                rs.getString(col++),
                rs.getString(col++),
                rs.getString(col++),
                rs.getString(col++),
                rs.getString(col++),
                rs.getString(col++)
                );
    }
}
