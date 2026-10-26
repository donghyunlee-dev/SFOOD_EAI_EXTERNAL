package com.sfood.eai.external.entity.godo;

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
@Table(name="TBL_GOODS_GODO_TRANS_LOG")
public class GodoGoodsTransLog {
    @Id
    @Column(name = "SEQ_NO")
    private Long seqNo;

    @CreatedDate
    @Column(name = "LOG_DATE" , updatable = false)
    private LocalDateTime logDate;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "MALL_CODE")
    private String mallCode;

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

    @Column(name = "DISPLAY_PC_YN")
    private String displayPcYn;

    @Column(name = "SALES_PC_YN")
    private String salesPcYn;

    @Column(name = "DISPLAY_MOBILE_YN")
    private String displayMobileYn;

    @Column(name = "SALES_MOBILE_YN")
    private String salesMobileYn;

    @Column(name = "COST")
    private Integer cost;

    @Column(name = "FIXED_PRICE")
    private Integer fixedPrice;

    @Column(name = "SALES_PRICE")
    private Integer salesPrice;

    @Column(name = "TAX_YN")
    private String taxYn;

    @Column(name = "EXT1")
    private String ext1;

    @Column(name = "EXT2")
    private String ext2;

    @Column(name = "RESULT")
    private String result;
}

