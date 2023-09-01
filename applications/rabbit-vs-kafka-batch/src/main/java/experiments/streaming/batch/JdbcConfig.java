/*
 *
 *  * Copyright 2023 VMware, Inc.
 *  * SPDX-License-Identifier: GPL-3.0
 *
 */

package experiments.streaming.batch;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@Component
public class JdbcConfig implements BeanPostProcessor {
    @Value("${spring.liquibase.defaultSchema}")
    private String schemaName;

    @SneakyThrows
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource dataSource) {
            try (var conn = dataSource.getConnection();
                 var statement = conn.createStatement()) {
                log.info("Going to create DB schema '{}' if not exists.", schemaName);
                statement.execute("create schema if not exists " + schemaName);

//                log.info("Create spring batch databases");
//                var ddl = new ClassPathResource(postgres_ddl_path).getContentAsString(StandardCharsets.UTF_8);
//                log.info("DDL: \n {}",ddl);
//
//                statement.execute(ddl);

            } catch (SQLException e) {
                throw new RuntimeException("Failed to create schema '" + schemaName + "'", e);
            }
        }
        return bean;
    }
}
