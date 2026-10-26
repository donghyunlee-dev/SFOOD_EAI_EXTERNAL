package com.sfood.eai.external.repository.order;

import com.sfood.eai.external.entity.id.order.OrderCloseInfoId;
import com.sfood.eai.external.entity.order.OrderCloseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OrderCloseInfoRepository extends JpaRepository<OrderCloseInfo, OrderCloseInfoId> {

    @Modifying
    @Transactional
    @Query(
            value = """
            INSERT INTO TBL_ORDER_CLOSE_INFO(COMPANY_CODE, ORDER_MERGE_NO, ORDER_DETAIL_SEQ, CLOSE_METHOD, CLOSE_YN, CLOSE_DATE, ORDER_TYPE, CREATE_DATE, CREATE_ID, UPDATE_DATE, UPDATE_ID)
            VALUES(:#{#order.id.companyCode}, :#{#order.id.orderMergeNo}, :#{#order.id.orderDetailSeq}, :#{#order.closeMethod}, :#{#order.closeYn}, :#{#order.closeDate}, :#{#order.orderType}, :#{#order.createDate}, :#{#order.createId}, :#{#order.updateDate}, :#{#order.updateId})
            """, nativeQuery = true
    )
    void insertOrderCloseInfoBatch(@Param("order") OrderCloseInfo order);
}
