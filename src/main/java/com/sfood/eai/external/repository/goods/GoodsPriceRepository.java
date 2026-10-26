package com.sfood.eai.external.repository.goods;

import com.sfood.eai.external.entity.goods.PricingMaster;
import com.sfood.eai.external.entity.id.goods.PriceMasterId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsPriceRepository extends JpaRepository<PricingMaster, PriceMasterId>, GoodsPriceRepositoryCustom {
}
