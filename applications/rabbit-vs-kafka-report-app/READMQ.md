Example Spring Batch Report SQL 


```roomsql
select 
    job_name,
	count(*) total_cnt,
	min(total_time) time_min,
	max(total_time) time_max,
	avg(total_time) time_avg,
	percentile_disc(0.90) within group (order by total_time) time_p90th, 
	percentile_disc(0.99) within group (order by total_time) time_p99th,
	percentile_disc(0.9999) within group (order by total_time) time_p99_99th,
	to_char(min(tps),'FM9,999,999') as tps_min,
	to_char(max(tps),'FM9,999,999') as tps_max,
	to_char(avg(tps),'FM9,999,999') as tps_avg,
	to_char(percentile_disc(0.90) within group (order by tps),'FM9,999,999') as tps_p90th,
	to_char(percentile_disc(0.99) within group (order by tps),'FM9,999,999') as tps_p99th,
	to_char(percentile_disc(0.9999) within group (order by tps),'FM9,999,999') as tps_p99_99th
from
(select ji.job_name as job_name, je.end_time - je.start_time as total_time,
     (( select step.write_count from evt_stream.batch_step_execution step where step.job_execution_id= je.job_execution_id)
     /(extract(epoch from end_time)  - extract(epoch from start_time))) as tps
from evt_stream.batch_job_execution je, evt_stream.batch_job_instance ji
where 
	je.status  = 'COMPLETED' and 
	je.job_instance_id = ji.job_instance_id) job_timings
group by job_name;
```

Job Status

```shell
select 
    *
from evt_stream.batch_job_execution je, evt_stream.batch_job_instance ji
where
    job_name  = 'nats' and 
	je.job_instance_id = ji.job_instance_id
    
```