package ru.aracle.dungeons;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Connection {

    private HikariDataSource dataSource;
    public java.sql.Connection data;

    public Connection() {
        if (!Dungeons.settings.statement("connection.connect")) {
            Dungeons.instance.getServer().shutdown();
            return;
        }
        String URL = "jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=%s&useSSL=%s";
        String PoolName = "dungeons-%s";
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format(URL, address(), port(), database(), publicKeyRetrieval(), ssl()));
        config.setUsername(username());
        config.setPassword(password());
        config.setMinimumIdle(minimumIdle());
        config.setMaximumPoolSize(maximumPoolSize());
        config.setConnectionTimeout(connectionTimeout());
        config.setIdleTimeout(0);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        config.addDataSourceProperty("autoReconnect","true");
        config.setLeakDetectionThreshold(60 * 1000);
        config.setPoolName(String.format(PoolName, server()));
        try {
            dataSource = new HikariDataSource(config);
        } catch (Exception exception) {
            Dungeons.error(exception.getMessage());
            Dungeons.instance.getServer().shutdown();
        }
        data = connection();
    }

    public void close() {
        dataSource.close();
    }

    public java.sql.Connection connection() {
        java.sql.Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException throwables) {
            Dungeons.error(throwables.getMessage());
        }
        return connection;
    }

    public void set(String query) {
        try {
            PreparedStatement statement = data.prepareStatement(query);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException throwables) {
            Dungeons.error(throwables.getMessage());
        }
    }

    public ResultSet get(String query) {
        ResultSet result = null;
        try {
            result = data.prepareStatement(query).executeQuery();
        } catch (SQLException throwables) {
            Dungeons.error(throwables.getMessage());
        }
        return result;
    }

    public static String server() {
        return Dungeons.settings.string("connection.server");
    }

    public static Integer maximumPoolSize() {
        return Dungeons.settings.integer("connection.maximumPoolSize");
    }

    public static Integer connectionTimeout() {
        return Dungeons.settings.integer("connection.connectionTimeout");
    }

    public static Integer minimumIdle() {
        return Dungeons.settings.integer("connection.minimumIdle");
    }

    public static String password() {
        return Dungeons.settings.string("connection.password");
    }

    public static String username() {
        return Dungeons.settings.string("connection.username");
    }

    public static String address() {
        return Dungeons.settings.string("connection.address");
    }

    public static String port() {
        return Dungeons.settings.string("connection.port");
    }

    public static String database() {
        return Dungeons.settings.string("connection.database");
    }

    public static String publicKeyRetrieval() {
        return Dungeons.settings.string("connection.publicKeyRetrieval");
    }

    public static String ssl() {
        return Dungeons.settings.string("connection.ssl");
    }

}
