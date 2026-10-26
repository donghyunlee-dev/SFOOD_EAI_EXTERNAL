package com.sfood.eai.external.entity.sabang;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="TBL_GOODS_SABANG_TRANS_LOG")
public class SabangGoodsTransLog {
    @Id
    @Column(name = "SEQ_NO")
    private Long seqNo;

    @CreatedDate
    @Column(name = "LOG_DATE" , updatable = false)
    private LocalDateTime logDate;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "GOODS_CODE")
    private String goodsCode;

    @Column(name = "MALL_GOODS_CODE")
    private String mallGoodsCode;

    @Column(name = "MALL_GOODS_NAME")
    private String mallGoodsName;

    @Column(name = "SUPPLIER")
    private String supplier;

    @Column(name = "ORIGIN")
    private String origin;

    @Column(name = "SEARCH_KEYWORD")
    private String searchKeyword;

    @Column(name = "GOODS_STATUS")
    private String goodsStatus;

    @Column(name = "COST")
    private Integer cost;

    @Column(name = "SALES_PRICE")
    private Integer salesPrice;

    @Column(name = "CONSUMER_PRICE")
    private Integer consumerPrice;

    @Column(name = "TAX_YN")
    private String taxYn;

    @Column(name = "EXT1")
    private String ext1;

    @Column(name = "EXT2")
    private String ext2;

    @Column(name = "RESULT")
    private String result;
}
