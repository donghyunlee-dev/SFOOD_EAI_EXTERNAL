package com.sfood.eai.external.entity.goods;

import com.sfood.eai.external.entity.id.goods.GoodsId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NamedEntityGraph
@NoArgsConstructor
@Entity
@Table(name="TBL_GOODS_MST")
@IdClass(GoodsId.class)
public class Goods {
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode ; // 회사코드
	
	@Id
	@Column(name = "GOODS_CODE")
	private String goodsCode; // 품목코드
	
	@Column(name = "GOODS_NAME")
	private String goodsName; // 품목명
	
	@Column(name = "ERP_GOODS_CODE")
	private String erpGoodsCode;

	@Column(name = "WAREHOUSE_CODE")
	private String warehouseCode; // 창고코드
	
	@Column(name = "GOODS_UNIT")
	private String goodsUnit; // 품목단위
	
	@Column(name = "BUNDLE_YN")
	private String bundleYn; // 묶음상품여부

	@Column(name = "WMS_API_YN")
	private String wmsApiYn;

	@Column(name = "BARCODE")
	private String barcode;

	@Column(name = "TAX_YN")
	private String taxYn;

	@Column(name = "SUBSCRIPTION_TYPE")
	private String subscriptionType;

	@Column(name = "GOODS_TEMPERATURE")
	private String goodsTemperature;

	@Column(name = "EXCLUSIVE_YN")
	private String exclusiveYn;

	@Column(name = "SALES_STATUS")
	private String salesStatus;

	@Column(name = "CATEGORY_1")
	private String category1;

	@Column(name = "CATEGORY_2")
	private String category2;

	@Column(name = "CATEGORY_3")
	private String category3;

	@Column(name = "CATEGORY_4")
	private String category4;

	@Column(name = "OPTION_YN")
	private String optionYn;

	@Column(name = "OPTION_TYPE")
	private String optionType;

	@Column(name = "OPTION_TITLE")
	private String optionTitle;

	@Column(name = "ORIGIN_CODE")
	private String originCode;

	@Column(name = "SUPPLIER_CODE")
	private String supplierCode;

	@Column(name = "EXPIRATION_MONTH")
	private String expirationMonth;

	@Column(name = "EXPIRATION_DAY")
	private String expirationDay;

	@Column(name = "USE_YN")
	private String useYn;

	@Column(name = "GOODS_DESCRIPTION")
	private String goodsDescription;

	@Column(name = "ADMIN_MEMO")
	private String adminMemo;

	@Column(name = "GOODS_NAME_SHORT")
	private String goodsNameShort;

	@Column(name = "GOODS_KEYWORDS")
	private String goodsKeywords;

	public Goods(GoodsId id) {
		this.companyCode = id.getCompanyCode();
		this.goodsCode = id.getGoodsCode();
		this.subscriptionType = "NS";
	}
}
