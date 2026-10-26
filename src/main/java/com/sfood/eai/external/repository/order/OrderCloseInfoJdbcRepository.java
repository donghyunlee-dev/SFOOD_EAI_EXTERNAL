package com.sfood.eai.external.repository.order;

import com.sfood.eai.external.entity.order.OrderCloseInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderCloseInfoJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<OrderCloseInfo> items) {
        String sql = """
            INSERT INTO TBL_ORDER_CLOSE_INFO(
                COMPANY_CODE,
                ORDER_MERGE_NO,
                ORDER_DETAIL_SEQ,
                CLOSE_METHOD,
                CLOSE_YN,
                CLOSE_DATE,
                ORDER_TYPE,
                CREATE_DATE,
                CREATE_ID,
                UPDATE_DATE,
                UPDATE_ID
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                OrderCloseInfo order = items.get(i);

                ps.setString(1, order.getId().getCompanyCode());
                ps.setString(2, order.getId().getOrderMergeNo());
                ps.setLong(3, order.getId().getOrderDetailSeq());
                ps.setString(4, order.getCloseMethod());
                ps.setString(5, order.getCloseYn());
                ps.setTimestamp(6, Timestamp.valueOf(order.getCloseDate()));
                ps.setString(7, order.getOrderType());
                ps.setTimestamp(8, Timestamp.valueOf(order.getCreateDate()));
                ps.setString(9, order.getCreateId());
                ps.setTimestamp(10, Timestamp.valueOf(order.getUpdateDate()));
                ps.setString(11, order.getUpdateId());
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
    }
}