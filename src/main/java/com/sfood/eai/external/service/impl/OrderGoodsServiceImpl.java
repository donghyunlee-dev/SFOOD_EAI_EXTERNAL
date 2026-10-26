package com.sfood.eai.external.service.impl;

import com.sfood.eai.external.dto.PartnerGoodsPrice;
import com.sfood.eai.external.entity.goods.*;
import com.sfood.eai.external.entity.id.goods.GoodsId;
import com.sfood.eai.external.entity.id.goods.GoodsOptionId;
import com.sfood.eai.external.entity.order.OrderGoods;
import com.sfood.eai.external.repository.goods.*;
import com.sfood.eai.external.repository.order.OrderGoodsRepository;
import com.sfood.eai.external.service.OrderGoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderGoodsServiceImpl implements OrderGoodsService {

    private final GoodsRepository goodsRepository;
    private final GoodsPriceRepository goodsPriceRepository;
    private final GoodsOptionRepository goodsOptionRepository;
    private final GoodsEdiPartnerRepository goodsEdiPartnerRepository;
    private final MallGoodsMatchRepository mallGoodsMatchRepository;

    @Transactional(readOnly = true)
    @Override
    public Goods getGoods(GoodsId id) {
        try {
            return goodsRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public GoodsOption getGoodsOption(GoodsOptionId id) {
        try {
            return goodsOptionRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public GoodsPrice findGoodsPrice(GoodsId id) {
        try {
            List<GoodsPrice> goodsPrice = goodsPriceRepository.findGoodsPrice(id.getCompanyCode(), id.getGoodsCode());

            if (CollectionUtils.isEmpty(goodsPrice)) {
                return null;
            }

            return goodsPrice.getFirst();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public SimpleGoods getSimpleGoods(GoodsId id) {
        try {
            if (!StringUtils.hasText(id.getGoodsCode())) {
                return null;
            }
            return goodsRepository.getSimpleGoods(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PartnerGoodsPrice> findPartnerGoodsPrices(String companyCode, String partnerCode, String goodsCode) {
        try {
            return goodsRepository.findMallGoodsPrice(companyCode, partnerCode, goodsCode);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<MallGoodsMatch> getMallOrderGoods(String companyCode, String mallCode, String mallGoodsCode, String optionCode) {
        try {
            optionCode = StringUtils.hasText(optionCode) ? optionCode : "-";

            return mallGoodsMatchRepository.findByMallGoods(companyCode, mallCode, mallGoodsCode, optionCode);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public SimpleGoods getGoodsByEdiGoodsCode(String companyCode, String mallCode, String ediGoodsCode, int exchangeRate) {
        try {
            Optional<GoodsEdiPartner> ediGoods = goodsEdiPartnerRepository.findByCompanyCodeAndMallCodeAndPartnerGoodsCodeAndExchangeFigure(companyCode, mallCode, ediGoodsCode, exchangeRate);

            if (ediGoods.isEmpty()) {
                return null;
            }

            return goodsRepository.getSimpleGoods(new GoodsId(companyCode, ediGoods.get().getGoodsCode()));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public SimpleGoods getGoodsByMall(String companyCode, String mallCode, String goodsCode) {
        try {

            if (!StringUtils.hasText(goodsCode)) {
                return null;
            }

            List<SimpleGoods> goodsCodes = goodsRepository.getGoodsByMall(companyCode, mallCode, goodsCode);

            if (goodsCodes.isEmpty()) {
                return null;
            }

            return goodsCodes.getFirst();

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
