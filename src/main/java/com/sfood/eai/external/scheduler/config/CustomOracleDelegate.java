package com.sfood.eai.external.scheduler.config;

import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomOracleDelegate extends StdJDBCDelegate {

    @Override
    protected void setBoolean(PreparedStatement ps, int index, boolean val) throws SQLException {
        ps.setString(index, val ? "1" : "0");
    }
}
