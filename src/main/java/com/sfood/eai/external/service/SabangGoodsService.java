package com.sfood.eai.external.service;

import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.sabang.SabangGoodsLiteReq;
import com.sfood.eai.external.dto.sabang.SabangGoodsReq;

public interface SabangGoodsService {

    CommonDataRes sendGoodsInfo(SabangGoodsReq goodsReq) throws Exception;

    CommonDataRes sendGoodsLiteInfo(SabangGoodsLiteReq goodsReq) throws Exception;
}
