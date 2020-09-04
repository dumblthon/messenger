package com.dumblthon.messenger.auth.repository;

import com.dumblthon.messenger.auth.userinfo.UserInfo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.JdbcDatabaseContainer;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Paths;
import java.sql.SQLException;

public class DbIntegrationTest {

    public static NamedParameterJdbcTemplate initialize(JdbcDatabaseContainer<?> container)
            throws SQLException, LiquibaseException {

        DataSource ds = createDataSource(container);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ds);

        String sql = "CREATE SCHEMA IF NOT EXISTS " + UserInfo.USER_INFO.getName();
        jdbcTemplate.getJdbcTemplate().execute(sql);

        updateDatabase(ds);
        return jdbcTemplate;
    }
    
    public static DataSource createDataSource(JdbcDatabaseContainer<?> container) {
        HikariConfig dsConfig = new HikariConfig();
        dsConfig.setDriverClassName(container.getDriverClassName());
        dsConfig.setJdbcUrl(container.getJdbcUrl());
        dsConfig.setUsername(container.getUsername());
        dsConfig.setPassword(container.getPassword());
        // dsConfig.setSchema(container.getDatabaseName());
        return new HikariDataSource(dsConfig);
    }

    public static void updateDatabase(DataSource ds) throws SQLException, LiquibaseException {
        String changelogRelPath = "liquibase/changelog/db.changelog-master.xml";
        File baseDir = Paths.get(".").toFile();
        ResourceAccessor resourceAccessor = new FileSystemResourceAccessor(baseDir);
        JdbcConnection jdbcConnection = new JdbcConnection(ds.getConnection());
        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(jdbcConnection);
        Liquibase liquibase = new Liquibase(changelogRelPath, resourceAccessor, database);
        liquibase.update(new Contexts());
    }

    public static void truncate(JdbcTemplate jdbcTemplate, String schema, String table) {
        String sql = String.format("TRUNCATE %s.%s CASCADE", schema, table);
        jdbcTemplate.execute(sql);
    }
    
}
