package com.sfood.eai.external.entity.id.retail;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EdiOrderDataId implements Serializable {

    public static final long serialVersionUID = 1L;
    private String companyCode;
    private String ediType;
    private Integer lineNo;
    private String mesgFrom;    // 송신자ID, 기본 : 유통사 코드
    private String mesgId;      // 문서참조번호, EDI 메시지 고유 아이디, 주문번호
    private String mesgName;    // 문서명, I/F 고유 문서명
    private String mesgNo;
    private String mesgTo;      // 수신자ID, 기본 : 각 유통사 VAN 접속 파트너사 ID
    private String soDt;        // 발주일시(발주날짜), 주문일자


}
