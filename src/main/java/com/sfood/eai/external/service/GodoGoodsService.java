package com.sfood.eai.external.service;

import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.godo.GodoGoodsReq;

public interface GodoGoodsService {

    /**
     * 고도몰 신규 품목 등록
     * @param goodsReq 고도몰 품목 정보
     * @return 전송 결과
     * @throws Exception 오류 발생 시 예외 처리
     */
    CommonDataRes sendNewGoods(GodoGoodsReq goodsReq) throws Exception;

    /**
     * 고도몰 품목 정보 수정
     * @param goodsReq 고도몰 품목 정보
     * @return 전송 결과
     * @throws Exception 오류 발생 시 예외 처리
     */
    CommonDataRes sendGoodsUpdateInfo(GodoGoodsReq goodsReq) throws Exception;
}
