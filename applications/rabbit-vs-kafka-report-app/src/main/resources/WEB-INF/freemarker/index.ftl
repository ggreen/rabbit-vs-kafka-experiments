<html>
<head>
  <style>
    #jobs {
      font-family: Arial, Helvetica, sans-serif;
      border-collapse: collapse;
      width: 100%;
    }

    #jobs td, #jobs th {
      border: 1px solid #ddd;
      padding: 8px;
    }

    #jobs tr:nth-child(even){background-color: #f2f2f2;}

    #jobs tr:hover {background-color: #ddd;}

    #jobs th {
      padding-top: 12px;
      padding-bottom: 12px;
      text-align: left;
      background-color: #04AA6D;
      color: white;
    }
  </style>
</head>
  <body>

  <h1>Spring Batch Reports</h1>
  <h2>Job TPS Report</h2>
  <table id="jobs">
      <tr>
        <th>jobName</th>
        <th>totalCount</th>
<!--        <th>timeMin</th>-->
<!--        <th>timeMax</th>-->
<!--        <th>timeAvg</th>-->
<!--        <th>time90th</th>-->
<!--        <th>time99th</th>-->
<!--        <th>time99_99th</th>-->
        <th>tpsMin</th>
        <th>tpsMax</th>
        <th>tpsAvg</th>
        <th>tps90th</th>
        <th>tps99th</th>
        <th>tps99_99th</th>
      </tr>
      <#list jobs as jobStat>
      <tr>
        <td>${jobStat.jobName}</td>
        <td>${jobStat.totalCount}</td>
<!--        <td>${jobStat.timeMin}</td>-->
<!--        <td>${jobStat.timeMax}</td>-->
<!--        <td>${jobStat.timeAvg}</td>-->
<!--        <td>${jobStat.time90th}</td>-->
<!--        <td>${jobStat.time99th}</td>-->
<!--        <td>${jobStat.time99_99th}</td>-->
        <td>${jobStat.tpsMin}</td>
        <td>${jobStat.tpsMax}</td>
        <td>${jobStat.tpsAvg}</td>
        <td>${jobStat.tps90th}</td>
        <td>${jobStat.tps99th}</td>
        <td>${jobStat.tps99_99th}</td>
      </tr>
      </#list>
  </table>


  </body>
</html>
