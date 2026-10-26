package com.sfood.eai.external.type;

import com.mysema.commons.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChannelType {
    MANUAL("CN0000", "수기등록"),
    SABANGNET("CN0001", "사방넷"),
    GODOMALL("CN0002", "고도몰"),
    PRM("CN0003", "PRM"),
    RETAIL("CN0004", "리테일");


    private final String code;
    private final String name;

    public String code() {
        return this.code;
    }

    public static ChannelType of(final String code) {

        Assert.notNull(code, "code is not null");

        for (ChannelType type : ChannelType.values()) {
            if (type.code().equals(code)) {
                return type;
            }

        }

        return null;
    }
}
