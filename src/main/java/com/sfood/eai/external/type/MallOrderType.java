package com.sfood.eai.external.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MallOrderType {
    SABANGNET( "M0001", "사방넷",Constants.SFOOD_COMPANY_CODE, ChannelType.SABANGNET, true),
    MEDISOLA_SABANGNET( "M0001", "사방넷",Constants.MEDISOLA_COMPANY_CODE, ChannelType.SABANGNET, true),
    SFOOD("M0002", "에쓰푸드몰", Constants.SFOOD_COMPANY_CODE, ChannelType.GODOMALL,  true),
    MEDISOLA_JHONCOOK("M0002", "메디쏠라몰", Constants.MEDISOLA_COMPANY_CODE, ChannelType.GODOMALL,  true),
    JHONCOOK("M0003", "존쿡몰", Constants.SFOOD_COMPANY_CODE, ChannelType.GODOMALL,  true),
    PRM("M0004", "PRM", Constants.SFOOD_COMPANY_CODE, ChannelType.PRM,  false),
    RETAIL("M0005", "리테일", Constants.SFOOD_COMPANY_CODE, ChannelType.RETAIL,  false),
    MANUAL("OM001", "수기주문", Constants.SFOOD_COMPANY_CODE, ChannelType.MANUAL, false),
    CARRYOVER("OM002", "매출이월",Constants.SFOOD_COMPANY_CODE, ChannelType.MANUAL,  false),
    REVERSE("OM003", "역발행", Constants.SFOOD_COMPANY_CODE, ChannelType.MANUAL,  false),
    TRANSFER("OM004", "재고이동", Constants.SFOOD_COMPANY_CODE, ChannelType.MANUAL, false),
    NONE("M0000","미지정", Constants.SFOOD_COMPANY_CODE, ChannelType.MANUAL,false)
    ;

    private final String code;
    private final String mallName;
    private final String companyCode;
    private final ChannelType channelType;
    private final boolean onlineMall;

    public String code() {
        return this.code;
    }

    public String mallName() {
        return this.mallName;
    }

    public String companyCode() {
        return this.companyCode;
    }

    public ChannelType channelType() {
        return this.channelType;
    }

    public boolean isOnlineMall() {
        return this.onlineMall;
    }
    public boolean equals(MallOrderType target) {
        return (this.companyCode.equals(target.companyCode) && this.code.equals(target.code));
    }

    public static MallOrderType fromByCode(String companyCode, String mallCode) {

        for (MallOrderType mall : MallOrderType.values()) {
            if(mall.companyCode.equals(companyCode) && mall.code.equals(mallCode)) {
                return mall;
            }
        }
        return NONE;
    }
}
