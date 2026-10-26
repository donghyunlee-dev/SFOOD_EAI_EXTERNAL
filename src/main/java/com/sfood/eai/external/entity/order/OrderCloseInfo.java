package com.sfood.eai.external.entity.order;

import com.sfood.eai.external.entity.id.order.OrderCloseInfoId;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_ORDER_CLOSE_INFO")
@Entity
public class OrderCloseInfo extends BaseEntity {
    @EmbeddedId
    private OrderCloseInfoId id;

    @Column(name = "CLOSE_METHOD")
    private String closeMethod;

    @Column(name = "CLOSE_YN")
    private String closeYn;

    @CreatedDate
    @Column(name = "CLOSE_DATE", nullable = false)
    private LocalDateTime closeDate;

    @Column(name = "ORDER_TYPE")
    private String orderType;

    @Builder
    public OrderCloseInfo(OrderCloseInfoId id, String createId, LocalDateTime createDate, String updateId, LocalDateTime updateDate,  String closeMethod
            , String closeYn, LocalDateTime closeDate, String orderType) {
        this.id = id;
        super.createId = createId;
        super.createDate = createDate;
        super.updateId = updateId;
        super.updateDate = updateDate;
        this.closeMethod = closeMethod;
        this.closeYn = closeYn;
        this.closeDate = closeDate;
        this.orderType = orderType;
    }
}
